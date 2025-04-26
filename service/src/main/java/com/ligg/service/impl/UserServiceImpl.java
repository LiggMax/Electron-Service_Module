package com.ligg.service.impl;

import com.ligg.common.entity.AdminUserEntity;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.common.entity.UserEntity;
import com.ligg.common.entity.UserFavoriteEntity;
import com.ligg.common.utils.JWTUtil;
import com.ligg.common.vo.UserDataVo;
import com.ligg.mapper.PhoneNumberMapper;
import com.ligg.mapper.UserMapper;
import com.ligg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PhoneNumberMapper phoneNumberMapper;

    /**
     * 根据账号和密码查询管理员用户信息
     */
    @Override
    public AdminUserEntity findByAdminUser(String account, String password) {
        return userMapper.findByAdminUser(account, password);
    }

    /**
     * 生成token
     */
    @Override
    public String createToken(Long userId, String account) {

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("account", account);
        String token = jwtUtil.createToken(claims);
        redisTemplate.opsForValue()
                .set("Token:" + userId, token,
                        6, TimeUnit.HOURS);
        return token;
    }

    /**
     * 清除token
     */
    @Override
    public void clearToken(String userId) {
        redisTemplate.delete("Token:" + userId);
    }

    /**
     * 根据id获取用户信息
     */
    @Override
    public AdminUserEntity findByAdminUserInfo(Long userId) {

        return userMapper.findByAdminUserInfo(userId);
    }

    /**
     * 根据账号和密码查询用户信息
     */
    @Override
    public UserEntity findByUser(String account, String password) {
        UserEntity user = userMapper.findByUser(account);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    /**
     * 根据id查询用户信息
     */
    @Override
    public UserEntity findByUserInfo(Long userId) {
        return userMapper.findByUserInfo(userId);
    }

    /**
     * 更新用户信息
     */
    @Override
    public String updateUserInfo(UserDataVo userDataVo) {

        if (!Objects.equals(userDataVo.getNewPassword(), "")) {
            UserEntity byUserInfo = userMapper.findByUserInfo(userDataVo.getUserId());
            if (userDataVo.getOldPassword().equals(byUserInfo.getPassword())) {
                userMapper.updateUserPassword(userDataVo.getUserId(), userDataVo.getNewPassword());
            } else {
                return "您输入的原始密码不正确";
            }
        }
        userMapper.updateUserInfo(userDataVo);
        return null;
    }

    @Override
    public String addUserFavorite(UserFavoriteEntity userFavoriteEntity) {

        if (userMapper.getUserFavorite(userFavoriteEntity.getUserId(), userFavoriteEntity.getProjectId()) != null) {
            return "该项目已收藏";
        }
        userMapper.addUserFavorite(userFavoriteEntity);
        return null;
    }

    /**
     * 购买项目
     */
    @Override
    @Transactional
    public String buyProject(Long userId, Integer regionId) {
        List<PhoneEntity> phoneEntities = phoneNumberMapper.getPhonesByProject(regionId);
        // 从phoneEntities列表中随机获取一个号码
        if (!phoneEntities.isEmpty()) {
            int randomIndex = (int) (Math.random() * phoneEntities.size());
            PhoneEntity phoneEntity = phoneEntities.get(randomIndex);

            // 使用事务来确保操作的原子性
            try {
                phoneNumberMapper.deletePhone(phoneEntity.getPhoneId());
                userMapper.addPhoneNumber(userId, phoneEntity.getPhoneNumber());
                return null;
            } catch (Exception e) {
                return "购买失败，请重试";
            }
        }
        return "号码可能已经被购买";
    }

    /**
     * 获取用户收藏的项目
     */
    @Override
    public List<Map<String, Object>> getUserFavorite(Long userId) {
        List<Map<String, Object>> userFavoriteByUserId = userMapper.getUserFavoriteByUserId(userId);
        return userFavoriteByUserId;
    }
}
