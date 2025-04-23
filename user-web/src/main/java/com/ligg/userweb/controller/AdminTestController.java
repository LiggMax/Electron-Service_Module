package com.ligg.userweb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/controller02")
public class AdminTestController {

    @GetMapping
    public String test() {
        System.out.println("admin-web");
        return "admin-web";
    }
}
