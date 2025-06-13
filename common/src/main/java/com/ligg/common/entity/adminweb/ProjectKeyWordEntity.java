package com.ligg.common.entity.adminweb;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author Ligg
 * @Time 2025/6/13
 * <p>
 * 项目关键词
 **/
@Data
@TableName("project_keywords")
public class ProjectKeyWordEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 关键词
     */
    @Pattern(regexp = "^.{1,50}$")
    private String keyword;

    /**
     * 验证码长度
     */
    private Integer codeLength;

    /**
     * 项目id
     */
    private Integer projectId;

    /**
     * 更新时间
     */
    private LocalDateTime updateAt;
}
