package com.demo.forum.plugins.forumbase.service;

import com.demo.forum.plugin.FilterRequest;
import com.demo.forum.plugin.FilterResult;
import com.demo.forum.plugin.TextFilter;
import java.util.List;
import java.util.Locale;
import org.pf4j.Extension;

@Extension
public class ForumTextFilter implements TextFilter {
    private static final List<String> BLOCKED_TERMS = List.of("hate", "abuse");

    @Override
    public FilterResult filter(FilterRequest request) {
        if (request == null || request.content() == null) {
            return FilterResult.deny("missing-content");
        }
        String content = request.content();
        String normalized = content.toLowerCase(Locale.ROOT);
        for (String term : BLOCKED_TERMS) {
            if (normalized.contains(term)) {
                return FilterResult.deny("blocked-term:" + term);
            }
        }
        return FilterResult.allow(content.trim());
    }
}
