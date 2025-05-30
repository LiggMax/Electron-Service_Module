package com.ligg.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("app_version")
public class AppVersionEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 版本号
     */
    private String version;

    /**
     * 描述
     */
    private String releaseNotes;

    /**
     * 下载地址
     */
    private String downloadUrl;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
