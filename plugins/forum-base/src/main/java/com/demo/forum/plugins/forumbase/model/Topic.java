package com.demo.forum.plugins.forumbase.model;

import java.time.Instant;
import java.util.UUID;

public record Topic(UUID id, UUID categoryId, UUID authorId, String title, Instant createdAt) {
}
