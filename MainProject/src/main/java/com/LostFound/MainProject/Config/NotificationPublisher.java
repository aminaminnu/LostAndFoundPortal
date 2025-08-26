package com.LostFound.MainProject.Config;

import java.time.Instant;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.LostFound.MainProject.Dto.NotificationPayload;

@Component
public class NotificationPublisher {
	private final SimpMessagingTemplate template;

    public NotificationPublisher(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void pushToUser(Long userId, String title, String message) {
        template.convertAndSend(
            "/topic/user/" + userId,
            new NotificationPayload(userId, title, message, Instant.now().toString())
        );
    }

}
