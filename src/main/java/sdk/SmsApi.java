package com.example.demo;

import com.example.demo.exception.UpstreamApiException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;

/**
 * 封装外部注册接口调用：创建客户端、发送请求、解析响应。
 */
@Service
public class SmsApi {

    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String host;
    private final int port;
    private final String registerPath;

    /**
     * 构造 SmsApi，并在初始化阶段创建 OkHttpClient。
     *
     * @param objectMapper JSON 序列化组件
     * @param host 云服务主机
     * @param port 云服务端口
     * @param registerPath 注册接口路径
     * @param connectTimeoutMs 连接超时时间（毫秒）
     * @param readTimeoutMs 读取超时时间（毫秒）
     * @param writeTimeoutMs 写入超时时间（毫秒）
     */
    @Autowired
    public SmsApi(
            ObjectMapper objectMapper,
            @Value("${sms.api.host}") String host,
            @Value("${sms.api.port}") int port,
            @Value("${sms.api.register-path}") String registerPath,
            @Value("${sms.api.connect-timeout-ms:3000}") long connectTimeoutMs,
            @Value("${sms.api.read-timeout-ms:5000}") long readTimeoutMs,
            @Value("${sms.api.write-timeout-ms:5000}") long writeTimeoutMs
    ) {
        this.objectMapper = objectMapper;
        this.host = host;
        this.port = port;
        this.registerPath = registerPath;
        this.httpClient = buildClient(connectTimeoutMs, readTimeoutMs, writeTimeoutMs);
    }

    /**
     * 调用外部注册接口。
     *
     * @param request 外部注册请求
     * @return 外部注册响应
     */
    public SmsRegisterResponse register(SmsRegisterRequest request) {
        try {
            String json = objectMapper.writeValueAsString(request);
            RequestBody requestBody = RequestBody.create(json, JSON_MEDIA_TYPE);
            Request httpRequest = new Request.Builder()
                    .url(buildRegisterUrl())
                    .post(requestBody)
                    .build();

            try (Response response = httpClient.newCall(httpRequest).execute()) {
                return parseResponse(response);
            }
        } catch (UpstreamApiException ex) {
            throw ex;
        } catch (IOException ex) {
            throw new UpstreamApiException("A502", "调用外部注册接口失败", ex);
        }
    }

    /**
     * 创建 OkHttpClient。
     *
     * @param connectTimeoutMs 连接超时毫秒
     * @param readTimeoutMs 读取超时毫秒
     * @param writeTimeoutMs 写入超时毫秒
     * @return OkHttpClient 实例
     */
    private OkHttpClient buildClient(long connectTimeoutMs, long readTimeoutMs, long writeTimeoutMs) {
        return new OkHttpClient.Builder()
                .connectTimeout(Duration.ofMillis(connectTimeoutMs))
                .readTimeout(Duration.ofMillis(readTimeoutMs))
                .writeTimeout(Duration.ofMillis(writeTimeoutMs))
                .build();
    }

    /**
     * 组装注册接口完整 URL。
     *
     * @return 完整注册 URL
     */
    private HttpUrl buildRegisterUrl() {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(host)
                .port(port)
                .addPathSegments(trimLeadingSlash(registerPath))
                .build();
        return url;
    }

    /**
     * 去除路径前导斜杠，避免 PathSegments 拼接异常。
     *
     * @param path 接口路径
     * @return 处理后的路径
     */
    private String trimLeadingSlash(String path) {
        if (path == null || path.isBlank()) {
            throw new UpstreamApiException("A500_CONFIG", "sms.api.register-path 配置不能为空");
        }
        String value = path.trim();
        while (value.startsWith("/")) {
            value = value.substring(1);
        }
        return value;
    }

    /**
     * 解析外部 HTTP 响应。
     *
     * @param response HTTP 响应
     * @return 标准化注册响应
     */
    private SmsRegisterResponse parseResponse(Response response) {
        if (!response.isSuccessful()) {
            throw new UpstreamApiException("A503", "外部注册接口返回非成功状态: " + response.code());
        }
        ResponseBody body = response.body();
        if (body == null) {
            throw new UpstreamApiException("A500_PARSE_ERROR", "外部注册接口响应体为空");
        }
        try {
            RemoteRegisterResponse remote = objectMapper.readValue(body.string(), RemoteRegisterResponse.class);
            if (remote == null || remote.data == null) {
                throw new UpstreamApiException("A500_PARSE_ERROR", "外部注册接口响应格式错误");
            }
            if (!"0".equals(remote.code)) {
                throw new UpstreamApiException(remote.code == null ? "A503" : remote.code,
                        remote.message == null ? "外部注册接口业务失败" : remote.message);
            }
            return new SmsRegisterResponse(remote.data.applianceId, Boolean.TRUE.equals(remote.data.isNew));
        } catch (IOException ex) {
            throw new UpstreamApiException("A500_PARSE_ERROR", "外部注册接口响应解析失败", ex);
        }
    }

    /**
     * 外部注册请求 DTO。
     */
    public record SmsRegisterRequest(
            @JsonProperty("account_ak") String accountAk,
            @JsonProperty("account_sk") String accountSk,
            @JsonProperty("domain") String domain,
            @JsonProperty("instance_fingerprint") String instanceFingerprint,
            @JsonProperty("hostname") String hostname,
            @JsonProperty("ip") String ip
    ) {
    }

    /**
     * 外部注册响应 DTO（标准化后）。
     */
    public record SmsRegisterResponse(String applianceId, boolean isNew) {
    }

    /**
     * 外部原始响应模型。
     */
    private static class RemoteRegisterResponse {
        /**
         * 响应码。
         */
        public String code;
        /**
         * 响应信息。
         */
        public String message;
        /**
         * 响应数据。
         */
        public RemoteRegisterData data;
    }

    /**
     * 外部原始响应数据模型。
     */
    private static class RemoteRegisterData {
        /**
         * Appliance 唯一标识。
         */
        @JsonProperty("appliance_id")
        public String applianceId;
        /**
         * 是否新建。
         */
        @JsonProperty("is_new")
        public Boolean isNew;
    }
}
