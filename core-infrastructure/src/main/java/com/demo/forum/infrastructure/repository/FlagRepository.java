package com.demo.forum.infrastructure.repository;

import com.demo.forum.domain.Flag;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlagRepository extends JpaRepository<Flag, UUID> {
}
