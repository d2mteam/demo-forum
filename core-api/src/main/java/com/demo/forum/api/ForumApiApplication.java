package com.demo.forum.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.demo.forum")
public class ForumApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ForumApiApplication.class, args);
    }
}
