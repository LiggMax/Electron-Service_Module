package com.ligg.userweb.controller;

import com.ligg.common.entity.UserEntity;
import com.ligg.common.utils.JWTUtil;
import com.ligg.common.utils.Result;
import com.ligg.common.vo.UserDataVo;
import com.ligg.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return Result.success(200,userInfo);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public Result<?> updateUserInfo(@RequestBody Map<String, Object> userData){
        Map<String, Object> map = jwtUtil.parseToken(request.getHeader("Token"));
        Long userId = (Long) map.get("userId");

        UserDataVo userDataVo = new UserDataVo();
        userDataVo.setUserId(userId);
        userDataVo.setNickName((String) userData.get("nickName"));
        userDataVo.setOldPassword((String) userData.get("oldPassword"));
        userDataVo.setNewPassword((String) userData.get("newPassword"));
        userDataVo.setUserAvatar((String) userData.get("userAvatar"));
        String user = userService.updateUserInfo(userDataVo);
        if (user == null){
            return Result.success();
        }
        return Result.error(400, user);
    }
}
