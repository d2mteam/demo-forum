package com.demo.forum.plugins.forumbase.model;

import java.time.Instant;
import java.util.UUID;

public record Post(UUID id, UUID topicId, UUID authorId, String content, Instant createdAt) {
}
