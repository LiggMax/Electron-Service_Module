package com.ligg.common.entity.adminweb;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("announcement")
public class AnnouncementEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @NotNull(message = "不能为空")
    @Pattern(regexp = "^.{1,30}$", message = "长度不能超过30个字符")
    private String title;
    @NotNull(message = "不能为空")
    @Pattern(regexp = "^.{5,100}$", message = "长度不能超过100个字符")
    private String content;
    private LocalDateTime  createTime;
}
