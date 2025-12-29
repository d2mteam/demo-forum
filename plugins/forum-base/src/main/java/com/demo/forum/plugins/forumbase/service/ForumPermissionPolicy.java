package com.demo.forum.plugins.forumbase.service;

import com.demo.forum.plugin.Action;
import com.demo.forum.plugin.Decision;
import com.demo.forum.plugin.PermissionPolicy;
import com.demo.forum.plugin.PolicyRequest;
import java.util.Collection;
import java.util.Map;
import org.pf4j.Extension;

@Extension
public class ForumPermissionPolicy implements PermissionPolicy {
    @Override
    public Decision evaluate(PolicyRequest request) {
        if (request == null) {
            return Decision.deny("missing-request");
        }
        Action action = request.action();
        if (action == Action.EDIT_POST || action == Action.EDIT_OTHERS) {
            return Decision.deny("append-only");
        }
        Map<String, Object> context = request.context();
        String scope = context == null ? null : (String) context.get("scope");
        Object allowedScopesValue = context == null ? null : context.get("allowedScopes");
        if (scope == null || allowedScopesValue == null) {
            return Decision.allow();
        }
        if (allowedScopesValue instanceof Collection<?> collection) {
            boolean allowed = collection.stream().anyMatch(item -> scope.equals(String.valueOf(item)));
            if (allowed) {
                return Decision.allow();
            }
        }
        return Decision.deny("scope-denied");
    }
}
