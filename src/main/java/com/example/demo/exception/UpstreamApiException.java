package com.example.demo.exception;

/**
 * 表示调用外部注册接口时产生的业务异常。
 */
public class UpstreamApiException extends RuntimeException {

    private final String code;

    /**
     * 构造异常对象。
     *
     * @param code 业务错误码
     * @param message 错误信息
     */
    public UpstreamApiException(String code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构造异常对象。
     *
     * @param code 业务错误码
     * @param message 错误信息
     * @param cause 原始异常
     */
    public UpstreamApiException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * 返回业务错误码。
     *
     * @return 错误码
     */
    public String getCode() {
        return code;
    }
}
