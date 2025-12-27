package com.demo.forum.plugin;

import java.util.Map;
import java.util.UUID;

public record PolicyRequest(UUID userId, Action action, UUID resourceId, Map<String, Object> context) {
}
