package com.ligg.common.utils.minio;

import io.minio.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.UUID;

/**
 * minio 工具类
 */
@Slf4j
public class MinioUtil {

    public static void main(String[] args) {
//        http://123.51.208.249/
        String  endpoint = "http://123.51.208.249:9000";
        String accessKey = "L28ShZVy3uUyjy4snjXH";
        String secretKey = "Izbn99vWbdAHMx5qafSzB4NhTHc2mwMN7nYW3EkW";
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
            File file1 = new File(file);
            String fileName = UUID.randomUUID() + file1.getCanonicalFile().getName();
            minioClient.uploadObject(UploadObjectArgs.builder().filename(file).bucket(bucketName).object(fileName).build());
            log.info("上传成功: bucketName={}, fileName={}", bucketName, fileName);
            log.info("访问路径 {}",  endpoint + "/" + bucketName + "/" + fileName );
        } catch (Exception e) {
            log.error("查询bucket失败: bucketName={}, error={}", bucketName, e.getMessage(), e);
        }
    }
}
