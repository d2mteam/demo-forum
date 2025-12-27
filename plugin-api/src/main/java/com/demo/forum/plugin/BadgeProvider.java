package com.demo.forum.plugin;

import java.util.List;
import org.pf4j.ExtensionPoint;

public interface BadgeProvider extends ExtensionPoint {
    List<String> badgesForUser(BadgeRequest request);
}
