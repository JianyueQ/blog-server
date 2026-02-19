# 访客记录接口文档

## 基础路径
```
/system/visitor/record
```

## 1. 获取访客记录列表

### URL
```
GET /system/visitor/record/list
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| ipaddr | String | 否 | IP地址，支持模糊查询 |
| location | String | 否 | 地区位置，支持模糊查询 |
| startTime | String | 否 | 开始时间，格式：yyyy-MM-dd HH:mm:ss |
| endTime | String | 否 | 结束时间，格式：yyyy-MM-dd HH:mm:ss |
| blacklist | String | 是 | 是否为黑名单，Y:是 N:否 |
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
      "visitorRecordId": 1,
      "ipaddr": "192.168.1.1",
      "location": "中国-北京",
      "visitTime": "2024-01-01 12:00:00"
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
| rows | Array | 访客记录列表 |
| visitorRecordId | Long | 访客记录ID |
| ipaddr | String | IP地址 |
| location | String | 地区位置 |
| visitTime | String | 访问时间 |

## 2. 获取访客记录详情

### URL
```
GET /system/visitor/record/detail
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| visitorRecordId | String | 是 | 访客记录ID |

### 响应参数
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "visitorRecordId": 1,
    "ipaddr": "192.168.1.1",
    "location": "中国-北京",
    "browser": "Chrome",
    "os": "Windows 10",
    "visitTime": "2024-01-01 12:00:00",
    "userAgent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
    "abnormalFlag": 0,
    "abnormalType": null,
    "blacklist": "N",
    "reason": null
  }
}
```

### 响应字段说明
| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 状态码，200表示成功 |
| msg | String | 响应消息 |
| data | Object | 详细信息对象 |
| visitorRecordId | Long | 访客记录ID |
| ipaddr | String | IP地址 |
| location | String | 地区位置 |
| browser | String | 浏览器类型 |
| os | String | 操作系统 |
| visitTime | String | 访问时间 |
| userAgent | String | 用户代理字符串 |
| abnormalFlag | Integer | 是否异常访问，0:正常 1:异常 |
| abnormalType | String | 异常类型 |
| blacklist | String | 是否黑名单，Y:是 N:否 |
| reason | String | 加入黑名单原因 |

## 3. 更新访客记录黑名单状态

### URL
```
POST /system/visitor/record/update/blacklist
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| visitorRecordId | String | 是 | 访客记录ID |
| isBlacklist | Boolean | 是 | 是否加入黑名单，true:加入黑名单 false:从黑名单移除 |

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

## 4. 清空访客记录

### URL
```
POST /system/visitor/record/clean
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