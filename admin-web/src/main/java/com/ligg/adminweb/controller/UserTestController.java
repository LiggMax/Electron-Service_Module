package com.ligg.adminweb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/controller01")
public class UserTestController {

    @GetMapping
    public String test() {
        System.out.println("user-web");
        return "user-web";
    }
}
