package com.demo.forum.plugin;

import java.util.Map;
import java.util.UUID;

public record TrustScoreRequest(UUID userId, Map<String, Object> context) {
}
