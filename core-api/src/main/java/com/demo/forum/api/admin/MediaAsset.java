package com.demo.forum.api.admin;

import java.time.Instant;
import java.util.UUID;

public record MediaAsset(UUID id, String filename, String contentType, byte[] data, Instant uploadedAt) {
}
