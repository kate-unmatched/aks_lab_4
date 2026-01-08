package com.coworking.booking.jms;

import com.coworking.booking.config.JmsConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChangeEventProducer {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    public void send(ChangeEventMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            jmsTemplate.convertAndSend(JmsConfig.CHANGE_LOG_QUEUE, json);
            log.info("JMS SEND: {}", json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize ChangeEventMessage", e);
        }
    }
}
