package com.demo.forum.plugin;

import java.util.Map;
import java.util.UUID;

public record ModerationRequest(UUID postId, String content, UUID authorId, Map<String, Object> context) {
}
