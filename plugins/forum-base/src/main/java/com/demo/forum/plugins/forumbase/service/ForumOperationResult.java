package com.demo.forum.plugins.forumbase.service;

public record ForumOperationResult<T>(boolean allowed, String reason, T payload) {
    public static <T> ForumOperationResult<T> allowed(T payload) {
        return new ForumOperationResult<>(true, "allowed", payload);
    }

    public static <T> ForumOperationResult<T> denied(String reason) {
        return new ForumOperationResult<>(false, reason, null);
    }
}
