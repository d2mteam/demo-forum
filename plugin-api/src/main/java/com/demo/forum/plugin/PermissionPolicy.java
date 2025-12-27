package com.demo.forum.plugin;

import org.pf4j.ExtensionPoint;

public interface PermissionPolicy extends ExtensionPoint {
    Decision evaluate(PolicyRequest request);
}
