package com.demo.forum.plugins.forumbase.service;

import com.demo.forum.plugin.Action;
import com.demo.forum.plugin.Decision;
import com.demo.forum.plugin.FilterRequest;
import com.demo.forum.plugin.FilterResult;
import com.demo.forum.plugin.ModerationRequest;
import com.demo.forum.plugin.ModerationSignal;
import com.demo.forum.plugin.PolicyRequest;
import com.demo.forum.plugins.forumbase.model.Category;
import com.demo.forum.plugins.forumbase.model.Post;
import com.demo.forum.plugins.forumbase.model.Topic;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ForumBaseService {
    private static final int DEFAULT_RATE_LIMIT = 10;
    private static final Duration DEFAULT_RATE_WINDOW = Duration.ofMinutes(1);

    private final ForumRepository repository;
    private final ForumPermissionPolicy permissionPolicy;
    private final ForumRateLimiter rateLimiter;
    private final ForumTextFilter textFilter;
    private final ForumModerationRule moderationRule;

    public ForumBaseService() {
        this(new ForumRepository(), new ForumPermissionPolicy(), new ForumRateLimiter(DEFAULT_RATE_LIMIT, DEFAULT_RATE_WINDOW, Clock.systemUTC()),
                new ForumTextFilter(), new ForumModerationRule());
    }

    public ForumBaseService(ForumRepository repository, ForumPermissionPolicy permissionPolicy, ForumRateLimiter rateLimiter,
                            ForumTextFilter textFilter, ForumModerationRule moderationRule) {
        this.repository = repository;
        this.permissionPolicy = permissionPolicy;
        this.rateLimiter = rateLimiter;
        this.textFilter = textFilter;
        this.moderationRule = moderationRule;
    }

    public ForumOperationResult<Category> createCategory(UUID actorId, String name, String description, Set<String> scopes) {
        Set<String> safeScopes = scopes == null ? Set.of() : scopes;
        Decision decision = permissionPolicy.evaluate(new PolicyRequest(actorId, Action.CREATE_TOPIC, null,
                Map.of("scope", "category:create", "allowedScopes", safeScopes)));
        if (!decision.allowed()) {
            return ForumOperationResult.denied(decision.reason());
        }
        Category category = new Category(UUID.randomUUID(), name, description, Instant.now());
        repository.saveCategory(category);
        return ForumOperationResult.allowed(category);
    }

    public ForumOperationResult<Topic> createTopic(UUID actorId, UUID categoryId, String title, Set<String> scopes) {
        Set<String> safeScopes = scopes == null ? Set.of() : scopes;
        Decision decision = permissionPolicy.evaluate(new PolicyRequest(actorId, Action.CREATE_TOPIC, categoryId,
                Map.of("scope", "topic:create", "allowedScopes", safeScopes)));
        if (!decision.allowed()) {
            return ForumOperationResult.denied(decision.reason());
        }
        Decision rateDecision = rateLimiter.check(actorId, Action.CREATE_TOPIC);
        if (!rateDecision.allowed()) {
            return ForumOperationResult.denied(rateDecision.reason());
        }
        if (repository.findCategory(categoryId).isEmpty()) {
            return ForumOperationResult.denied("unknown-category");
        }
        Topic topic = new Topic(UUID.randomUUID(), categoryId, actorId, title, Instant.now());
        repository.saveTopic(topic);
        return ForumOperationResult.allowed(topic);
    }

    public ForumOperationResult<Post> appendPost(UUID actorId, UUID topicId, String content, Set<String> scopes) {
        Set<String> safeScopes = scopes == null ? Set.of() : scopes;
        Decision permission = permissionPolicy.evaluate(new PolicyRequest(actorId, Action.CREATE_POST, topicId,
                Map.of("scope", "post:create", "allowedScopes", safeScopes)));
        if (!permission.allowed()) {
            return ForumOperationResult.denied(permission.reason());
        }
        Decision rateDecision = rateLimiter.check(actorId, Action.CREATE_POST);
        if (!rateDecision.allowed()) {
            return ForumOperationResult.denied(rateDecision.reason());
        }
        if (repository.findTopic(topicId).isEmpty()) {
            return ForumOperationResult.denied("unknown-topic");
        }
        FilterResult filterResult = textFilter.filter(new FilterRequest(content, Map.of("topicId", topicId)));
        if (!filterResult.allowed()) {
            return ForumOperationResult.denied(filterResult.reason());
        }
        ModerationSignal signal = moderationRule.evaluate(new ModerationRequest(UUID.randomUUID(), filterResult.sanitizedContent(),
                actorId, Map.of("topicId", topicId)));
        if (signal.flagged()) {
            return ForumOperationResult.denied("moderation:" + signal.reason());
        }
        Post post = new Post(UUID.randomUUID(), topicId, actorId, filterResult.sanitizedContent(), Instant.now());
        repository.appendPost(post);
        return ForumOperationResult.allowed(post);
    }

    public List<Post> listPosts(UUID topicId) {
        return repository.findPosts(topicId);
    }

    public ForumSearchResult search(String query) {
        if (query == null || query.isBlank()) {
            return new ForumSearchResult(List.of(), List.of(), List.of());
        }
        String needle = query.toLowerCase(Locale.ROOT);
        List<Category> categories = repository.listCategories().stream()
                .filter(category -> category.name().toLowerCase(Locale.ROOT).contains(needle)
                        || (category.description() != null && category.description().toLowerCase(Locale.ROOT).contains(needle)))
                .toList();
        List<Topic> topics = repository.listTopicsByKeyword(needle);
        List<Post> posts = repository.listPosts().stream()
                .filter(post -> post.content().toLowerCase(Locale.ROOT).contains(needle))
                .toList();
        return new ForumSearchResult(categories, topics, posts);
    }
}
