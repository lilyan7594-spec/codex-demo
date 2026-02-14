package com.example.demo.controller;

import com.example.demo.api.model.RegisterApplianceResponse;
import com.example.demo.exception.UpstreamApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 将参数异常转换为统一的 400 响应。
     *
     * @param ex 参数校验异常
     * @return 错误响应（A400）
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RegisterApplianceResponse> handleBadRequest(IllegalArgumentException ex) {
        RegisterApplianceResponse response = new RegisterApplianceResponse()
                .code("A400")
                .message(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 将外部接口调用异常转换为统一响应。
     *
     * @param ex 外部接口异常
     * @return 错误响应（A502/A503/A500_PARSE_ERROR 等）
     */
    @ExceptionHandler(UpstreamApiException.class)
    public ResponseEntity<RegisterApplianceResponse> handleUpstream(UpstreamApiException ex) {
        RegisterApplianceResponse response = new RegisterApplianceResponse()
                .code(ex.getCode())
                .message(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
    }
}
