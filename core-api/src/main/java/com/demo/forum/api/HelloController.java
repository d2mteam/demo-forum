package com.demo.forum.api;

import com.demo.forum.application.ForumInfoService;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private final ForumInfoService forumInfoService;

    public HelloController(ForumInfoService forumInfoService) {
        this.forumInfoService = forumInfoService;
    }

    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("message", forumInfoService.helloMessage());
    }
}
