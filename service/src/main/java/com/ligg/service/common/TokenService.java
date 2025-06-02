package com.ligg.service.common;

/**
 * @Author Ligg
 * @Time 2025/6/2
 **/
public interface TokenService {

    //生成token
    String createToken(Long userId, String account);

    //清理token
    void clearToken(Long userId);
}
