package com.example.demo.api;

import org.springframework.web.context.request.NativeWebRequest;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiUtil {
    /**
     * 工具类不允许实例化。
     */
    private ApiUtil() {
    }

    /**
     * 向响应写入示例报文（供生成代码的默认实现使用）。
     *
     * @param req 原生请求包装对象
     * @param contentType 响应内容类型
     * @param example 示例响应体
     */
    public static void setExampleResponse(NativeWebRequest req, String contentType, String example) {
        try {
            HttpServletResponse res = req.getNativeResponse(HttpServletResponse.class);
            if (res != null) {
                res.setCharacterEncoding("UTF-8");
                res.addHeader("Content-Type", contentType);
                res.getWriter().print(example);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
