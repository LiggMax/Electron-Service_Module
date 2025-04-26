package com.ligg.mapper;

import com.ligg.common.entity.AdminUserEntity;
import com.ligg.common.entity.UserEntity;
import com.ligg.common.entity.UserFavoriteEntity;
import com.ligg.common.vo.UserDataVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    //根据账号密码查询用户信息
    AdminUserEntity findByAdminUser(String account, String password);

    //根据id查询用户信息
    AdminUserEntity findByAdminUserInfo(Long userId);

    //根据查询用户信息
    UserEntity findByUser(String account);

    //根据id查询用户信息
    UserEntity findByUserInfo(Long userId);

    //修改用户密码
    void updateUserPassword(Long userId, String newPassword);

    void updateUserInfo(UserDataVo userDataVo);

    //根据用户id和项目id查询收藏数据
    UserFavoriteEntity getUserFavorite(Long userId, Long projectId);

    //根据用户id查询收藏项目
    List<Map<String,Object>> getUserFavoriteByUserId(Long userId);

    //添加用户收藏
    void addUserFavorite(UserFavoriteEntity userFavoriteEntity);

    //添加号码
    void addPhoneNumber(Long userId,Long phoneNumber);
}
