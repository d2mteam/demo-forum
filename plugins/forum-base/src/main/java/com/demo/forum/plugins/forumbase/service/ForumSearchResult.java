package com.demo.forum.plugins.forumbase.service;

import com.demo.forum.plugins.forumbase.model.Category;
import com.demo.forum.plugins.forumbase.model.Post;
import com.demo.forum.plugins.forumbase.model.Topic;
import java.util.List;

public record ForumSearchResult(List<Category> categories, List<Topic> topics, List<Post> posts) {
}
