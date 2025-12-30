package com.demo.forum.plugins.forumbase.service;

import com.demo.forum.plugin.Action;
import com.demo.forum.plugin.Decision;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ForumRateLimiter {
    private final int limit;
    private final Duration window;
    private final Clock clock;
    private final Map<String, Deque<Instant>> activity = new ConcurrentHashMap<>();

    public ForumRateLimiter(int limit, Duration window, Clock clock) {
        this.limit = limit;
        this.window = window;
        this.clock = clock;
    }

    public Decision check(UUID userId, Action action) {
        Instant now = clock.instant();
        String key = userId + ":" + action.name();
        Deque<Instant> timestamps = activity.computeIfAbsent(key, ignored -> new ArrayDeque<>());
        synchronized (timestamps) {
            Instant cutoff = now.minus(window);
            while (!timestamps.isEmpty() && timestamps.peekFirst().isBefore(cutoff)) {
                timestamps.pollFirst();
            }
            if (timestamps.size() >= limit) {
                return Decision.deny("rate-limit-exceeded");
            }
            timestamps.addLast(now);
        }
        return Decision.allow();
    }
}
