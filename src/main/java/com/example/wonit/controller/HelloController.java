package com.example.wonit.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 사용자의 요청을 받아서 화면에 돌려주는 계층
@RestController
@RequestMapping("/hello")
public class HelloController {
    @GetMapping
    public String sayHello(){
        return "HELLO";
    }
    @GetMapping("/{id}")
    public String sayHello(@PathVariable String id){
        return "HELLO"+id;
    }
}
