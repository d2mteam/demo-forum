package com.demo.forum.infrastructure;

import com.demo.forum.domain.Post;
import com.demo.forum.domain.PostState;
import com.demo.forum.domain.ReputationStats;
import com.demo.forum.domain.Topic;
import com.demo.forum.domain.TrustLevel;
import com.demo.forum.domain.User;
import com.demo.forum.infrastructure.repository.PostRepository;
import com.demo.forum.infrastructure.repository.TopicRepository;
import com.demo.forum.infrastructure.repository.UserRepository;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@Import(JpaInfrastructureConfig.class)
class EntityPersistenceTest {
    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:15-alpine")
        .withDatabaseName("forum")
        .withUsername("forum")
        .withPassword("forum");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    void createsAndLoadsEntities() {
        User user = User.builder()
            .handle("hello")
            .email("hello@example.com")
            .trustLevel(TrustLevel.TL1)
            .reputationStats(ReputationStats.builder()
                .readTimeMinutes(15)
                .posts(1)
                .likesGiven(1)
                .likesReceived(2)
                .flagsReceived(0)
                .daysVisited(3)
                .build())
            .createdAt(Instant.now())
            .build();
        User savedUser = userRepository.save(user);

        Topic topic = Topic.builder()
            .title("Welcome")
            .authorId(savedUser.getId())
            .category("general")
            .replyCount(0)
            .lastPostAt(Instant.now())
            .build();
        Topic savedTopic = topicRepository.save(topic);

        Post post = Post.builder()
            .topicId(savedTopic.getId())
            .authorId(savedUser.getId())
            .state(PostState.PUBLISHED)
            .content("Hello world")
            .likeCount(0)
            .flagWeight(0)
            .build();
        Post savedPost = postRepository.save(post);

        assertThat(userRepository.findById(savedUser.getId())).isPresent();
        assertThat(topicRepository.findById(savedTopic.getId())).isPresent();
        assertThat(postRepository.findById(savedPost.getId())).isPresent();
        assertThat(postRepository.findById(savedPost.getId()).orElseThrow().getContent())
            .isEqualTo("Hello world");
    }
}
