package com.coworking.booking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "change_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type", nullable = false, length = 32)
    private String eventType;

    @Column(name = "entity_name", nullable = false, length = 128)
    private String entityName;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(
        name = "change_data",
        columnDefinition = "TEXT"
    )
    private String changeData;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
