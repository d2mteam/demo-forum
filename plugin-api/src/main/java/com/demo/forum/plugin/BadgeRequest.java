package com.demo.forum.plugin;

import java.util.Map;
import java.util.UUID;

public record BadgeRequest(UUID userId, Map<String, Object> context) {
}
