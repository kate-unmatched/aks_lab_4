package com.coworking.booking.jms;

import com.coworking.booking.config.JmsConfig;
import com.coworking.booking.entity.ChangeLog;
import com.coworking.booking.repository.ChangeLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChangeLogListener {

    private final ObjectMapper objectMapper;
    private final ChangeLogRepository changeLogRepository;

    @JmsListener(destination = JmsConfig.CHANGE_LOG_QUEUE)
    public void onMessage(String messageJson) {

        try {
            ChangeEventMessage event =
                    objectMapper.readValue(messageJson, ChangeEventMessage.class);

            ChangeLog logEntry = ChangeLog.builder()
                    .eventType(event.getEventType())
                    .entityName(event.getEntityName())
                    .entityId(event.getEntityId())
                    .changeData(event.getPayload())
                    .createdAt(
                            event.getOccurredAt() != null
                                    ? event.getOccurredAt()
                                    : LocalDateTime.now()
                    )
                    .build();

            changeLogRepository.save(logEntry);

            log.info(
                    "Change logged: {} {} (id={})",
                    event.getEventType(),
                    event.getEntityName(),
                    event.getEntityId()
            );

        } catch (Exception e) {
            log.error("Failed to process JMS message: {}", messageJson, e);
        }
    }
}
