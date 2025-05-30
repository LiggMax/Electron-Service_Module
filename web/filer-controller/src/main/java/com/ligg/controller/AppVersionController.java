package com.ligg.controller;

import com.ligg.common.entity.AppVersionEntity;
import com.ligg.common.utils.Result;
import com.ligg.service.file.AppVersionService;
import com.ligg.service.file.FileService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 软件更新
 */
@RestController
@RequestMapping("/api/version")
public class AppVersionController {

    @Autowired
    private FileService fileService;

    @Autowired
    private AppVersionService appVersionService;

    /**
     * 上传新版本信息
     */
    @PostMapping("/upload")
    public Result<String> uploadVersion(@RequestPart("appFile") MultipartFile appFile,
                                        @RequestPart("version") String version,
                                        @RequestPart("releaseNotes") @Pattern(regexp = "^.{1,100}$") String releaseNotes) {
        //大小不能超过1g
        if (appFile.getSize() > 1024 * 1024 * 1024) {
            return Result.error(400, "文件大小不合法");
        }

        String downloadUrl = fileService.uploadApp(appFile);
        if (downloadUrl != null) {
            //保存更新内容
            appVersionService.saveVersion(version, releaseNotes, downloadUrl);
            Result.success(200, "上传成功");
        }
        return Result.error(400, "上传失败");
    }

}
