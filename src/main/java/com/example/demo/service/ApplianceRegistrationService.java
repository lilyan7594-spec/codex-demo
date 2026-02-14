package com.example.demo.service;

import com.example.demo.SmsApi;
import com.example.demo.api.model.RegisterApplianceData;
import com.example.demo.api.model.RegisterApplianceRequest;
import com.example.demo.util.FingerprintUtil;
import com.example.demo.util.LocalIpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Appliance 注册业务服务，负责参数处理与调用外部注册接口。
 */
@Service
public class ApplianceRegistrationService {

    private final SmsApi smsApi;

    /**
     * 构造注册服务。
     *
     * @param smsApi 外部注册调用组件
     */
    @Autowired
    public ApplianceRegistrationService(SmsApi smsApi) {
        this.smsApi = smsApi;
    }

    /**
     * 注册 Appliance 实例。
     *
     * @param request 注册请求
     * @return 注册结果
     */
    public RegisterResult register(RegisterApplianceRequest request) {
        validateRequired(request);
        String domain = request.getDomain().trim();
        String fingerprint = resolveFingerprint(request, domain);
        String ip = resolveIp(request, domain);
        String hostname = request.getHostname() == null ? "" : request.getHostname().trim();

        SmsApi.SmsRegisterRequest outboundRequest = new SmsApi.SmsRegisterRequest(
                request.getAccountAk().trim(),
                request.getAccountSk().trim(),
                domain,
                fingerprint,
                hostname,
                ip
        );
        SmsApi.SmsRegisterResponse outboundResponse = smsApi.register(outboundRequest);

        return new RegisterResult(outboundResponse.applianceId(), outboundResponse.isNew(), ip, fingerprint);
    }

    /**
     * 将内部结果转换为 API 返回模型。
     *
     * @param result 内部注册结果
     * @return API 数据对象
     */
    public RegisterApplianceData toApiData(RegisterResult result) {
        return new RegisterApplianceData()
                .applianceId(result.applianceId())
                .isNew(result.isNew());
    }

    /**
     * 校验请求必填字段。
     *
     * @param request 注册请求
     */
    private static void validateRequired(RegisterApplianceRequest request) {
        if (request.getAccountAk() == null || request.getAccountAk().isBlank()) {
            throw new IllegalArgumentException("account_ak is required");
        }
        if (request.getAccountSk() == null || request.getAccountSk().isBlank()) {
            throw new IllegalArgumentException("account_sk is required");
        }
        if (request.getDomain() == null || request.getDomain().isBlank()) {
            throw new IllegalArgumentException("domain is required");
        }
    }

    /**
     * 优先使用请求中的指纹，否则在本地生成指纹。
     *
     * @param request 注册请求
     * @param domain 云服务域名
     * @return 标准化后的指纹
     */
    private static String resolveFingerprint(RegisterApplianceRequest request, String domain) {
        if (request.getInstanceFingerprint() != null && !request.getInstanceFingerprint().isBlank()) {
            String provided = request.getInstanceFingerprint().trim().toLowerCase();
            if (!provided.matches("^[0-9a-f]{64}$")) {
                throw new IllegalArgumentException("instance_fingerprint must be 64-char hex");
            }
            return provided;
        }
        return FingerprintUtil.generate(domain);
    }

    /**
     * 解析注册上报 IP：优先请求字段，其次出口 IP，最后内网网卡兜底。
     *
     * @param request 注册请求
     * @param domain 云服务域名
     * @return 解析得到的 IP，失败时返回空字符串
     */
    private static String resolveIp(RegisterApplianceRequest request, String domain) {
        if (request.getIp() != null && !request.getIp().isBlank()) {
            return request.getIp().trim();
        }
        String outboundIp = LocalIpUtil.resolveOutboundIp(domain, 443);
        if (outboundIp != null && !outboundIp.isBlank()) {
            return outboundIp;
        }
        String fallback = LocalIpUtil.resolveLanIpv4();
        return Objects.requireNonNullElse(fallback, "");
    }

    /**
     * 注册结果对象。
     *
     * @param applianceId Appliance 标识
     * @param isNew 是否为新建
     * @param ip 注册时上报 IP
     * @param instanceFingerprint 实例指纹
     */
    public record RegisterResult(String applianceId, boolean isNew, String ip, String instanceFingerprint) {
    }
}
