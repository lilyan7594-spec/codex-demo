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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-14T14:32:48.708622200+08:00[Asia/Shanghai]", comments = "Generator version: 7.19.0")
public class RegisterApplianceData {

  private String applianceId;

  private Boolean isNew;

  /**
   * Default constructor.
   */
  public RegisterApplianceData() {
    super();
  }

  /**
   * 仅包含必填参数的构造函数。
   */
  public RegisterApplianceData(String applianceId, Boolean isNew) {
    this.applianceId = applianceId;
    this.isNew = isNew;
  }

  /**
   * 链式设置 applianceId。
   *
   * @param applianceId Appliance 标识
   * @return 当前对象
   */
  public RegisterApplianceData applianceId(String applianceId) {
    this.applianceId = applianceId;
    return this;
  }

  /**
   * 获取 applianceId。
   *
   * @return Appliance 标识
   */
  @NotNull 
  @JsonProperty("appliance_id")
  public String getApplianceId() {
    return applianceId;
  }

  /**
   * 设置 applianceId。
   *
   * @param applianceId Appliance 标识
   */
  public void setApplianceId(String applianceId) {
    this.applianceId = applianceId;
  }

  /**
   * 链式设置是否新建标志。
   *
   * @param isNew 是否为新建注册
   * @return 当前对象
   */
  public RegisterApplianceData isNew(Boolean isNew) {
    this.isNew = isNew;
    return this;
  }

  /**
   * 获取是否新建标志。
   *
   * @return 是否新建
   */
  @NotNull 
  @JsonProperty("is_new")
  public Boolean getIsNew() {
    return isNew;
  }

  /**
   * 设置是否新建标志。
   *
   * @param isNew 是否为新建注册
   */
  public void setIsNew(Boolean isNew) {
    this.isNew = isNew;
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
    RegisterApplianceData registerApplianceData = (RegisterApplianceData) o;
    return Objects.equals(this.applianceId, registerApplianceData.applianceId) &&
        Objects.equals(this.isNew, registerApplianceData.isNew);
  }

  /**
   * 计算当前对象哈希值。
   *
   * @return 哈希值
   */
  @Override
  public int hashCode() {
    return Objects.hash(applianceId, isNew);
  }

  /**
   * 生成调试用字符串。
   *
   * @return 可读字符串
   */
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
   * 将对象转为字符串，并在换行后增加缩进（首行除外）。
   */
  private String toIndentedString(@Nullable Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
