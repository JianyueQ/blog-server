# 消息记录接口文档

## 基础路径
```
/system/messageRecord
```

---

# 一、后台管理接口

## 1. 获取消息列表

### URL
```
GET /system/messageRecord/list
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页条数，默认10 |
| orderByColumn | String | 否 | 排序列，如：create_time |
| isAsc | String | 否 | 排序方向，asc-升序，desc-降序，默认asc |
| reasonable | Boolean | 否 | 分页参数合理化，true-合理化处理，false-不进行合理化处理，默认true |

### 响应参数
```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 100,
  "rows": [
    {
      "messageId": 1,
      "messageTitle": "系统通知",
      "messageContent": "这是一条系统通知消息",
      "messageType": "SYSTEM",
      "isRead": 0,
      "readTime": null,
      "createTime": "2024-01-01 12:00:00"
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
| rows | Array | 消息列表 |
| messageId | Long | 消息ID |
| messageTitle | String | 消息标题 |
| messageContent | String | 消息内容 |
| messageType | String | 消息类型 |
| isRead | Integer | 阅读状态：0-未读，1-已读 |
| readTime | String | 阅读时间 |
| createTime | String | 创建时间 |

## 2. 修改消息阅读状态

### URL
```
POST /system/messageRecord/updateMessageReadStatus
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| messageId | Long | 是 | 消息ID |
| isRead | Integer | 是 | 阅读状态：0-未读，1-已读 |

### 响应参数
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "messageId": 1,
    "isRead": 1,
    "readTime": "2024-01-01 12:00:00"
  }
}
```

### 响应字段说明
| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 状态码，200表示成功 |
| msg | String | 响应消息 |
| data | Object | 详细信息对象 |
| messageId | Long | 消息ID |
| isRead | Integer | 阅读状态 |
| readTime | String | 阅读时间 |

## 3. 获取未读消息列表

### URL
```
GET /system/messageRecord/listUnread
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页条数，默认10 |
| orderByColumn | String | 否 | 排序列，如：create_time |
| isAsc | String | 否 | 排序方向，asc-升序，desc-降序，默认asc |
| reasonable | Boolean | 否 | 分页参数合理化，true-合理化处理，false-不进行合理化处理，默认true |

### 响应参数
```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 50,
  "rows": [
    {
      "messageId": 2,
      "messageTitle": "新留言提醒",
      "messageContent": "您有新的留言需要处理",
      "messageType": "COMMENT",
      "isRead": 0,
      "readTime": null,
      "createTime": "2024-01-01 11:30:00"
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
| rows | Array | 消息列表 |
| messageId | Long | 消息ID |
| messageTitle | String | 消息标题 |
| messageContent | String | 消息内容 |
| messageType | String | 消息类型 |
| isRead | Integer | 阅读状态：0-未读，1-已读 |
| readTime | String | 阅读时间 |
| createTime | String | 创建时间 |

## 4. 获取已读消息列表

### URL
```
GET /system/messageRecord/listRead
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页条数，默认10 |
| orderByColumn | String | 否 | 排序列，如：create_time |
| isAsc | String | 否 | 排序方向，asc-升序，desc-降序，默认asc |
| reasonable | Boolean | 否 | 分页参数合理化，true-合理化处理，false-不进行合理化处理，默认true |

### 响应参数
```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 50,
  "rows": [
    {
      "messageId": 3,
      "messageTitle": "系统维护通知",
      "messageContent": "系统将于今晚进行维护",
      "messageType": "SYSTEM",
      "isRead": 1,
      "readTime": "2024-01-01 10:00:00",
      "createTime": "2024-01-01 09:00:00"
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
| rows | Array | 消息列表 |
| messageId | Long | 消息ID |
| messageTitle | String | 消息标题 |
| messageContent | String | 消息内容 |
| messageType | String | 消息类型 |
| isRead | Integer | 阅读状态：0-未读，1-已读 |
| readTime | String | 阅读时间 |
| createTime | String | 创建时间 |

## 5. 删除消息

### URL
```
POST /system/messageRecord/delete/{ids}
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| ids | String | 是 | 消息ID数组，多个ID用逗号分隔（路径参数） |

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

## 6. 清空消息

### URL
```
POST /system/messageRecord/clean
```

### 请求参数
无

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

## 7. 全部已读

### URL
```
POST /system/messageRecord/allRead
```

### 请求参数
无

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

# 二、WebSocket接口

## WebSocket连接地址
```
ws://localhost:8998/ws/system/messageRecord
```

## 连接参数
- 需要携带认证信息（通过握手拦截器处理）

## WebSocket消息类型

### 1. 新消息通知（服务器推送）

**消息描述**: 当有新消息时，服务器主动推送消息给在线管理员

**推送参数**:
```json
{
  "messageId": 1,
  "messageTitle": "新消息标题",
  "messageContent": "消息内容",
  "messageType": "SYSTEM",
  "isRead": 0,
  "createTime": "2024-01-01 12:00:00"
}
```

**推送字段说明**:
| 字段名 | 类型 | 说明 |
|--------|------|------|
| messageId | Long | 消息ID |
| messageTitle | String | 消息标题 |
| messageContent | String | 消息内容 |
| messageType | String | 消息类型 |
| isRead | Integer | 阅读状态 |
| createTime | String | 创建时间 |

**消息类型**:前端可以根据消息类型进行对应的路由跳转 ,例如:GUESTBOOK、SYSTEM、FRIEND_LINK等,GUESTBOOK跳转到留言板页面,SYSTEM跳转到系统通知页面,FRIEND_LINK跳转到友链页面

### 2. 消息状态更新（客户端请求）

**消息描述**: 客户端发送消息来更新消息阅读状态

**请求参数**:
```json
{
  "messageId": 1,
  "isRead": 1
}
```

**请求字段说明**:
| 字段名 | 类型 | 说明 |
|--------|------|------|
| messageId | Long | 消息ID |
| isRead | Integer | 阅读状态：0-未读，1-已读 |

**响应参数**:
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "messageId": 1,
    "isRead": 1,
    "readTime": "2024-01-01 12:00:00"
  }
}
```

**响应字段说明**:
| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 状态码，200表示成功 |
| msg | String | 响应消息 |
| data | Object | 详细信息对象 |
| messageId | Long | 消息ID |
| isRead | Integer | 阅读状态 |
| readTime | String | 阅读时间 |

### 3. 认证过期通知

**消息描述**: 当用户认证过期时，服务器推送认证过期通知

**推送参数**:
```json
{
  "type": "error",
  "message": "认证已过期"
}
```

**推送字段说明**:
| 字段名 | 类型 | 说明 |
|--------|------|------|
| type | String | 消息类型 |
| message | String | 错误消息 |

---

# 三、数据字典

## messageType 消息类型
| 值 | 说明 |
|----|------|
| SYSTEM | 系统消息 |
| COMMENT | 评论/留言相关 |
| FRIEND_LINK | 友链相关 |
| OTHER | 其他类型 |

## isRead 阅读状态
| 值 | 说明 |
|----|------|
| 0 | 未读 |
| 1 | 已读 |

## 错误码说明
| 状态码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 500 | 服务器内部错误 |
| 401 | 未认证或认证过期 |