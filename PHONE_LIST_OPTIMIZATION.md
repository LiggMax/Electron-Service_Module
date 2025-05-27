# 获取号码列表接口优化说明

## 优化目标
将获取号码列表接口的返回数据中的 `projectName` 字段从字符串格式改为JSON多层嵌套格式，以提供更丰富的项目信息。

## 修改内容

### 1. 新增ProjectInfoVo类
- 文件：`common/src/main/java/com/ligg/common/vo/ProjectInfoVo.java`
- 用途：表示项目的详细信息
- 字段：
  - `projectId`: 项目ID
  - `projectName`: 项目名称  
  - `projectPrice`: 项目价格

### 2. 修改PhoneVo类
- 文件：`common/src/main/java/com/ligg/common/vo/PhoneVo.java`
- 变更：将 `projectName` 字段改为 `projects` 字段
- 类型：从 `String` 改为 `List<ProjectInfoVo>`

### 3. 更新数据库查询
- 文件：`mapper/src/main/resources/mapper/PhoneNumberMapper.xml`
- 变更：
  - 新增 `PhoneVoResultMap` 结果映射
  - 使用 `<collection>` 标签映射项目列表
  - 移除 `GROUP_CONCAT` 聚合，改为关联查询

## 接口返回格式对比

### 优化前
```json
{
    "code": 200,
    "message": "操作成功", 
    "data": [
        {
            "phoneId": "54",
            "phoneNumber": 11273746478,
            "projectName": "123,测试,测试项目,测试项目1,测试项目3",
            "regionName": "上海",
            "adminNickName": "测试卡商",
            "money": 0.2
        }
    ]
}
```

### 优化后
```json
{
    "code": 200,
    "message": "操作成功",
    "data": [
        {
            "phoneId": "54", 
            "phoneNumber": 11273746478,
            "projects": [
                {
                    "projectId": "1",
                    "projectName": "123",
                    "projectPrice": 0.5
                },
                {
                    "projectId": "2",
                    "projectName": "测试", 
                    "projectPrice": 1.0
                },
                {
                    "projectId": "3",
                    "projectName": "测试项目",
                    "projectPrice": 1.5
                }
            ],
            "regionName": "上海",
            "adminNickName": "测试卡商", 
            "money": 0.2
        }
    ]
}
```

## 优势

1. **结构化数据**：项目信息以对象数组形式返回，便于前端处理
2. **丰富信息**：包含项目ID和价格等详细信息
3. **易于扩展**：后续可以轻松添加更多项目字段
4. **类型安全**：前端可以获得更好的类型提示和验证

## 测试

使用 `test-phone-list.http` 文件测试接口：

```http
GET http://localhost:8080/api/phone
Content-Type: application/json
```

## 注意事项

- 此修改会影响前端代码，需要相应更新前端的数据处理逻辑
- 数据库查询性能可能会有轻微影响，因为移除了GROUP_CONCAT聚合
- 建议在生产环境部署前进行充分测试 