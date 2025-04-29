package com.ligg.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionEntity {
    private Integer regionId;
    private String regionName;
    private String regionMark;
}
