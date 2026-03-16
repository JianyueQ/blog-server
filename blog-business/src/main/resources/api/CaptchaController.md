# 验证码接口文档

## 基础路径

```
/captchaImage
```

---

## 1. 获取验证码图片

### URL

```
GET /captchaImage
```

### 请求参数

无

### 响应参数

```json
{
  "code": 200,
  "msg": "操作成功",
  "captchaEnabled": true,
  "uuid": "abc123-def456",
  "img": "data:image/jpeg;base64,/9j/4AAQSkZJRg..."
}
```

### 响应字段说明

| 字段名             | 类型      | 说明                    |
|-----------------|---------|-----------------------|
| code            | Integer | 状态码，200表示成功           |
| msg             | String  | 响应消息                  |
| captchaEnabled  | Boolean | 验证码是否启用，true-启用，false-不启用 |
| uuid            | String  | 验证码唯一标识，用于登录时验证      |
| img             | String  | 验证码图片的base64编码         |

### 说明

- 当 `captchaEnabled` 为 `false` 时，不会返回 `uuid` 和 `img` 字段
- 验证码类型支持两种：
  - `math`：数学验证码（如：1+2=?）
  - `char`：字符验证码（如：abcd）
- 验证码有效期为2分钟
