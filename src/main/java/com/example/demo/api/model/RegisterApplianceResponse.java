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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-14T17:10:22.418536+08:00[Asia/Shanghai]", comments = "Generator version: 7.19.0")
public class RegisterApplianceResponse {

  private String code;

  private String message;

  private RegisterApplianceData data;

  public RegisterApplianceResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RegisterApplianceResponse(String code, String message, RegisterApplianceData data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }

  public RegisterApplianceResponse code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Get code
   * @return code
   */
  @NotNull 
  @JsonProperty("code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public RegisterApplianceResponse message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  @NotNull 
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public RegisterApplianceResponse data(RegisterApplianceData data) {
    this.data = data;
    return this;
  }

  /**
   * Get data
   * @return data
   */
  @NotNull @Valid 
  @JsonProperty("data")
  public RegisterApplianceData getData() {
    return data;
  }

  public void setData(RegisterApplianceData data) {
    this.data = data;
  }

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

  @Override
  public int hashCode() {
    return Objects.hash(code, message, data);
  }

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
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(@Nullable Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

