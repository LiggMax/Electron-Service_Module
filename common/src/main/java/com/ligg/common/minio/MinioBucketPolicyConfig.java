package com.ligg.common.minio;

import javax.swing.plaf.PanelUI;

/**
 * @Author Ligg
 * @Time 2025/6/11
 * <p>
 * 存储桶权限配置
 **/
public class MinioBucketPolicyConfig {
    /**
     * 存储桶配置
     * "Principal": "*" 表示允许所有用户访问。
     * "Action": "s3:GetObject" 表示允许下载/读取对象
     * "Action": "s3:PutObject" 表示允许上传/写入对象
     * %s 是格式化占位符，会被传入的 bucketName 替换
     */
    public static String createBucketPolicyConfig(String bucketName) {
        return """
                {
                    "Version": "2012-10-17",
                    "Statement": [
                        {
                            "Effect": "Allow",
                            "Principal": "*",
                            "Action": [
                                "s3:GetObject"
                            ],
                            "Resource": "arn:aws:s3:::%s/*"
                        },
                        {
                            "Effect": "Allow",
                            "Principal": "*",
                            "Action": [
                                "s3:PutObject"
                            ],
                            "Resource": "arn:aws:s3:::%s/*"
                        }
                    ]
                }
                """.formatted(bucketName, bucketName);
    }
}
