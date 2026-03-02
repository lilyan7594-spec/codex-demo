package com.example.demo.api.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RegisterApplianceRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-02T09:31:43.352578+08:00[Asia/Shanghai]", comments = "Generator version: 7.19.0")
public class RegisterApplianceRequest {

  private String applianceName;

  public RegisterApplianceRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RegisterApplianceRequest(String applianceName) {
    this.applianceName = applianceName;
  }

  public RegisterApplianceRequest applianceName(String applianceName) {
    this.applianceName = applianceName;
    return this;
  }

  /**
   * Get applianceName
   * @return applianceName
   */
  @NotNull @Size(min = 1, max = 128) 
  @JsonProperty("appliance_name")
  public String getApplianceName() {
    return applianceName;
  }

  public void setApplianceName(String applianceName) {
    this.applianceName = applianceName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegisterApplianceRequest registerApplianceRequest = (RegisterApplianceRequest) o;
    return Objects.equals(this.applianceName, registerApplianceRequest.applianceName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(applianceName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegisterApplianceRequest {\n");
    sb.append("    applianceName: ").append(toIndentedString(applianceName)).append("\n");
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

