package com.demo.forum.plugin;

public record FilterResult(boolean allowed, String sanitizedContent, String reason) {
    public static FilterResult allow(String content) {
        return new FilterResult(true, content, "allowed");
    }

    public static FilterResult deny(String reason) {
        return new FilterResult(false, null, reason);
    }
}
