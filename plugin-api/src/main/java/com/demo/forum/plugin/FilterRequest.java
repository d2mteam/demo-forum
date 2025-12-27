package com.demo.forum.plugin;

import java.util.Map;

public record FilterRequest(String content, Map<String, Object> context) {
}
