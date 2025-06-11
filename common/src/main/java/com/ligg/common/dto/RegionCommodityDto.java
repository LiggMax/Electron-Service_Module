package com.ligg.common.dto;

import lombok.Data;

@Data
public class RegionCommodityDto {
    private Integer regionId;
    private String regionName;
    private String regionMark;
    private String icon;
    private Integer phoneCount;
    private Double projectPrice;

}
