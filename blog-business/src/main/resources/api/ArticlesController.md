# 文章接口文档

## 基础路径
```
/system/articles
```

---

# 一、后台管理接口

## 1. 获取文章列表

### URL
```
GET /system/articles/list
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页条数，默认10 |
| orderByColumn | String | 否 | 排序列 |
| isAsc | String | 否 | 排序方向，asc-升序，desc-降序，默认asc |
| reasonable | Boolean | 否 | 分页参数合理化，true-合理化处理，false-不进行合理化处理，默认true |
| title | String | 否 | 文章标题（模糊查询） |
| articleCategoriesId | Long | 否 | 文章分类ID |
| isPublished | Integer | 否 | 是否发布，0-否，1-是 |
| isTop | Integer | 否 | 是否置顶，0-否，1-是 |

### 响应参数
```json
{
  "code": 200,
  "msg": "查询成功",
  "total": 100,
  "rows": [
    {
      "articlesId": 1,
      "articleCategoriesId": 1,
      "categoryName": "日常",
      "title": "示例文章标题",
      "coverImage": "https://example.com/cover.jpg",
      "isPublished": 1,
      "isTop": 0
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
| rows | Array | 文章列表 |
| articlesId | Long | 文章ID |
| articleCategoriesId | Long | 文章分类ID |
| categoryName | String | 分类名称 |
| title | String | 文章标题 |
| coverImage | String | 封面图片url |
| isPublished | Integer | 是否发布，0-否，1-是 |
| isTop | Integer | 是否置顶，0-否，1-是 |

## 2. 查看文章详情

### URL
```
GET /system/articles/detail
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| articlesId | Long | 是 | 文章ID |

### 响应参数
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "articlesId": 1,
    "articleCategoriesId": 1,
    "categoryName": "日常",
    "title": "示例文章标题",
    "slug": "example-article",
    "summary": "这是文章摘要",
    "coverImage": "https://example.com/cover.jpg",
    "contentMarkdown": "# 标题\n\n文章内容",
    "contentHtml": "<h1>标题</h1><p>文章内容</p>",
    "viewCount": 100,
    "likeCount": 50,
    "commentCount": 10,
    "wordCount": 1000,
    "readingTime": 5,
    "isPublished": 1,
    "isTop": 0,
    "publishTime": "2024-01-01 12:00:00",
    "publishYear": 2024,
    "publishMonth": 1,
    "publishDay": 1,
    "publishDate": "2024-01-01",
    "articleTagRelations": [
      {
        "articleTagRelationsId": 1,
        "articleId": 1,
        "tagId": 1,
        "name": "Java",
        "slug": "java"
      }
    ]
  }
}
```

### 响应字段说明
| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 状态码，200表示成功 |
| msg | String | 响应消息 |
| data | Object | 文章详细信息对象 |
| articlesId | Long | 文章ID |
| articleCategoriesId | Long | 文章分类ID |
| categoryName | String | 分类名称 |
| title | String | 文章标题 |
| slug | String | url标识 |
| summary | String | 文章摘要 |
| coverImage | String | 封面图片url |
| contentMarkdown | String | markdown内容 |
| contentHtml | String | 转换后的html内容 |
| viewCount | Integer | 浏览次数 |
| likeCount | Integer | 点赞次数 |
| commentCount | Integer | 评论数 |
| wordCount | Integer | 字数统计 |
| readingTime | Integer | 预计阅读时间，单位：分钟 |
| isPublished | Integer | 是否发布，0-否，1-是 |
| isTop | Integer | 是否置顶，0-否，1-是 |
| publishTime | String | 发布时间 |
| publishYear | Integer | 发布年份 |
| publishMonth | Integer | 发布月份 |
| publishDay | Integer | 发布日期 |
| publishDate | String | 发布日期（去掉时间） |
| articleTagRelations | Array | 文章标签关联列表 |
| articleTagRelationsId | Long | 文章标签关系ID |
| articleId | Long | 文章ID |
| tagId | Long | 标签ID |
| name | String | 标签名称 |
| slug | String | url标识 |

## 3. 新增文章

### URL
```
POST /system/articles/add
```

### 请求参数
```json
{
  "articleCategoriesId": 1,
  "categoryName": "日常",
  "title": "示例文章标题",
  "slug": "example-article",
  "contentMarkdown": "# 标题\n\n文章内容",
  "summary": "这是文章摘要",
  "coverImage": "https://example.com/cover.jpg",
  "articleTagRelations": [
    {
      "tagId": 1
    },
    {
      "tagId": 2
    }
  ]
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| articleCategoriesId | Long | 是 | 文章分类ID |
| categoryName | String | 是 | 分类名称 |
| title | String | 是 | 文章标题 |
| slug | String | 是 | url标识 |
| contentMarkdown | String | 是 | markdown内容 |
| summary | String | 否 | 文章摘要 |
| coverImage | String | 否 | 封面图片url |
| articleTagRelations | Array | 否 | 文章标签关联列表 |
| articleTagRelations[].tagId | Long | 否 | 标签ID |

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

## 4. 修改文章

### URL
```
POST /system/articles/edit
```

### 请求参数
```json
{
  "articlesId": 1,
  "articleCategoriesId": 1,
  "categoryName": "日常",
  "title": "示例文章标题",
  "slug": "example-article",
  "contentMarkdown": "# 标题\n\n文章内容",
  "summary": "这是文章摘要",
  "coverImage": "https://example.com/cover.jpg",
  "articleTagRelations": [
    {
      "tagId": 1
    },
    {
      "tagId": 2
    }
  ]
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| articlesId | Long | 是 | 文章ID |
| articleCategoriesId | Long | 是 | 文章分类ID |
| categoryName | String | 是 | 分类名称 |
| title | String | 是 | 文章标题 |
| slug | String | 是 | url标识 |
| contentMarkdown | String | 是 | markdown内容 |
| summary | String | 否 | 文章摘要 |
| coverImage | String | 否 | 封面图片url |
| articleTagRelations | Array | 否 | 文章标签关联列表 |
| articleTagRelations[].tagId | Long | 否 | 标签ID |

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

## 5. 删除文章

### URL
```
POST /system/articles/delete/{ids}
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| ids | Long[] | 是 | 文章ID数组（路径参数） |

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

## 6. 修改文章发布状态

### URL
```
POST /system/articles/changePublishStatus
```

### 请求参数
```json
{
  "articlesId": 1,
  "isPublished": 1,
  "isTop": 0
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| articlesId | Long | 是 | 文章ID |
| isPublished | Integer | 否 | 是否发布，0-否，1-是 |
| isTop | Integer | 否 | 是否置顶，0-否，1-是 |

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
