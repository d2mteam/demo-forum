package com.demo.forum.plugins.badgestreak;

import com.demo.forum.plugin.BadgeProvider;
import com.demo.forum.plugin.BadgeRequest;
import java.util.List;
import org.pf4j.Extension;

@Extension
public class StreakBadgeProvider implements BadgeProvider {
    @Override
    public List<String> badgesForUser(BadgeRequest request) {
        Object daysVisited = request.context().getOrDefault("daysVisited", 0);
        if (daysVisited instanceof Number number && number.intValue() >= 7) {
            return List.of("7-day-streak");
        }
        return List.of();
    }
}
