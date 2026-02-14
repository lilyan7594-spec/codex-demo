package com.example.demo.service;

import com.example.demo.SmsApi;
import com.example.demo.api.model.RegisterApplianceData;
import com.example.demo.api.model.RegisterApplianceRequest;
import com.example.demo.util.FingerprintUtil;
import com.example.demo.util.LocalIpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.Objects;

/**
 * Appliance 注册业务服务，负责组装本机信息并调用外部注册接口。
 */
@Service
public class ApplianceRegistrationService {

    private final SmsApi smsApi;
    private final String accountAk;
    private final String accountSk;
    private final String cloudDomain;

    /**
     * 构造注册服务。
     *
     * @param smsApi 外部注册调用组件
     * @param accountAk 本地保存的云账号 AK
     * @param accountSk 本地保存的云账号 SK
     * @param cloudDomain 云服务域名
     */
    @Autowired
    public ApplianceRegistrationService(
            SmsApi smsApi,
            @Value("${appliance.cloud-account.account-ak}") String accountAk,
            @Value("${appliance.cloud-account.account-sk}") String accountSk,
            @Value("${appliance.cloud-account.domain}") String cloudDomain
    ) {
        this.smsApi = smsApi;
        this.accountAk = accountAk;
        this.accountSk = accountSk;
        this.cloudDomain = cloudDomain;
    }

    /**
     * 注册 Appliance 实例。
     *
     * @param request 前端触发请求（当前仅保留 force，可选）
     * @return 注册结果
     */
    public RegisterResult register(RegisterApplianceRequest request) {
        validateConfig();
        String domain = cloudDomain.trim();
        String fingerprint = FingerprintUtil.generate(domain);
        String ip = resolveIp(domain);
        String hostname = resolveHostname();

        SmsApi.SmsRegisterRequest outboundRequest = new SmsApi.SmsRegisterRequest(
                accountAk.trim(),
                accountSk.trim(),
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
     * 校验本地配置合法性。
     */
    private void validateConfig() {
        if (accountAk == null || accountAk.isBlank()) {
            throw new IllegalArgumentException("appliance.cloud-account.account-ak is required");
        }
        if (accountSk == null || accountSk.isBlank()) {
            throw new IllegalArgumentException("appliance.cloud-account.account-sk is required");
        }
        if (cloudDomain == null || cloudDomain.isBlank()) {
            throw new IllegalArgumentException("appliance.cloud-account.domain is required");
        }
    }

    /**
     * 解析注册上报 IP：优先出口 IP，最后内网网卡兜底。
     *
     * @param domain 云服务域名
     * @return 解析得到的 IP，失败时返回空字符串
     */
    private static String resolveIp(String domain) {
        String outboundIp = LocalIpUtil.resolveOutboundIp(domain, 443);
        if (outboundIp != null && !outboundIp.isBlank()) {
            return outboundIp;
        }
        String fallback = LocalIpUtil.resolveLanIpv4();
        return Objects.requireNonNullElse(fallback, "");
    }

    /**
     * 解析本机主机名。
     *
     * @return 主机名，失败时返回空字符串
     */
    private static String resolveHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception ignored) {
            return "";
        }
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
