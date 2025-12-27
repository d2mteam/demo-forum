package com.demo.forum.plugins.spamlinks;

import com.demo.forum.plugin.FilterRequest;
import com.demo.forum.plugin.FilterResult;
import com.demo.forum.plugin.TextFilter;
import java.util.List;
import org.pf4j.Extension;

@Extension
public class SpamLinksFilter implements TextFilter {
    private static final List<String> BLOCKED_DOMAINS = List.of("spam.example", "bad-domain.test");

    @Override
    public FilterResult filter(FilterRequest request) {
        String content = request.content();
        for (String blocked : BLOCKED_DOMAINS) {
            if (content != null && content.contains(blocked)) {
                return FilterResult.deny("blocked domain: " + blocked);
            }
        }
        return FilterResult.allow(content);
    }
}
