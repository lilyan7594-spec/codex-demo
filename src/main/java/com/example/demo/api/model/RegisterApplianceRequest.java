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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-14T17:10:22.418536+08:00[Asia/Shanghai]", comments = "Generator version: 7.19.0")
public class RegisterApplianceRequest {

  private @Nullable Boolean force;

  public RegisterApplianceRequest force(@Nullable Boolean force) {
    this.force = force;
    return this;
  }

  /**
   * Optional. Whether to force a fresh registration attempt.
   * @return force
   */
  
  @JsonProperty("force")
  public @Nullable Boolean getForce() {
    return force;
  }

  public void setForce(@Nullable Boolean force) {
    this.force = force;
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
    return Objects.equals(this.force, registerApplianceRequest.force);
  }

  @Override
  public int hashCode() {
    return Objects.hash(force);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegisterApplianceRequest {\n");
    sb.append("    force: ").append(toIndentedString(force)).append("\n");
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

