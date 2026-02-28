# 留言板接口文档

## 基础路径
```
/system/guestbook
```

---

# 一、后台管理接口

## 1. 获取留言列表

### URL
```
GET /system/guestbook/list/isRoot
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| startTime | String | 否 | 开始时间，格式：yyyy-MM-dd HH:mm:ss |
| endTime | String | 否 | 结束时间，格式：yyyy-MM-dd HH:mm:ss |
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
  "total": 1,
  "rows": [
    {
      "guestbookId": "262",
      "nickname": "3123",
      "email": "",
      "content": "123321132",
      "avatar": "",
      "location": "内网IP",
      "rootId": "0",
      "parentId": "0",
      "isRoot": 1,
      "status": 1,
      "replyCount": 0,
      "messageTime": "2026-02-26 02:35:16"
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
| rows | Array | 留言列表 |
| guestbookId | Long | 留言ID |
| nickname | String | 昵称 |
| email | String | 邮箱 |
| content | String | 留言内容 |
| avatar | String | 头像地址 |
| location | String | IP所在地区 |
| rootId | Long | 根留言ID，0表示根留言 |
| parentId | Long | 回复留言ID，0表示直接回复根留言 |
| isRoot | Integer | 是否为根留言：0-否，1-是 |
| status | Integer | 状态：0-隐藏，1-显示，2-审核中 |
| messageTime | String | 留言时间 |
| replyCount | Integer | 回复数量（仅根留言有该字段） |

## 2. 获取子留言列表

### URL
```
GET /system/guestbook/list/child
```

### 请求参数
| 参数名 | 类型 |必填 | 说明 |
|--------|------|------|------|
| guestbookId | Long | 是 |根留言ID |
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 |每条数页条数，默认10 |
| orderByColumn | String |否 |排序列，如：create_time |
| isAsc | String | 否 |排序方向，asc-升序，desc-降序，默认asc |
| reasonable | Boolean |否 | 分页参数合理化，true-合理化处理，false-不进行合理化处理，默认true |

###响应参数
```json
{
  "code": 200,
  "msg": "查询成功",
  "rows": [
    {
      "guestbookId": 2,
      "nickname": "JianyueQ",
      "email": "3137390381@qq.com",
      "content": "12312323",
      "avatar": "https://oss.jianyue.cloud/blog-oss/2026/02/20/e026483f648640f88c820e5730fd8f9a.png",
      "location": "内网IP",
      "rootId": 1,
      "parentId": 0,
      "isRoot": 0,
      "status": 1,
      "messageTime": "2026-02-21 22:46:29"
    }
  ]
}
```

### 响应字段说明
| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | Integer |状态码，200表示成功 |
| msg | String |响应消息 |
| total | Long |总记录数 |
| rows | Array |留列表 |
| guestbookId | Long |留ID |
| nickname | String |昵称 |
| email | String | |
| content | String |留内容内容 |
| avatar | String | 头像地址 |
| location | String | IP所在地区 |
| rootId | Long |根留言ID，0表示根留言 |
| parentId | Long |回复留言ID，0表示直接回复根留言 |
| isRoot | Integer | 是否为根留言：0-否，1-是 |
| status | Integer | 状态：0-隐藏，1-显示，2-审核中 |
| messageTime | String |时间时间 |

## 3. 后台回复留言

### URL
```
POST /system/guestbook/adminReplyMessage
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| nickname | String | 是 | 昵称 |
| email | String | 是 | 邮箱 |
| content | String | 是 | 回复内容 |
| avatar | String | 否 | 头像地址 |
| rootId | Long | 是 | 根留言ID，0表示根留言 |
| parentId | Long | 是 | 回复留言ID，0表示直接回复根留言 |

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

## 4. 修改留言状态

### URL
```
POST /system/guestbook/updateStatus
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| guestbookId | Long | 是 | 留言ID |
| status | Integer | 是 | 状态：0-隐藏，1-显示，2-审核中 |

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
| msg | String |响应消息 |

## 5. 删除留言

### URL
```
POST /system/guestbook/delete/{id}
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 留言ID（路径参数） |

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
/blog/user/guestbook
```

## 1. 获取前台根留言列表

### URL
```
GET /blog/user/guestbook/list/isRoot
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
  "total": 1,
  "rows": [
    {
      "guestbookId": "262",
      "nickname": "3123",
      "email": "",
      "content": "123321132",
      "avatar": "",
      "location": "内网IP",
      "rootId": "0",
      "parentId": "0",
      "isRoot": 1,
      "replyCount": 0,
      "messageTime": "2026-02-26 02:35:16"
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
| rows | Array | 留言列表 |
| guestbookId | Long | 留言ID |
| nickname | String | 昵称 |
| email | String | 邮箱 |
| content | String | 留言内容 |
| avatar | String | 头像地址 |
| location | String | IP所在地区 |
| rootId | Long | 根留言ID，0表示根留言 |
| parentId | Long | 回复留言ID，0表示直接回复根留言 |
| isRoot | Integer | 是否为根留言：0-否，1-是 |
| messageTime | String | 留言时间 |
| replyCount | Integer | 回复数量（仅根留言有该字段） |

## 2. 获取前台子留言列表

### URL
```
GET /blog/user/guestbook/list/child
```

### 请求参数
| 参数名 | 类型 |必填 | 说明 |
|--------|------|------|------|
| guestbookId | Long | 是 |根留言ID |
| pageNum | Integer |否 页码，默认1 |
| pageSize | Integer |否每页条数，默认10 |
| orderByColumn | String |否排序列，如：create_time |
| isAsc | String |否排序方向，asc-升序，desc-降序，默认asc |
| reasonable | Boolean |否 分页参数合理化，true-合理化处理，false-不进行合理化处理，默认true |

### 响应参数
```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 1,
  "rows": [
    {
      "guestbookId": 2,
      "nickname": "JianyueQ",
      "email": "3137390381@qq.com",
      "content": "12312323",
      "avatar": "https://oss.jianyue.cloud/blog-oss/2026/02/20/e026483f648640f88c820e5730fd8f9a.png",
      "location": "内网IP",
      "rootId": 1,
      "parentId": 0,
      "isRoot": 0,
      "messageTime": "2026-02-21 22:46:29"
    }
  ]
}
```

### 响应字段说明
| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 状态码，200表示成功 |
| msg | String |响应消息 |
| total | Long |总记录数 |
| rows | Array |留列表 |
| guestbookId | Long |留IDID |
| nickname | String | 昵称 |
| email | String | |
| content | String |留内容 |
| avatar | String |头像地址 |
| location | String | IP所在地区 |
| rootId | Long |根留言ID，0表示根留言 |
| parentId | Long |回复留言ID，0表示直接回复根留言 |
| isRoot | Integer | 是否为根留言：0-否，1-是 |
| messageTime | String |留时间 |

## 3. 前台留言或回复

### URL
```
POST /blog/user/guestbook/add
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| nickname | String | 是 | 昵称 |
| email | String | 是 | 邮箱 |
| content | String | 是 | 留言内容 |
| avatar | String | 否 | 头像地址 |
| rootId | Long | 否 | 根留言ID，0表示根留言 |
| parentId | Long | 否 | 回复留言ID，0表示直接回复根留言 |

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
