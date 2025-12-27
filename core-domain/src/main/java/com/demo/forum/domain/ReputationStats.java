package com.demo.forum.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReputationStats {
    private long readTimeMinutes;
    private int posts;
    private int likesGiven;
    private int likesReceived;
    private int flagsReceived;
    private int daysVisited;
}
