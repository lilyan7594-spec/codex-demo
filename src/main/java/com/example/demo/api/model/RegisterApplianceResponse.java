package com.example.demo.api.model;

import java.net.URI;
import java.util.Objects;
import com.example.demo.api.model.RegisterApplianceData;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RegisterApplianceResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-14T14:32:48.708622200+08:00[Asia/Shanghai]", comments = "Generator version: 7.19.0")
public class RegisterApplianceResponse {

  private String code;

  private String message;

  private RegisterApplianceData data;

  /**
   * Default constructor.
   */
  public RegisterApplianceResponse() {
    super();
  }

  /**
   * 仅包含必填参数的构造函数。
   */
  public RegisterApplianceResponse(String code, String message, RegisterApplianceData data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }

  /**
   * 链式设置 code。
   *
   * @param code 响应码
   * @return 当前对象
   */
  public RegisterApplianceResponse code(String code) {
    this.code = code;
    return this;
  }

  /**
   * 获取 code。
   *
   * @return 响应码
   */
  @NotNull 
  @JsonProperty("code")
  public String getCode() {
    return code;
  }

  /**
   * 设置 code。
   *
   * @param code 响应码
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * 链式设置 message。
   *
   * @param message 响应消息
   * @return 当前对象
   */
  public RegisterApplianceResponse message(String message) {
    this.message = message;
    return this;
  }

  /**
   * 获取 message。
   *
   * @return 响应消息
   */
  @NotNull 
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  /**
   * 设置 message。
   *
   * @param message 响应消息
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * 链式设置 data。
   *
   * @param data 响应数据
   * @return 当前对象
   */
  public RegisterApplianceResponse data(RegisterApplianceData data) {
    this.data = data;
    return this;
  }

  /**
   * 获取 data。
   *
   * @return 响应数据
   */
  @NotNull @Valid 
  @JsonProperty("data")
  public RegisterApplianceData getData() {
    return data;
  }

  /**
   * 设置 data。
   *
   * @param data 响应数据
   */
  public void setData(RegisterApplianceData data) {
    this.data = data;
  }

  /**
   * 比较当前对象与目标对象是否相等。
   *
   * @param o 目标对象
   * @return 所有字段相等时返回 true
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegisterApplianceResponse registerApplianceResponse = (RegisterApplianceResponse) o;
    return Objects.equals(this.code, registerApplianceResponse.code) &&
        Objects.equals(this.message, registerApplianceResponse.message) &&
        Objects.equals(this.data, registerApplianceResponse.data);
  }

  /**
   * 计算当前对象哈希值。
   *
   * @return 哈希值
   */
  @Override
  public int hashCode() {
    return Objects.hash(code, message, data);
  }

  /**
   * 生成调试用字符串。
   *
   * @return 可读字符串
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegisterApplianceResponse {\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * 将对象转为字符串，并在换行后增加缩进（首行除外）。
   */
  private String toIndentedString(@Nullable Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
