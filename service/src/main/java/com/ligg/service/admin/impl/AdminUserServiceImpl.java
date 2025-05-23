package com.ligg.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.AdminUserEntity;
import com.ligg.common.entity.UserOrderEntity;
import com.ligg.common.utils.BCryptUtil;
import com.ligg.common.vo.OrderVo;
import com.ligg.mapper.AdminUserMapper;
import com.ligg.mapper.user.UserOrderMapper;
import com.ligg.service.admin.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUserEntity> implements AdminUserService {

    @Autowired
    private AdminUserMapper adminUserMapper;
    @Autowired
    private UserOrderMapper userOrderMapper;

    /**
     * 重置密码
     */
    @Override
    public void resetPassword(Long userId, String password) {
        LambdaUpdateWrapper<AdminUserEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AdminUserEntity::getUserId, userId)
                .set(AdminUserEntity::getPassword, BCryptUtil.encrypt(password));
        adminUserMapper.update(null, updateWrapper);
    }

    /**
     * 添加卡商
     */
    @Override
    public void saveCardUser(AdminUserEntity adminUserEntity) {
        adminUserEntity.setCreatedAt(LocalDateTime.now());
        adminUserEntity.setPassword(BCryptUtil.encrypt(adminUserEntity.getPassword()));
        adminUserMapper.insert(adminUserEntity);
    }

    /**
     * 更新登录时间
     */
    @Override
    public void updateLoginTime(Long userId) {
        LambdaUpdateWrapper<AdminUserEntity> updateWrapper = new LambdaUpdateWrapper<AdminUserEntity>()
                .eq(AdminUserEntity::getUserId, userId)
                .set(AdminUserEntity::getLoginTime, LocalDateTime.now());
        adminUserMapper.update(null, updateWrapper);
    }

    /**
     * 获取订单
     */
    @Override
    public List<OrderVo> getOrder(Long AdminId) {
        List<UserOrderEntity> orderEntities = userOrderMapper.selectList(new LambdaUpdateWrapper<UserOrderEntity>()
                .eq(UserOrderEntity::getAdminId, AdminId));

        ArrayList<OrderVo> orderVoList = new ArrayList<>();
        for (UserOrderEntity orderEntity : orderEntities){
            OrderVo orderVo = new OrderVo();
            orderVo.setId(orderEntity.getUserProjectId());
            orderVo.setAdminId(orderEntity.getAdminId());
            orderVo.setUserId(orderEntity.getUserId());
            orderVo.setPhoneNumber(orderEntity.getPhoneNumber());
            orderVo.setState(orderEntity.getState());
            orderVo.setCreatedAt(orderEntity.getCreatedAt());

            orderVoList.add(orderVo);
        }
        return orderVoList;
    }
}
