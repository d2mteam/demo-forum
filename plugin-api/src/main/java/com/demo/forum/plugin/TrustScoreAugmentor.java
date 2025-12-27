package com.demo.forum.plugin;

import org.pf4j.ExtensionPoint;

public interface TrustScoreAugmentor extends ExtensionPoint {
    int adjust(TrustScoreRequest request);
}
