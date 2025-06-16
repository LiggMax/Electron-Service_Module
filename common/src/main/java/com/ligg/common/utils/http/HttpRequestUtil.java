package com.ligg.common.utils.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


/**
 * @Author Ligg
 * @Time 2025/6/16
 **/
@Component
public class HttpRequestUtil {
    private static RestTemplate restTemplate;

    @Autowired
    public void init(RestTemplate restTemplate) {
        HttpRequestUtil.restTemplate = restTemplate;
    }

    /**
     * 发送GET请求获取网页内容
     *
     * @param url 请求地址
     * @return 网页内容
     */
    public static String sendGetRequest(String url) {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        }
        return "";
    }
}