package com.demo.forum.api.admin;

import java.time.Instant;
import java.util.UUID;

public record MediaSummary(UUID id, String filename, String contentType, Instant uploadedAt) {
}
