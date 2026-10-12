package com.example.wonit.controller;


import org.springframework.web.bind.annotation.*;

// 사용자의 요청을 받아서 화면에 돌려주는 계층
// 라우팅 기능: 사용자의 요청을 주소줄과 매핑해서 서버에 전달하는 역할
@RestController
@RequestMapping("/hello")
public class HelloController { // localhost:8080/hello
    @GetMapping
    public String sayHello(){
        return "Hello, Spring Boot!";
    }

//    @GetMapping("/{id}")// localhost:8080/hello/123
//    public String sayHelloWithPathVariable(@PathVariable String id){
//        return "Hello, Spring Boot! "+id;
//    }

//    // 매핑 중복 불가, sayHello와 아래 둘 중 하나 주석처리 후 실행
//    @GetMapping // localhost:8080/hello?key=value
//    public String sayHelloWithRequestParam(@RequestParam(value = "id") String id){
//        return "Hello, Spring Boot! "+id;
//    }

//    // PathVariable과 RequestParam 같이 사용 가능
//    @GetMapping("/{path}") // localhost:8080/hello/path?key=value
//    public String sayHelloWithPathVariableAndRequestParam(@PathVariable String path,@RequestParam(value = "id", defaultValue = "안 들어옴") String id){
//        return "Hello, Spring Boot! "+path+id;
//    }

    @GetMapping("/{path}") // localhost:8080/hello/path?key=value
    public String sayHello(@PathVariable String path,
                           @RequestParam(value = "name", defaultValue = "안 들어옴") String name,
                           @RequestParam(value = "footsize", defaultValue = "안 들어옴2") String footsize){
        return "Hello, Spring Boot! "+path+name+footsize;
    }
}
