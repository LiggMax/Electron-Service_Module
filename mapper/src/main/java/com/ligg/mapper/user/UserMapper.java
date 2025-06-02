package com.ligg.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ligg.common.entity.adminweb.AdminWebUserEntity;
import com.ligg.common.entity.user.UserEntity;
import com.ligg.common.entity.user.UserFavoriteEntity;
import com.ligg.common.vo.UserDataVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    //根据账号查询用户信息
    UserEntity findByUser(String account);

    //根据id查询用户信息
    UserEntity findByUserInfo(Long userId);

    //修改用户密码
    void updateUserPassword(Long userId, String newPassword);

    void updateUserInfo(UserDataVo userDataVo);

    //根据用户id和项目id查询收藏数据
    UserFavoriteEntity getUserFavorite(Long userId, Long projectId);

    //根据用户id查询收藏项目
    @MapKey("projectId")
    List<Map<String, Object>> getUserFavoriteByUserId(Long userId);

    //添加用户收藏
    void addUserFavorite(UserFavoriteEntity userFavoriteEntity);

    //添加号码
    int addPhoneNumber(Long userId, Long phoneNumber,Long adminUserId, Integer projectId,Float projectMoney,Float phoneMoney,Integer regionId);

    //用户订单
    @MapKey("user_project_id")
    List<Map<String, Object>> getUserOrder(Long userId);

    //账号注销
    void logoutAccount(Long userId, int status);

    //根据账号密码查询用户信息
    UserEntity findAccountAndPasswordByUser(String account);

    //注册账号
//    void registerAccount(String account, String password);

    //根据账号密码查询后台管理用户信息
    AdminWebUserEntity findByAdminWebUser(String account);

    //更新订单状态和添加验证码
    void updateOrderAndAddCode(int i);

    //更新用户余额
    void updateUserMoney(Long userId, Float deduct);
}