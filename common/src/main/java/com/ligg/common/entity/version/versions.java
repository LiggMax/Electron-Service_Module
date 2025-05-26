package com.ligg.common.entity.version;

import lombok.Data;

@Data
public class versions {
    private String version; //版本号
    private String platform;      // "win32" | "darwin" | "linux"
    private String releaseNotes; // 更新内容信息
    private boolean mandatory; // 是否强制更新
    private String downloadUrl; // 下载地址

}
