package com.demo.forum.plugins.forumbase.service;

import com.demo.forum.plugin.ModerationRequest;
import com.demo.forum.plugin.ModerationRule;
import com.demo.forum.plugin.ModerationSignal;
import java.util.List;
import java.util.Locale;
import org.pf4j.Extension;

@Extension
public class ForumModerationRule implements ModerationRule {
    private static final List<String> BANNED_TERMS = List.of("spam", "scam", "phish");

    @Override
    public ModerationSignal evaluate(ModerationRequest request) {
        if (request == null || request.content() == null) {
            return new ModerationSignal(0, "empty-content");
        }
        String content = request.content().toLowerCase(Locale.ROOT);
        int weight = 0;
        StringBuilder reason = new StringBuilder();
        for (String term : BANNED_TERMS) {
            if (content.contains(term)) {
                weight += 2;
                reason.append("banned-term:").append(term).append(';');
            }
        }
        int linkCount = content.split("https?://").length - 1;
        if (linkCount >= 3) {
            weight += 1;
            reason.append("excessive-links;");
        }
        if (weight == 0) {
            return new ModerationSignal(0, "clean");
        }
        return new ModerationSignal(weight, reason.toString());
    }
}
