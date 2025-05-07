package com.ligg.controller;

import com.ligg.common.utils.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/adminWeb/account")
public class AdminWebAccountController {



    @RequestMapping("/login")
    public Result<String> login() {


    }
}
