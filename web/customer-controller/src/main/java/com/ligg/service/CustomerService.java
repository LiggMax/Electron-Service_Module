package com.ligg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ligg.common.entity.adminweb.AdminWebUserEntity;
import com.ligg.common.entity.user.UserEntity;
import com.ligg.common.entity.user.UserFavoriteEntity;
import com.ligg.common.vo.UserDataVo;

import java.util.List;
import java.util.Map;

public interface CustomerService extends IService<UserEntity> {

     //根据账号查询用户
    UserEntity findByUser(String account);

    //根据用户id查询用户信息
    UserEntity findByUserInfo(Long userId);

    //更新用户信息
    String updateUserInfo(UserDataVo userDataVo);

    //添加用户项目收藏
    String addUserFavorite(UserFavoriteEntity userFavoriteEntity);

    //购买项目
    String buyProject(Long userId, Integer regionId, Integer projectId);

    //查询用户收藏项目
    List<Map<String,Object>> getUserFavorite(Long userId);

    //获取用户订单
    List<Map<String,Object>> getUserOrder(Long userId);

    //账号注销
    void logoutAccount(Long userId);

    //根据账号查询信息
    UserEntity findAccountAndPasswordByUser(String account);

    //注册账号
    void registerAccount(String account, String password);

    //获取管理员信息
    AdminWebUserEntity getAdminWebInfo(String account);

    //更新登录时间
    void updateLoginTime(Long userId);
}
