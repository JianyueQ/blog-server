# 文章标签接口文档

## 基础路径
```
/system/articleTags
```

---

# 一、后台管理接口

## 1. 获取文章标签列表

### URL
```
GET /system/articleTags/list
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页条数，默认10 |
| orderByColumn | String | 否 | 排序列 |
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
      "articleTagsId": 1,
      "name": "Java",
      "slug": "java",
      "usageFrequency": 10
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
| rows | Array | 标签列表 |
| articleTagsId | Integer | 标签ID |
| name | String | 标签名称 |
| slug | String | URL标识 |
| usageFrequency | Integer | 使用频次 |

## 2. 获取文章标签详情

### URL
```
GET /system/articleTags/detail
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| articleTagsId | Long | 是 | 标签ID |

### 响应参数
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "articleTagsId": 1,
    "name": "Java",
    "slug": "java"
  }
}
```

### 响应字段说明
| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 状态码，200表示成功 |
| msg | String | 响应消息 |
| data | Object | 详细信息对象 |
| articleTagsId | Integer | 标签ID |
| name | String | 标签名称 |
| slug | String | URL标识 |

## 3. 添加文章标签

### URL
```
POST /system/articleTags/add
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | String | 是 | 标签名称 |
| slug | String | 是 | URL标识 |

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

## 4. 修改文章标签

### URL
```
POST /system/articleTags/edit
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| articleTagsId | Integer | 是 | 标签ID |
| name | String | 否 | 标签名称 |
| slug | String | 否 | URL标识 |

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

## 5. 删除文章标签

### URL
```
POST /system/articleTags/delete/{ids}
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| ids | Long[] | 是 | 标签ID数组（路径参数） |

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

## 6. 文章添加关联的标签

### URL
```
POST /system/articleTags/addArticleTags
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| articleId | Long | 是 | 文章ID |
| tagId | Long | 是 | 标签ID |

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

## 7. 文章删除关联的标签

### URL
```
POST /system/articleTags/deleteArticleTags
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| articleId | Long | 是 | 文章ID |
| tagId | Long | 是 | 标签ID |

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
