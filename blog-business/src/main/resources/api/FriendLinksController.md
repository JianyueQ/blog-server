# 友链接口文档

## 基础路径
```
/system/friendLinks
```

---

# 一、后台管理接口

## 1. 获取友链列表

### URL
```
GET /system/friendLinks/list
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页条数，默认10 |

### 响应参数
```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 100,
  "rows": [
    {
      "friendLinksId": 1,
      "name": "示例站点",
      "url": "https://example.com",
      "logo": "https://example.com/logo.png",
      "description": "这是一个示例站点",
      "email": "admin@example.com",
      "status": 1,
      "joinTime": "2024-01-01 12:00:00"
    }
  ]
}
```

### 响应字段说明
| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 状态码，200表示成功 |
| msg | String | 响应消息 |
| total | Long | 总记录数 |
| rows | Array | 友链列表 |
| friendLinksId | Long | 站点ID |
| name | String | 站点名称 |
| url | String | 站点地址 |
| logo | String | 站点logo |
| description | String | 站点描述 |
| email | String | 联系邮箱 |
| status | Integer | 状态(0-隐藏, 1-显示, 2-待同意) |
| joinTime | String | 加入时间 |

## 2. 获取友链详情

### URL
```
GET /system/friendLinks/detail
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| friendLinksId | Long | 是 | 友链ID |

### 响应参数
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "friendLinksId": 1,
    "name": "示例站点",
    "url": "https://example.com",
    "logo": "https://example.com/logo.png",
    "description": "这是一个示例站点",
    "email": "admin@example.com"
  }
}
```

### 响应字段说明
| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 状态码，200表示成功 |
| msg | String | 响应消息 |
| data | Object | 详细信息对象 |
| friendLinksId | Long | 站点ID |
| name | String | 站点名称 |
| url | String | 站点地址 |
| logo | String | 站点logo |
| description | String | 站点描述 |
| email | String | 联系邮箱 |

## 3. 获取通过前台申请的友链列表

### URL
```
GET /system/friendLinks/approvedList
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页条数，默认10 |

### 响应参数
```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 100,
  "rows": [
    {
      "friendLinksId": 1,
      "name": "示例站点",
      "url": "https://example.com",
      "logo": "https://example.com/logo.png",
      "description": "这是一个示例站点",
      "email": "admin@example.com",
      "status": 2,
      "joinTime": "2024-01-01 12:00:00"
    }
  ]
}
```

### 响应字段说明
| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 状态码，200表示成功 |
| msg | String | 响应消息 |
| total | Long | 总记录数 |
| rows | Array | 友链列表 |
| friendLinksId | Long | 站点ID |
| name | String | 站点名称 |
| url | String | 站点地址 |
| logo | String | 站点logo |
| description | String | 站点描述 |
| email | String | 联系邮箱 |
| status | Integer | 状态(0-隐藏, 1-显示, 2-待同意) |
| joinTime | String | 加入时间 |

## 4. 新增友链

### URL
```
POST /system/friendLinks/add
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | String | 是 | 站点名称 |
| url | String | 是 | 站点地址 |
| logo | String | 否 | 站点logo |
| description | String | 否 | 站点描述 |
| email | String | 否 | 联系邮箱 |

### 响应参数
```json
{
  "code": 200,
  "msg": "操作成功"
}
```

### 响应字段说明
| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 状态码，200表示成功 |
| msg | String | 响应消息 |

## 5. 修改友链

### URL
```
POST /system/friendLinks/update
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| friendLinksId | Long | 是 | 友链ID |
| name | String | 是 | 站点名称 |
| url | String | 是 | 站点地址 |
| logo | String | 否 | 站点logo |
| description | String | 否 | 站点描述 |
| email | String | 否 | 联系邮箱 |

### 响应参数
```json
{
  "code": 200,
  "msg": "操作成功"
}
```

### 响应字段说明
| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 状态码，200表示成功 |
| msg | String | 响应消息 |

## 6. 修改友链展示状态

### URL
```
POST /system/friendLinks/status
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| friendLinksId | Long | 是 | 友链ID |
| status | Integer | 是 | 状态(0-隐藏, 1-显示) |

### 响应参数
```json
{
  "code": 200,
  "msg": "操作成功"
}
```

### 响应字段说明
| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 状态码，200表示成功 |
| msg | String | 响应消息 |

## 7. 删除友链

### URL
```
POST /system/friendLinks/delete
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| friendLinksId | Long | 是 | 友链ID |

### 响应参数
```json
{
  "code": 200,
  "msg": "操作成功"
}
```

### 响应字段说明
| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 状态码，200表示成功 |
| msg | String | 响应消息 |

---

# 二、前台接口

## 基础路径
```
/blog/user/friendLinks
```

## 1. 获取前台展示友链列表

### URL
```
GET /blog/user/friendLinks/getDisplayedFriendLinks
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页条数，默认10 |

### 响应参数
```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 100,
  "rows": [
    {
      "name": "示例站点",
      "url": "https://example.com",
      "logo": "https://example.com/logo.png",
      "description": "这是一个示例站点",
      "email": "admin@example.com",
      "joinTime": "2024-01-01 12:00:00"
    }
  ]
}
```

### 响应字段说明
| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 状态码，200表示成功 |
| msg | String | 响应消息 |
| total | Long | 总记录数 |
| rows | Array | 友链列表 |
| name | String | 站点名称 |
| url | String | 站点地址 |
| logo | String | 站点logo |
| description | String | 站点描述 |
| email | String | 联系邮箱 |
| joinTime | String | 加入时间 |

## 2. 前台申请添加友链

### URL
```
POST /blog/user/friendLinks/requestToAdd
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | String | 是 | 站点名称 |
| url | String | 是 | 站点地址 |
| logo | String | 否 | 站点logo |
| description | String | 否 | 站点描述 |
| email | String | 否 | 联系邮箱 |

### 响应参数
```json
{
  "code": 200,
  "msg": "操作成功"
}
```

### 响应字段说明
| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 状态码，200表示成功 |
| msg | String | 响应消息 |
