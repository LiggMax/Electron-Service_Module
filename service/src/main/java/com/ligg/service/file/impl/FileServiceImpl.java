package com.ligg.service.file.impl;

import com.ligg.common.utils.DatePathUtils;
import com.ligg.common.minio.MinioProperties;
import com.ligg.service.file.FileService;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static com.ligg.common.minio.MinioBucketPolicyConfig.createBucketPolicyConfig;

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
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                        .bucket(properties.getUserAvatar())
                        .config(createBucketPolicyConfig(properties.getUserAvatar()))
                        .build());
            }
            //上传文件
            String datePath = DatePathUtils.generateYearMonthDayPath();
            String fileName = String.join("/", datePath + UUID.randomUUID() + "-" + avatar.getOriginalFilename());
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
    @Override
    public String uploadApp(MultipartFile appFile, Integer app) {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(properties.getDownloadApp()).build())) {
                //创建存储桶
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(properties.getDownloadApp())
                        .build());
                //设置权限
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                        .bucket(properties.getDownloadApp())
                        .config(createBucketPolicyConfig(properties.getDownloadApp()))
                        .build());
            }
            //上传文件
            String folder;
            if (app == 0) {
                folder = "kehu";
            } else if (app == 1) {
                folder = "kashang";
            } else {
                log.warn("不支持的应用类型: {}", app);
                return null;
            }
            String datePath = DatePathUtils.generateYearMonthDayPath();
            String fileName = String.join("/", folder, datePath + appFile.getOriginalFilename());
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(properties.getDownloadApp())
                    .stream(appFile.getInputStream(), appFile.getSize(), -1)
                    .contentType(appFile.getContentType())
                    .object(fileName)
                    .build());

            log.info("上传成功: bucketName={}, fileName={}", properties.getDownloadApp(), fileName);
            return String.join("/", properties.getEndpoint(), properties.getDownloadApp(), fileName);
        } catch (Exception e) {
            log.error("上传失败: bucketName={}, error={}", properties.getDownloadApp(), e.getMessage(), e);
        }
        return null;
    }

    /**
     * 图片上传
     */
    @Override
    public String uploadImage(MultipartFile image) {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(properties.getImage()).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(properties.getImage())
                        .build());
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                        .bucket(properties.getImage())
                        .config(createBucketPolicyConfig(properties.getImage()))
                        .build());
            }

            String datePath = DatePathUtils.generateYearMonthDayPath();
            String fileName = String.join("/", datePath + UUID.randomUUID() + "-" + image.getOriginalFilename());
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(properties.getImage())
                    .stream(image.getInputStream(), image.getSize(), -1)
                    .contentType(image.getContentType())
                    .object(fileName)
                    .build());
            return String.join("/", properties.getEndpoint(), properties.getImage(), fileName);
        } catch (Exception e) {
            log.error("上传失败: bucketName={}, error={}", properties.getImage(), e.getMessage(), e);
        }
        return null;
    }
}
