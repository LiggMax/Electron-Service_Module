package com.ligg.controller;

import com.ligg.common.entity.admin.MerchantEntity;
import com.ligg.common.status.BusinessStatus;
import com.ligg.common.utils.JWTUtil;
import com.ligg.common.utils.Result;
import com.ligg.common.vo.OrderVo;
import com.ligg.service.MerchantUserService;
import com.ligg.service.common.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/merchant/user")
public class AdminUserController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JWTUtil jwtUtil;


    @Autowired
    private MerchantUserService merchantUserService;

    @Autowired
    private TokenService tokenService;
    /**
     * 退出
     */
    @GetMapping("/logout")
    public Result<String> logout() {
        String token = request.getHeader("Token");
        Map<String, Object> map = jwtUtil.parseToken(token);
        Long userId = (Long) map.get("userId");
        tokenService.clearToken(userId);
        return Result.success(BusinessStatus.SUCCESS, "退出成功");
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/info")
    public Result<MerchantEntity> getUserInfo() {
        Map<String, Object> map = jwtUtil.parseToken(request.getHeader("Token"));
        if (map == null){
            return Result.error(BusinessStatus.UNAUTHORIZED, "请重新登录");
        }
        Long userId = (Long) map.get("userId");
        //根据id获取用户信息
        MerchantEntity AdminUserInfo = merchantUserService.getUserById(userId);
        return Result.success(BusinessStatus.SUCCESS, AdminUserInfo);
    }

    /**
     * 获取订单信息
     */
    @GetMapping("/order")
    public Result<List<OrderVo>> getUserOrder() {
        Map<String, Object> map = jwtUtil.parseToken(request.getHeader("Token"));
        Long AdminId = (Long) map.get("userId");
        List<OrderVo> orderVoList = merchantUserService.getOrder(AdminId);
        return Result.success(BusinessStatus.SUCCESS, orderVoList);
    }
}
