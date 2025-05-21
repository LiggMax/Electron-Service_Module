package com.ligg.service.common.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.AnnouncementEntity;
import com.ligg.mapper.AnnouncementMapper;
import com.ligg.service.common.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, AnnouncementEntity> implements AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    /**
     * 获取用户公告
     */
    @Override
    public List<AnnouncementEntity> UserGetAnnouncement() {
        //根据创建时间倒序，通过stream流获取最新一条公告，如果没有则返回null
        return announcementMapper.selectList(new LambdaQueryWrapper<AnnouncementEntity>()
                .orderByDesc(AnnouncementEntity::getCreateTime));
    }
}
