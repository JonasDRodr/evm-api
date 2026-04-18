package com.trycore.evm.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal bac;

    @Column(name = "planned_progress", nullable = false)
    private BigDecimal plannedProgress;

    @Column(name = "actual_progress", nullable = false)
    private BigDecimal actualProgress;

    @Column(name = "actual_cost", nullable = false)
    private BigDecimal actualCost;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}