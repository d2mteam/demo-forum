package com.demo.forum.plugins.toxicity;

import com.demo.forum.plugin.ModerationRequest;
import com.demo.forum.plugin.ModerationRule;
import com.demo.forum.plugin.ModerationSignal;
import java.util.List;
import org.pf4j.Extension;

@Extension
public class SimpleToxicityRule implements ModerationRule {
    private static final List<String> WATCHWORDS = List.of("hate", "abuse", "toxic");

    @Override
    public ModerationSignal evaluate(ModerationRequest request) {
        String content = request.content();
        if (content == null) {
            return new ModerationSignal(0, "empty");
        }
        long hits = WATCHWORDS.stream().filter(word -> content.toLowerCase().contains(word)).count();
        if (hits > 0) {
            return new ModerationSignal((int) hits, "matched watchwords: " + hits);
        }
        return new ModerationSignal(0, "clean");
    }
}
