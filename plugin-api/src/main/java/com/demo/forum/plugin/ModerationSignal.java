package com.demo.forum.plugin;

public record ModerationSignal(int weight, String reason) {
    public boolean flagged() {
        return weight > 0;
    }
}
