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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-02-14T14:32:48.708622200+08:00[Asia/Shanghai]", comments = "Generator version: 7.19.0")
public class RegisterApplianceRequest {

  private String accountAk;

  private String accountSk;

  private String domain;

  private @Nullable String hostname;

  private @Nullable String ip;

  private @Nullable String instanceFingerprint;

  /**
   * Default constructor.
   */
  public RegisterApplianceRequest() {
    super();
  }

  /**
   * 仅包含必填参数的构造函数。
   */
  public RegisterApplianceRequest(String accountAk, String accountSk, String domain) {
    this.accountAk = accountAk;
    this.accountSk = accountSk;
    this.domain = domain;
  }

  /**
   * 链式设置 accountAk。
   *
   * @param accountAk 账号 AK
   * @return 当前对象
   */
  public RegisterApplianceRequest accountAk(String accountAk) {
    this.accountAk = accountAk;
    return this;
  }

  /**
   * 获取 accountAk。
   *
   * @return 账号 AK
   */
  @NotNull @Size(min = 1) 
  @JsonProperty("account_ak")
  public String getAccountAk() {
    return accountAk;
  }

  /**
   * 设置 accountAk。
   *
   * @param accountAk 账号 AK
   */
  public void setAccountAk(String accountAk) {
    this.accountAk = accountAk;
  }

  /**
   * 链式设置 accountSk。
   *
   * @param accountSk 账号 SK
   * @return 当前对象
   */
  public RegisterApplianceRequest accountSk(String accountSk) {
    this.accountSk = accountSk;
    return this;
  }

  /**
   * 获取 accountSk。
   *
   * @return 账号 SK
   */
  @NotNull @Size(min = 1) 
  @JsonProperty("account_sk")
  public String getAccountSk() {
    return accountSk;
  }

  /**
   * 设置 accountSk。
   *
   * @param accountSk 账号 SK
   */
  public void setAccountSk(String accountSk) {
    this.accountSk = accountSk;
  }

  /**
   * 链式设置 domain。
   *
   * @param domain 云服务域名
   * @return 当前对象
   */
  public RegisterApplianceRequest domain(String domain) {
    this.domain = domain;
    return this;
  }

  /**
   * 获取 domain。
   *
   * @return 云服务域名
   */
  @NotNull @Size(min = 1) 
  @JsonProperty("domain")
  public String getDomain() {
    return domain;
  }

  /**
   * 设置 domain。
   *
   * @param domain 云服务域名
   */
  public void setDomain(String domain) {
    this.domain = domain;
  }

  /**
   * 链式设置 hostname。
   *
   * @param hostname 主机名
   * @return 当前对象
   */
  public RegisterApplianceRequest hostname(@Nullable String hostname) {
    this.hostname = hostname;
    return this;
  }

  /**
   * 获取 hostname。
   *
   * @return 主机名
   */
  
  @JsonProperty("hostname")
  public @Nullable String getHostname() {
    return hostname;
  }

  /**
   * 设置 hostname。
   *
   * @param hostname 主机名
   */
  public void setHostname(@Nullable String hostname) {
    this.hostname = hostname;
  }

  /**
   * 链式设置 ip。
   *
   * @param ip IP 地址
   * @return 当前对象
   */
  public RegisterApplianceRequest ip(@Nullable String ip) {
    this.ip = ip;
    return this;
  }

  /**
   * 获取 ip。
   *
   * @return IP 地址
   */
  
  @JsonProperty("ip")
  public @Nullable String getIp() {
    return ip;
  }

  /**
   * 设置 ip。
   *
   * @param ip IP 地址
   */
  public void setIp(@Nullable String ip) {
    this.ip = ip;
  }

  /**
   * 链式设置 instanceFingerprint。
   *
   * @param instanceFingerprint 实例指纹
   * @return 当前对象
   */
  public RegisterApplianceRequest instanceFingerprint(@Nullable String instanceFingerprint) {
    this.instanceFingerprint = instanceFingerprint;
    return this;
  }

  /**
   * 获取 instanceFingerprint。
   *
   * @return 实例指纹（可为空；为空时由 Appliance 生成）
   */
  
  @JsonProperty("instance_fingerprint")
  public @Nullable String getInstanceFingerprint() {
    return instanceFingerprint;
  }

  /**
   * 设置 instanceFingerprint。
   *
   * @param instanceFingerprint 实例指纹
   */
  public void setInstanceFingerprint(@Nullable String instanceFingerprint) {
    this.instanceFingerprint = instanceFingerprint;
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
    RegisterApplianceRequest registerApplianceRequest = (RegisterApplianceRequest) o;
    return Objects.equals(this.accountAk, registerApplianceRequest.accountAk) &&
        Objects.equals(this.accountSk, registerApplianceRequest.accountSk) &&
        Objects.equals(this.domain, registerApplianceRequest.domain) &&
        Objects.equals(this.hostname, registerApplianceRequest.hostname) &&
        Objects.equals(this.ip, registerApplianceRequest.ip) &&
        Objects.equals(this.instanceFingerprint, registerApplianceRequest.instanceFingerprint);
  }

  /**
   * 计算当前对象哈希值。
   *
   * @return 哈希值
   */
  @Override
  public int hashCode() {
    return Objects.hash(accountAk, accountSk, domain, hostname, ip, instanceFingerprint);
  }

  /**
   * 生成调试用字符串。
   *
   * @return 可读字符串
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegisterApplianceRequest {\n");
    sb.append("    accountAk: ").append(toIndentedString(accountAk)).append("\n");
    sb.append("    accountSk: ").append(toIndentedString(accountSk)).append("\n");
    sb.append("    domain: ").append(toIndentedString(domain)).append("\n");
    sb.append("    hostname: ").append(toIndentedString(hostname)).append("\n");
    sb.append("    ip: ").append(toIndentedString(ip)).append("\n");
    sb.append("    instanceFingerprint: ").append(toIndentedString(instanceFingerprint)).append("\n");
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
