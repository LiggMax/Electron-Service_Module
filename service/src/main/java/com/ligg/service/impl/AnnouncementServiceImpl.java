package com.ligg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.AnnouncementEntity;
import com.ligg.mapper.AnnouncementMapper;
import com.ligg.service.AnnouncementService;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, AnnouncementEntity> implements AnnouncementService {
}
