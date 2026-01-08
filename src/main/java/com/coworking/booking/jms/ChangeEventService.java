package com.coworking.booking.jms;

import com.coworking.booking.utils.EntitySnapshotUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChangeEventService {

    private final ChangeEventProducer producer;
    private final ObjectMapper objectMapper;

    public void sendInsert(String entityName, Object entity, Long entityId) {
        send("INSERT", entityName, entity, entityId);
    }

    public void sendUpdate(String entityName, Object entity, Long entityId) {
        send("UPDATE", entityName, entity, entityId);
    }

    public void sendDelete(String entityName, Long entityId) {
        ChangeEventMessage message = ChangeEventMessage.builder()
                .eventType("DELETE")
                .entityName(entityName)
                .entityId(entityId)
                .payload(null)
                .occurredAt(LocalDateTime.now())
                .build();

        producer.send(message);
    }

    private void send(String eventType, String entityName, Object entity, Long entityId) {
        try {
            Map<String, Object> snapshot = EntitySnapshotUtil.extract(entity);
            String payload = objectMapper.writeValueAsString(snapshot);

            ChangeEventMessage message = ChangeEventMessage.builder()
                    .eventType(eventType)
                    .entityName(entityName)
                    .entityId(entityId)
                    .payload(payload)
                    .occurredAt(LocalDateTime.now())
                    .build();

            producer.send(message);

        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize change event", e);
        }
    }
}
