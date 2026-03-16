# 登录接口文档

## 基础路径

```
/login
```

---

## 1. 登录方法

### URL

```
POST /login
```

### 请求参数

```json
{
  "username": "admin",
  "password": "123456",
  "code": "1234",
  "uuid": "abc123",
  "userType": "admin"
}
```

| 参数名      | 类型     | 必填 | 说明       |
|----------|--------|----|----------|
| username | String | 是  | 用户名      |
| password | String | 是  | 密码       |
| code     | String | 否  | 验证码      |
| uuid     | String | 否  | 验证码唯一标识 |
| userType | String | 是  | 用户类型     |

### 响应参数

```json
{
  "code": 200,
  "msg": "操作成功",
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### 响应字段说明

| 字段名   | 类型      | 说明           |
|-------|---------|--------------|
| code  | Integer | 状态码，200表示成功  |
| msg   | String  | 响应消息         |
| token | String  | 登录token，后续请求需要携带 |

---

## 2. 获取管理用户信息

### URL

```
GET /getAdminInfo
```

### 请求参数

无（需要在请求头中携带 token）

### 响应参数

```json
{
  "code": 200,
  "msg": "操作成功",
  "user": {
    "adminId": 1,
    "username": "admin",
    "nickname": "管理员",
    "email": "admin@example.com",
    "avatar": "https://example.com/avatar.jpg",
    "status": 1,
    "createTime": "2024-01-01 12:00:00",
    "updateTime": "2024-01-01 12:00:00"
  }
}
```

### 响应字段说明

| 字段名         | 类型      | 说明           |
|-------------|---------|--------------|
| code        | Integer | 状态码，200表示成功  |
| msg         | String  | 响应消息         |
| user        | Object  | 管理员信息对象       |
| adminId     | Long    | 管理员ID         |
| username    | String  | 用户名          |
| nickname    | String  | 昵称           |
| email       | String  | 邮箱           |
| avatar      | String  | 头像URL        |
| status      | Integer | 状态，1-正常，0-禁用 |
| createTime  | String  | 创建时间         |
| updateTime  | String  | 更新时间         |
