package com.demo.forum.plugins.forumbase.service;

import com.demo.forum.plugins.forumbase.model.Category;
import com.demo.forum.plugins.forumbase.model.Post;
import com.demo.forum.plugins.forumbase.model.Topic;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ForumRepository {
    private final Map<UUID, Category> categories = new ConcurrentHashMap<>();
    private final Map<UUID, Topic> topics = new ConcurrentHashMap<>();
    private final Map<UUID, List<Post>> postsByTopic = new ConcurrentHashMap<>();

    public Category saveCategory(Category category) {
        categories.put(category.id(), category);
        return category;
    }

    public Topic saveTopic(Topic topic) {
        topics.put(topic.id(), topic);
        return topic;
    }

    public Post appendPost(Post post) {
        postsByTopic.computeIfAbsent(post.topicId(), ignored -> new CopyOnWriteArrayList<>()).add(post);
        return post;
    }

    public Optional<Category> findCategory(UUID categoryId) {
        return Optional.ofNullable(categories.get(categoryId));
    }

    public Optional<Topic> findTopic(UUID topicId) {
        return Optional.ofNullable(topics.get(topicId));
    }

    public List<Post> findPosts(UUID topicId) {
        return postsByTopic.getOrDefault(topicId, List.of())
                .stream()
                .sorted(Comparator.comparing(Post::createdAt))
                .toList();
    }

    public List<Category> listCategories() {
        return new ArrayList<>(categories.values());
    }

    public List<Topic> listTopics(UUID categoryId) {
        return topics.values()
                .stream()
                .filter(topic -> topic.categoryId().equals(categoryId))
                .sorted(Comparator.comparing(Topic::createdAt))
                .toList();
    }

    public List<Topic> listTopicsByKeyword(String keyword) {
        return topics.values()
                .stream()
                .filter(topic -> topic.title().toLowerCase(java.util.Locale.ROOT).contains(keyword))
                .sorted(Comparator.comparing(Topic::createdAt))
                .toList();
    }

    public List<Post> listPosts() {
        return postsByTopic.values()
                .stream()
                .flatMap(List::stream)
                .sorted(Comparator.comparing(Post::createdAt))
                .toList();
    }
}
