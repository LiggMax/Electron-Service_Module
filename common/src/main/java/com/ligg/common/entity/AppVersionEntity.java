package com.ligg.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 应用版本实体类
 */
@Data
@TableName("app_version")
public class AppVersionEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 版本号
     */
    private String version;

    /**
     * 下载地址
     */
    private String downloadUrl;

    /**
     * 发布说明
     */
    private String releaseNotes;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件MD5
     */
    @TableField(exist = false)
    private String fileMd5;

    /**
     * 上传状态：0-上传中，1-上传完成，2-上传失败
     */
    @TableField(exist = false)
    private Integer uploadStatus;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    @TableField(exist = false)
    private LocalDateTime createTime;
}
