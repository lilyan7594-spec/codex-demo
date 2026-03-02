# Appliance 侧注册功能设计

## 1. 需求背景
Appliance 需要在本地完成设备身份初始化，并向云服务注册获取 `appliance_id`。注册成功后，`appliance_id` 作为后续心跳与数据上报的稳定身份标识。

当前实现目标：
1. 前端只触发注册动作，不传敏感参数。
2. 后端内部组装注册参数（账号、域名、指纹、设备名）。
3. 注册调用云端接口，解析并返回 `appliance_id`。

---

## 2. 设计范围
包含：
1. Appliance 本地触发注册接口设计。
2. Appliance 后端注册编排逻辑。
3. Appliance 调用云端注册接口逻辑。
4. 注册参数来源、异常处理、配置项说明。

不包含：
1. 云端数据库落表实现细节。
2. 心跳接口与资产上报接口的完整设计。

---

## 3. 总体架构与调用链

调用链：
`ApplianceApiController -> ApplianceRegistrationService -> SmsApi -> Cloud Register API`

职责分层：
1. `ApplianceApiController`：提供本地 REST 接口，返回统一响应。
2. `ApplianceRegistrationService`：校验本地配置、生成指纹、获取主机信息、编排注册流程。
3. `SmsApi`：封装 HTTP 客户端、构造请求、调用云端、解析响应。

---

## 4. 接口设计

### 4.1 Appliance 本地注册触发接口
- 方法：`POST`
- 路径：`/api/v1/appliances/register`
- 请求体：

```json
{
  "appliance_name": "北京机房-Appliance1"
}
```
- 返回：

```json
{
  "code": "0",
  "message": "success",
  "data": {
    "appliance_id": "app-v-66238890",
    "is_new": false
  }
}
```

说明：
1. 前端不传 `AK/SK`。
2. 前端不传 `domain`。
3. 前端不传 `instance_fingerprint`。
4. 前端只需传用户自定义的 `appliance_name`。

### 4.2 云端注册接口（Appliance 后端调用）
- 方法：`POST`
- 路径：`/v3/appliance/register`（由 `sms.api.register-path` 配置）
- 鉴权：AK/SK 签名（仅后端使用）

请求字段：
1. `account_ak`
2. `account_sk`
3. `domain`
4. `instance_fingerprint`
5. `appliance_name`

响应字段：
1. `code`
2. `message`
3. `data.appliance_id`
4. `data.is_new`

---

## 5. 注册流程

### 5.1 本地流程
1. 前端调用本地注册接口。
2. 服务层读取本地配置：`account-ak/account-sk/domain`。
3. 调用 `FingerprintUtil.generate(domain)` 生成 `instance_fingerprint`。
4. 读取请求体中的 `appliance_name` 并做非空/长度校验。
5. 组装请求并调用云端注册接口。
6. 解析响应并返回 `appliance_id` 与 `is_new`。

### 5.2 幂等预期
1. 同一设备重复注册应返回同一 `appliance_id`。
2. 是否新建由云端返回 `is_new` 标识。

---

## 6. 配置设计

### 6.1 云端接口配置
位于 `application.yml`：

```yaml
sms:
  api:
    host: localhost
    port: 9000
    register-path: /api/v1/appliances/register
    connect-timeout-ms: 3000
    read-timeout-ms: 5000
    write-timeout-ms: 5000
```

### 6.2 本地云账号配置
位于 `application.yml`：

```yaml
appliance:
  cloud-account:
    account-ak: demo-ak
    account-sk: demo-sk
    domain: sms.cn-north-4.myhuaweicloud.com
```

约束：
1. `account-ak/account-sk/domain` 不能为空。
2. 缺失时返回参数错误并终止注册。

---

## 7. 关键实现说明

### 7.1 ApplianceRegistrationService
核心职责：
1. 校验本地配置有效性。
2. 生成指纹并校验 `appliance_name`。
3. 调用 `SmsApi.register(...)`。
4. 转换响应为 API 返回模型。

### 7.2 SmsApi
核心职责：
1. 初始化 `OkHttpClient`。
2. 基于 `host + port + register-path` 组装 URL。
3. 发送 HTTP 请求并解析响应。
4. 统一抛出上游异常（网络失败、非 2xx、解析失败）。

---

## 8. 异常处理策略

1. 本地配置缺失：返回 `A400`（参数错误）。
2. `appliance_name` 缺失或不合法：返回 `A400`。
3. 云端调用失败（网络/超时）：返回 `A502`。
4. 云端返回非成功状态：返回 `A503`。
5. 云端响应解析失败：返回 `A500_PARSE_ERROR`。
6. 以上异常通过全局异常处理器统一封装响应。

---

## 9. 安全要求

1. `AK/SK` 不得由前端透传。
2. `AK/SK` 不得写入明文日志。
3. `instance_fingerprint` 仅上传哈希值，不上传原始硬件标识。

---

## 10. 与当前代码文件映射

1. 控制层：`src/main/java/com/example/demo/controller/ApplianceApiController.java`
2. 服务层：`src/main/java/com/example/demo/service/ApplianceRegistrationService.java`
3. 云端调用层：`src/main/java/sdk/SmsApi.java`
4. 指纹工具：`src/main/java/com/example/demo/util/FingerprintUtil.java`
5. IP 工具：`src/main/java/com/example/demo/util/LocalIpUtil.java`
6. 配置文件：`src/main/resources/application.yml`

---

## 11. 后续优化建议

1. 将 `account-sk` 从明文配置迁移到密钥管理服务（KMS/本地加密存储）。
2. 增加注册重试策略（指数退避 + 最大次数）。
3. 将 `appliance_id` 持久化策略文档化（文件路径、权限、损坏恢复）。
4. 增加注册链路埋点（耗时、成功率、失败原因分布）。
