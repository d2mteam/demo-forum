package com.demo.forum.infrastructure.repository;

import com.demo.forum.domain.Post;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, UUID> {
}
