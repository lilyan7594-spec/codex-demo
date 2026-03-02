# Appliance 设备管理设计文档

## 1. 设计目标
1. Appliance 前端只负责触发注册，不传 AK/SK、domain、fingerprint。
2. Appliance 后端内部获取云账号配置并生成设备指纹。
3. 云端基于设备指纹实现幂等注册与设备身份延续。

---

## 2. 架构边界

### 2.1 前端 -> Appliance 后端（管理面）
- 接口：`POST /api/v1/appliances/register`
- 请求体：无
- 职责：触发本机注册流程

### 2.2 Appliance 后端 -> 云服务（控制面）
- 接口：`POST /v3/appliance/register`
- 鉴权：AK/SK 签名（仅后端持有）
- 请求参数：`hw_fingerprint`、`appliance_name`、`hostname`、`ip`
- 职责：向云端申请/恢复 `appliance_id`

---

## 3. 数据库表设计（云端）

建议将“物理唯一”约束调整为“账号内唯一”，避免跨账号冲突。

表：`appliance_info`

| 字段名 | 类型 | 描述 | 约束 |
| --- | --- | --- | --- |
| `appliance_id` | VARCHAR(64) | 云端生成的业务唯一 ID | Primary Key |
| `hw_fingerprint` | VARCHAR(128) | 设备指纹 | Not Null |
| `owner_user_id` | VARCHAR(64) | 当前归属账号 ID | Not Null, Index |
| `appliance_name` | VARCHAR(128) | 设备显示名称 | - |
| `status` | TINYINT | 0:离线,1:在线,2:异常,3:禁用 | Not Null |
| `last_heartbeat` | TIMESTAMP | 最近心跳时间 | - |
| `created_at` | TIMESTAMP | 首次注册时间 | Not Null |
| `updated_at` | TIMESTAMP | 最近更新时间 | Not Null |

索引建议：
1. `uk_owner_fingerprint(owner_user_id, hw_fingerprint)`
2. `idx_owner_status(owner_user_id, status)`

---

## 4. 接口设计

### 4.1 Appliance 本地触发注册接口

- Endpoint：`POST /api/v1/appliances/register`
- 请求体：无

响应示例：

```json
{
  "code": "0",
  "message": "success",
  "data": {
    "appliance_id": "app-v-66238890",
    "is_new": true
  }
}
```

### 4.2 云端注册接口（Appliance 后端调用）

- Endpoint：`POST /v3/appliance/register`
- 鉴权：Header AK/SK 签名

请求示例：

```json
{
  "hw_fingerprint": "8da8f8a1b2c34d5eaf6g7890abcdef12",
  "appliance_name": "北京机房-Appliance1",
}
```

响应示例：

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

错误码建议：
1. HTTP 状态码用于协议层（200/400/401/403/429/500）。
2. `code` 字段用于业务码（如 `A401`、`A409`、`A429`）。

---

## 5. 注册流程

### 5.1 Appliance 本地流程
1. 启动后检查本地 `appliance_id`。
2. 若已有且有效，进入心跳/上报流程。
3. 若不存在，前端调用 `POST /api/v1/appliances/register` 触发注册。
4. 后端从本地配置/数据库读取 `account_ak/account_sk/domain`。
5. 后端生成 `hw_fingerprint`，持久化到本地配置文件。
6. 后端调用云端注册接口。
7. 接收 `appliance_id` 并持久化到本地配置文件。

### 5.2 云端处理流程
1. 校验 AK/SK，解析 `owner_user_id`。
2. 以 `(owner_user_id, hw_fingerprint)` 查询设备。
3. 不存在：创建新记录并生成 `appliance_id`。
4. 存在：更新设备名称/状态，返回既有 `appliance_id`。

---

## 6. 异常场景与策略

| 场景 | 处理策略 |
| --- | --- |
| 注册时断网 | 指数退避重试，超限后返回可重试错误 |
| 本地身份文件丢失 | 重新注册；若指纹一致则返回原 `appliance_id` |
| 账号切换 | 清理本地身份后重新注册，云端按新账号归属写入 |
| 指纹冲突 | 返回 `A409`，进入人工处理流程 |

---

## 7. 硬件指纹设计原则
1. 多因子采集：UUID、CPU、系统盘序列号、MAC。
2. 数据清洗：统一小写、去空白、过滤占位值。
3. 稳定序列化后做 SHA-256。
4. 指纹仅用于身份识别，不回传原始硬件数据。

---

## 8. 身份生命周期管理
1. 首次注册后本地保存 `appliance_id + hw_fingerprint`。
2. 重启时优先复用本地 `appliance_id`。
3. 定期心跳维护在线状态。
4. 本地身份损坏时支持自动恢复（依赖稳定指纹）。

---

## 9. 云端校验并分配 `appliance_id` 流程

### 9.1 输入与鉴权
1. 云端接收注册请求：`hw_fingerprint`、`appliance_name`、`hostname`、`ip`。
2. 基于 Header AK/SK 签名校验请求合法性，解析出 `owner_user_id`。
3. 校验防重放字段（如 timestamp/nonce/signature）。

### 9.2 参数校验
1. `hw_fingerprint` 必填，建议限制为 64 位十六进制字符串。
2. `appliance_name` 做长度与字符合法性校验。
3. `hostname`、`ip` 做基础格式校验。

### 9.3 幂等处理（事务内）
1. 以 `(owner_user_id, hw_fingerprint)` 查询 `appliance_info`。
2. 若不存在：进入新建设备分支，生成 `appliance_id` 并插入记录。
3. 若存在：复用既有 `appliance_id`，更新 `appliance_name/hostname/ip/updated_at`。
4. 依赖唯一索引 `uk_owner_fingerprint(owner_user_id, hw_fingerprint)` 兜底并发。

### 9.4 返回结果
1. 成功返回：`code=0`、`appliance_id`、`is_new`。
2. 失败返回：HTTP 状态码 + 业务码（如 `A401`、`A409`、`A500`）。

---

## 10. `appliance_id` 生成方案

### 10.1 设计目标
1. 全局唯一：不同设备、不同时间生成 ID 不冲突。
2. 稳定复用：同一设备重复注册不重复分配 ID。
3. 可观测：便于日志检索与问题排查。

### 10.2 推荐格式
建议使用固定前缀 + 时间有序唯一 ID：

```text
app-<ULID>
```

示例：

```text
app-01J5ZQ4TY9M8YQGQ8K7G6R9V5W
```

说明：
1. `app-` 作为资源类型前缀。
2. ULID 天然按时间近似有序，便于排序与运维排查。
3. 如果已有雪花 ID 组件，也可使用 `app-<snowflakeId>`。

### 10.3 生成时机
1. 仅在“新建设备分支”生成一次。
2. 后续同 `(owner_user_id, hw_fingerprint)` 的注册请求，始终返回原 `appliance_id`。

### 10.4 并发一致性
1. 采用“查库 + 唯一索引兜底”的方式避免重复创建。
2. 发生唯一键冲突时，回查并返回已存在记录，不直接返回失败。

### 10.5 禁止项
1. 禁止直接用 `hw_fingerprint` 作为 `appliance_id`。
2. 禁止账号切换时重建 `appliance_id`（应按策略复用设备身份）。

