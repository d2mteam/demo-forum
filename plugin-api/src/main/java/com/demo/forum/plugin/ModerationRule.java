package com.demo.forum.plugin;

import org.pf4j.ExtensionPoint;

public interface ModerationRule extends ExtensionPoint {
    ModerationSignal evaluate(ModerationRequest request);
}
