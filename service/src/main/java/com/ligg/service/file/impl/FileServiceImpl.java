package com.ligg.service.file.impl;

import com.ligg.common.utils.DatePathUtils;
import com.ligg.common.utils.minio.MinioProperties;
import com.ligg.service.file.FileService;
import io.minio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 文件上传业务类
 */
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
    public String uploadAvatar(MultipartFile file) {
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
            String fileName = String.join("/",datePath + UUID.randomUUID() + "-" + file.getOriginalFilename());
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(properties.getUserAvatar())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .object(fileName)
                    .build());
            return String.join("/", properties.getEndpoint(), properties.getUserAvatar(), fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

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
