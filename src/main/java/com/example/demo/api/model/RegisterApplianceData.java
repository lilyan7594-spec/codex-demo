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
 * RegisterApplianceData
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-03-02T09:31:43.352578+08:00[Asia/Shanghai]", comments = "Generator version: 7.19.0")
public class RegisterApplianceData {

  private String applianceId;

  private Boolean isNew;

  public RegisterApplianceData() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RegisterApplianceData(String applianceId, Boolean isNew) {
    this.applianceId = applianceId;
    this.isNew = isNew;
  }

  public RegisterApplianceData applianceId(String applianceId) {
    this.applianceId = applianceId;
    return this;
  }

  /**
   * Get applianceId
   * @return applianceId
   */
  @NotNull 
  @JsonProperty("appliance_id")
  public String getApplianceId() {
    return applianceId;
  }

  public void setApplianceId(String applianceId) {
    this.applianceId = applianceId;
  }

  public RegisterApplianceData isNew(Boolean isNew) {
    this.isNew = isNew;
    return this;
  }

  /**
   * Get isNew
   * @return isNew
   */
  @NotNull 
  @JsonProperty("is_new")
  public Boolean getIsNew() {
    return isNew;
  }

  public void setIsNew(Boolean isNew) {
    this.isNew = isNew;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegisterApplianceData registerApplianceData = (RegisterApplianceData) o;
    return Objects.equals(this.applianceId, registerApplianceData.applianceId) &&
        Objects.equals(this.isNew, registerApplianceData.isNew);
  }

  @Override
  public int hashCode() {
    return Objects.hash(applianceId, isNew);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegisterApplianceData {\n");
    sb.append("    applianceId: ").append(toIndentedString(applianceId)).append("\n");
    sb.append("    isNew: ").append(toIndentedString(isNew)).append("\n");
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

