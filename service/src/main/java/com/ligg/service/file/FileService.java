package com.ligg.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    //  上传头像
    String uploadAvatar(MultipartFile avatar);

    //上传APP
    String uploadApp(MultipartFile appFile, Integer app);
}
