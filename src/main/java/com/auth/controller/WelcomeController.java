package com.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/welcome")
public class WelcomeController {


    @GetMapping("/hello")
    public String hello() {
        int x =100;
        return "hello";
    }

    @GetMapping("/hi")
    public String hi() {
        return "hi";
    }

}
