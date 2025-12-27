package com.demo.forum.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID actorId;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String resourceRef;

    @Column(nullable = false, columnDefinition = "text")
    private String details;

    @Column(nullable = false)
    private Instant at;
}
