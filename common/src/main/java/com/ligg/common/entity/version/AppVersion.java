package com.ligg.common.entity.version;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("app_version")
public class AppVersion {
    @TableId(type = IdType.AUTO)
    private String id;
    private String version; //版本号
    private String releaseNotes; // 更新内容信息
    private String downloadUrl; // 下载地址
    private LocalDateTime uploadTime;
    private Long fileSize;
}
