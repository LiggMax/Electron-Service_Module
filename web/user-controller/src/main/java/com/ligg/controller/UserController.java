package com.ligg.controller;

import com.ligg.common.entity.UserEntity;
import com.ligg.common.entity.UserFavoriteEntity;
import com.ligg.common.utils.JWTUtil;
import com.ligg.common.utils.Result;
import com.ligg.common.vo.UserDataVo;
import com.ligg.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public Result<UserEntity> getUserInfo() {
        Map<String, Object> map = jwtUtil.parseToken(request.getHeader("Token"));
        Long userId = (Long) map.get("userId");
        UserEntity userInfo = userService.findByUserInfo(userId);
        return Result.success(200, userInfo);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public Result<?> updateUserInfo(@RequestBody Map<String, Object> userData) {
        Map<String, Object> map = jwtUtil.parseToken(request.getHeader("Token"));
        Long userId = (Long) map.get("userId");

        UserDataVo userDataVo = new UserDataVo();
        userDataVo.setUserId(userId);
        userDataVo.setNickName((String) userData.get("nickName"));
        userDataVo.setOldPassword((String) userData.get("oldPassword"));
        userDataVo.setNewPassword((String) userData.get("newPassword"));
        userDataVo.setUserAvatar((String) userData.get("userAvatar"));
        String user = userService.updateUserInfo(userDataVo);
        if (user == null) {
            return Result.success();
        }
        return Result.error(400, user);
    }

    /**
     * 项目收藏
     */
    @PostMapping("/favorite")
    public Result<?> addUserFavorite(@RequestBody UserFavoriteEntity userFavoriteEntity) {
        Map<String, Object> map = jwtUtil.parseToken(request.getHeader("Token"));
        userFavoriteEntity.setUserId((Long) map.get("userId"));

        String result = userService.addUserFavorite(userFavoriteEntity);
        if (result != null) {
            return Result.error(400, result);
        }
        return Result.success();
    }

    /**
     * 购买项目
     */
    @PostMapping("/buy")
    public Result<?> buyProject(@RequestBody Map<String, Object> userData) {
        Map<String, Object> map = jwtUtil.parseToken(request.getHeader("Token"));
        Long userId = (Long) map.get("userId");

        Integer regionId = (Integer) userData.get("regionId");
        String result = userService.buyProject(userId, regionId);
        if (result != null) {
            return Result.error(400, result);
        }
        return Result.success();
   }

   /**
    * 获取用户收藏
    */
   @GetMapping("/favorite")
    public Result<List<Map<String, Object>>> getUserFavorite() {
        Map<String, Object> map = jwtUtil.parseToken(request.getHeader("Token"));
        Long userId = (Long) map.get("userId");
        List<Map<String, Object>> userFavorite = userService.getUserFavorite(userId);
        return Result.success(200, userFavorite);
   }

    /**
     * 获取用户订单
     */
    @GetMapping("/order")
    public Result<List<Map<String, Object>>> getUserOrder() {
        Map<String, Object> map = jwtUtil.parseToken(request.getHeader("Token"));
        Long userId = (Long) map.get("userId");
        List<Map<String, Object>> userOrder = userService.getUserOrder(userId);
        return Result.success(200, userOrder);
    }

    /**
     * 账号注销
     */
    @DeleteMapping("/logoutAccount")
    public Result<?> logoutAccount() {
        Map<String, Object> map = jwtUtil.parseToken(request.getHeader("Token"));
        Long userId = (Long) map.get("userId");
        userService.logoutAccount(userId);
        userService.clearToken(userId);
        return Result.success();
    }
}
