package com.ligg.common.entity.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFavoriteEntity {
    private Integer favoriteId;
    private Long userId;
    private Long projectId;
    private LocalDateTime createTime;
}
