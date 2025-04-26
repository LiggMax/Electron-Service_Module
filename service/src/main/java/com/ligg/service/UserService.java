package com.ligg.service;


import com.ligg.common.entity.AdminUserEntity;
import com.ligg.common.entity.UserEntity;
import com.ligg.common.entity.UserFavoriteEntity;
import com.ligg.common.vo.UserDataVo;

import java.util.List;
import java.util.Map;

public interface UserService {

    //根据账号和密码查询用户
    AdminUserEntity findByAdminUser(String account, String password);

    //生成token
    String createToken(Long userId, String account);

    //清理token
    void clearToken(String userId);

    //根据用户id查询管理员用户信息
    AdminUserEntity findByAdminUserInfo(Long userId);

     //根据账号和密码查询用户
    UserEntity findByUser(String account, String password);

    //根据用户id查询用户信息
    UserEntity findByUserInfo(Long userId);

    //更新用户信息
    String updateUserInfo(UserDataVo userDataVo);

    //添加用户项目收藏
    String addUserFavorite(UserFavoriteEntity userFavoriteEntity);

    //购买项目
    String buyProject(Long userId, Integer regionId);

    //查询用户收藏项目
    List<Map<String,Object>> getUserFavorite(Long userId);

    //获取用户订单
    List<Map<String,Object>> getUserOrder(Long userId);

    //账号注销
    void logoutAccount(Long userId);
}
