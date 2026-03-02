package com.example.demo.service;

import com.example.demo.SmsApi;
import com.example.demo.api.model.RegisterApplianceData;
import com.example.demo.api.model.RegisterApplianceRequest;
import com.example.demo.util.FingerprintUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Appliance 注册业务服务，负责组装参数并调用云端注册接口。
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
     * @param smsApi 云端注册调用组件
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
     * @param request 前端注册请求
     * @return 注册结果
     */
    public RegisterResult register(RegisterApplianceRequest request) {
        validateConfig();
        validateRequest(request);

        String domain = cloudDomain.trim();
        String fingerprint = FingerprintUtil.generate(domain);
        String applianceName = request.getApplianceName().trim();

        SmsApi.SmsRegisterRequest outboundRequest = new SmsApi.SmsRegisterRequest(
                accountAk.trim(),
                accountSk.trim(),
                domain,
                fingerprint,
                applianceName
        );

        SmsApi.SmsRegisterResponse outboundResponse = smsApi.register(outboundRequest);
        return new RegisterResult(outboundResponse.applianceId(), outboundResponse.isNew(), applianceName, fingerprint);
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
     * 校验前端请求参数。
     *
     * @param request 前端注册请求
     */
    private void validateRequest(RegisterApplianceRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request body is required");
        }
        if (request.getApplianceName() == null || request.getApplianceName().isBlank()) {
            throw new IllegalArgumentException("appliance_name is required");
        }
        if (request.getApplianceName().trim().length() > 128) {
            throw new IllegalArgumentException("appliance_name length must be <= 128");
        }
    }

    /**
     * 注册结果对象。
     *
     * @param applianceId Appliance 标识
     * @param isNew 是否为新建
     * @param applianceName 设备名称
     * @param instanceFingerprint 实例指纹
     */
    public record RegisterResult(String applianceId, boolean isNew, String applianceName, String instanceFingerprint) {
    }
}
