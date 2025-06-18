package com.ligg.common.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author Ligg
 * @Time 2025/6/17
 **/
@Data
public class PageVo<T> {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总前页数
     */
    private Long pages;

    /**
     * 当前页数据
     */
    private List<T> list;
}
