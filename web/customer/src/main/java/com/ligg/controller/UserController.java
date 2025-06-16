package com.ligg.controller;

import com.ligg.common.entity.user.UserEntity;
import com.ligg.common.entity.user.UserFavoriteEntity;
import com.ligg.common.statuEnum.BusinessStates;
import com.ligg.common.utils.JWTUtil;
import com.ligg.common.utils.Result;
import com.ligg.common.vo.UserDataVo;
import com.ligg.service.CustomerService;
import com.ligg.service.common.TokenService;
import com.ligg.service.file.FileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 客户控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private CustomerService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取用户信息
     */
    @GetMapping("/info")
    public Result<UserEntity> getUserInfo() {
        Map<String, Object> map = jwtUtil.parseToken(request.getHeader("Token"));
        Long userId = (Long) map.get("userId");
        UserEntity userInfo = userService.findByUserInfo(userId);
        return Result.success(BusinessStates.SUCCESS, userInfo);
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
        return Result.error(BusinessStates.SUCCESS, user);
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
            return Result.error(BusinessStates.BAD_REQUEST, result);
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
        
        // 获取项目ID、地区ID和购买数量
        Integer projectId = (Integer) userData.get("projectId");
        Integer regionId = ((Number) userData.get("regionId")).intValue();
        int quantity = userData.get("quantity") != null ?
                ((Number) userData.get("quantity")).intValue() : 1;

        // 验证购买数量
        if (quantity <= 0 || quantity > 100) {
            return Result.error(BusinessStates.BAD_REQUEST, "购买数量必须在1-100之间");
        }

        long startTime = System.currentTimeMillis();
        
        // 调用Service层方法处理购买逻辑
        Map<String, Object> result = userService.buyProject(userId, regionId, projectId, quantity);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        if (result.containsKey("error")) {
            log.warn("用户[{}]购买项目失败 - 耗时: {}ms, 原因: {}", userId, duration, result.get("error"));
            return Result.error(BusinessStates.BAD_REQUEST, (String) result.get("error"));
        }

        log.info("用户[{}]购买项目成功 - 耗时: {}ms, 购买数量: {}", userId, duration, quantity);
        return Result.success(BusinessStates.SUCCESS, result);
    }

    /**
     * 获取用户收藏
     */
    @GetMapping("/favorite")
    public Result<List<Map<String, Object>>> getUserFavorite() {
        Map<String, Object> map = jwtUtil.parseToken(request.getHeader("Token"));
        Long userId = (Long) map.get("userId");
        List<Map<String, Object>> userFavorite = userService.getUserFavorite(userId);
        return Result.success(BusinessStates.SUCCESS, userFavorite);
    }

    /**
     * 获取用户订单
     */
    @GetMapping("/order")
    public Result<List<Map<String, Object>>> getUserOrder() {
        Map<String, Object> map = jwtUtil.parseToken(request.getHeader("Token"));
        Long userId = (Long) map.get("userId");
        List<Map<String, Object>> userOrder = userService.getUserOrder(userId);
        return Result.success(BusinessStates.SUCCESS, userOrder);
    }

    /**
     * 账号注销
     */
    @DeleteMapping("/logoutAccount")
    public Result<?> logoutAccount() {
        Map<String, Object> map = jwtUtil.parseToken(request.getHeader("Token"));
        Long userId = (Long) map.get("userId");
        userService.logoutAccount(userId);
        tokenService.clearToken(userId);
        return Result.success();
    }

    /**
     * 上传头像
     */
    @PostMapping("/uploadAvatar")
    public Result<String> uploadAvatar(@RequestParam("avatar") MultipartFile avatar) {
        if (avatar.getSize() > 1024 * 1024 * 5) {
            return Result.error(BusinessStates.BAD_REQUEST, "文件大小不能超过5M");
        }
        Map<String, Object> map = jwtUtil.parseToken(request.getHeader("Token"));
        Long userId = (Long) map.get("userId");

        // 上传头像
        String avatarUrl = fileService.uploadAvatar(avatar);
        log.info("用户{}"+"上传头像{}"+"成功", userId, avatarUrl);
        if (avatarUrl == null) {
            return Result.error(BusinessStates.BAD_REQUEST, "上传失败,请稍后再试");
        }

        // 更新用户头像
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setUserAvatar(avatarUrl);
        userService.updateById(userEntity);
        return Result.success(BusinessStates.SUCCESS, avatarUrl);
    }
}
