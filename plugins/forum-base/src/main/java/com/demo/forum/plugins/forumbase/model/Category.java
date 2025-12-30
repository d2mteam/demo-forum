package com.demo.forum.plugins.forumbase.model;

import java.time.Instant;
import java.util.UUID;

public record Category(UUID id, String name, String description, Instant createdAt) {
}
