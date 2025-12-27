package com.demo.forum.api;

import com.demo.forum.application.ForumInfoService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {
    private final ForumInfoService forumInfoService;

    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("message", forumInfoService.helloMessage());
    }
}
