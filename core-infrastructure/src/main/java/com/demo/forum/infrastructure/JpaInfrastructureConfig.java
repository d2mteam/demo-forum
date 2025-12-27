package com.demo.forum.infrastructure;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "com.demo.forum.domain")
@EnableJpaRepositories(basePackages = "com.demo.forum.infrastructure.repository")
public class JpaInfrastructureConfig {
}
