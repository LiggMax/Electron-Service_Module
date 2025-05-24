package com.ligg.common.utils;

import io.minio.*;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * minio 工具类
 */
@Slf4j
public class MinioUtil {

    public static void main(String[] args) {

        String  endpoint = "http://129.204.224.233:19000";
        String accessKey = "Jp0EfeRfThrT4ResuF0O";
        String secretKey = "UMDeQycKFXHViS3LopjeOvDHJvN6IVxBCixo5G5e";
        String bucketName = "image";
        String file = "C:\\Users\\Administrator\\Downloads\\爱心.svg";

        // 创建MinioClient对象
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();

        try {
            // 判断bucket是否存在
            if(!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())){
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            String fileName = UUID.randomUUID() + file;
            minioClient.uploadObject(UploadObjectArgs.builder().filename(file).bucket(bucketName).object(fileName).build());
            log.info("上传成功: bucketName={}, fileName={}", bucketName, fileName);
            log.info("访问路径 {}",  endpoint + "/" + bucketName + "/" + fileName );
        } catch (Exception e) {
            log.error("查询bucket失败: bucketName={}, error={}", bucketName, e.getMessage(), e);
        }
    }
}
