package com.example.demo.controller;

import com.example.demo.api.ApplianceApi;
import com.example.demo.api.model.RegisterApplianceRequest;
import com.example.demo.api.model.RegisterApplianceResponse;
import com.example.demo.service.ApplianceRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplianceApiController implements ApplianceApi {

    private final ApplianceRegistrationService registrationService;

    /**
     * 构造注册控制器。
     *
     * @param registrationService 注册服务
     */
    @Autowired
    public ApplianceApiController(ApplianceRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * 处理 Appliance 注册请求。
     *
     * @param registerApplianceRequest 注册请求体
     * @return 注册响应
     */
    @Override
    public ResponseEntity<RegisterApplianceResponse> registerAppliance(RegisterApplianceRequest registerApplianceRequest) {
        ApplianceRegistrationService.RegisterResult result = registrationService.register(registerApplianceRequest);

        RegisterApplianceResponse response = new RegisterApplianceResponse()
                .code("0")
                .message("success")
                .data(registrationService.toApiData(result));

        return ResponseEntity.ok(response);
    }
}
