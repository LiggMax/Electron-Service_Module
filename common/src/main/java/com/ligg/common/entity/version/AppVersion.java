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

    /**
     * 版本号
     */
    private String version;

    /**
     * 更新内容信息
     */
    private String releaseNotes;

    /**
     * 下载地址
     */
    private String downloadUrl;

    /**
     * 0：客户端  1：卡商端
     */
    private Integer app;

    /**
     * 上传时间
     */
    private LocalDateTime uploadTime;

    /**
     * 文件大小
     */
    private Long fileSize;
}
