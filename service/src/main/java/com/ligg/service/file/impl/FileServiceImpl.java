package com.ligg.service.file.impl;

import com.ligg.common.utils.DatePathUtils;
import com.ligg.common.utils.minio.MinioProperties;
import com.ligg.service.file.FileService;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * 文件上传业务类
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private MinioProperties properties;

    @Autowired
    private MinioClient minioClient;

    /**
     * 上传头像
     */
    @Override
    public String uploadAvatar(MultipartFile avatar) {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(properties.getUserAvatar()).build());
            if (!bucketExists) {
                //创建桶
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(properties.getUserAvatar())
                        .build());
                //设置桶权限
                minioClient.setBucketPolicy(SetBucketPolicyArgs
                        .builder()
                        .bucket(properties.getUserAvatar())
                        .config(createBucketPolicyConfig(properties.getUserAvatar()))
                        .build());
            }
            //上传文件
            String datePath = DatePathUtils.generateYearMonthDayPath();
            String fileName = String.join("/",datePath + UUID.randomUUID() + "-" + avatar.getOriginalFilename());
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(properties.getUserAvatar())
                    .stream(avatar.getInputStream(), avatar.getSize(), -1)
                    .contentType(avatar.getContentType())
                    .object(fileName)
                    .build());
            return String.join("/", properties.getEndpoint(), properties.getUserAvatar(), fileName);
        } catch (Exception e) {
            log.error("上传失败: bucketName={}, error={}", properties.getUserAvatar(), e.getMessage(), e);
        }
        return null;
    }

    /**
     * 软件包上传
     */


    /**
     * 存储桶配置
     * "Principal": "*" 表示允许所有用户访问。
     * "Action": "s3:GetObject" 表示允许下载/读取对象
     * "Action": "s3:PutObject" 表示允许上传/写入对象
     * %s 是格式化占位符，会被传入的 bucketName 替换
     */
    private String createBucketPolicyConfig(String bucketName) {
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
