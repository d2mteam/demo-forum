package com.demo.forum.infrastructure.repository;

import com.demo.forum.domain.Topic;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, UUID> {
}
