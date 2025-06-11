package com.ligg.common.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    /**
     * 域名
     */
    private String endpoint;
    /**
     * ak密钥
     */
    private String accessKey;

    /**
     * sk密钥
     */
    private String secretKey;

    /**
     * 用户头像存储桶
     */
    private String userAvatar;

    /**
     * app存储桶
     */
    private String downloadApp;

    /**
     * 图片存储桶
     */
    private String Image;
}
