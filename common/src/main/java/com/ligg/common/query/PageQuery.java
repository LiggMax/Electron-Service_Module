package com.ligg.common.query;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author Ligg
 * @Time 2025/6/17
 **/
@Data
public class PageQuery {
    /**
     * 页码
     */
    @NotNull
    @Max(value = 100, message = "页码不能大于100")
    private Long pageNum;

    /**
     * 每页数量
     */
    @NotNull
    @Max(value = 100, message = "每页数量不能大于100")
    @Min(value = 1, message = "每页数量不能小于1")
    private Long pageSize;
}
