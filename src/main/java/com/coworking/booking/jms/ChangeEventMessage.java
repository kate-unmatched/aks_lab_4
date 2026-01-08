package com.coworking.booking.jms;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeEventMessage {

    private String eventType;    // INSERT / UPDATE / DELETE
    private String entityName;   // имя сущности
    private Long entityId;       // ID записи
    private String payload;      // JSON с данными
    private LocalDateTime occurredAt;
}
