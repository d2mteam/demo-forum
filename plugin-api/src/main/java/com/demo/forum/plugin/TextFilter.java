package com.demo.forum.plugin;

import org.pf4j.ExtensionPoint;

public interface TextFilter extends ExtensionPoint {
    FilterResult filter(FilterRequest request);
}
