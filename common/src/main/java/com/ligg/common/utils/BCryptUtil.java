package com.ligg.common.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * BCrypt 密码加密工具类
 */
public final class BCryptUtil {

    // 默认计算强度 (work factor)，范围 4~31，值越大越安全但越慢
    private static final int DEFAULT_LOG_ROUNDS = 12;

    private BCryptUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 加密密码（使用默认计算强度）
     * @param plainPassword 明文密码
     * @return 加密后的哈希密码
     */
    public static String encrypt(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(DEFAULT_LOG_ROUNDS));
    }

    /**
     * 加密密码（自定义计算强度）
     * @param plainPassword 明文密码
     * @param logRounds 计算强度 (4~31)
     * @return 加密后的哈希密码
     */
    public static String encrypt(String plainPassword, int logRounds) {
        if (logRounds < 4 || logRounds > 31) {
            throw new IllegalArgumentException("logRounds must be between 4 and 31");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(logRounds));
    }

    /**
     * 验证密码是否正确
     * @param plainPassword 待验证的明文密码
     * @param hashedPassword 已加密的哈希密码
     * @return true 验证成功，false 验证失败
     */
    public static boolean verify(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
            return false;
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    /**
     * 判断字符串是否是BCrypt加密的哈希值
     * @param hashedPassword 哈希密码
     * @return true 是BCrypt哈希，false 不是
     */
    public static boolean isBCryptHash(String hashedPassword) {
        return hashedPassword != null && hashedPassword.startsWith("$2a$");
    }
}