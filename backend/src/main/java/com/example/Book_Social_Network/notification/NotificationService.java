package com.example.Book_Social_Network.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final SimpMessagingTemplate MeessagingTemplate;

    public void sendNotification(String receiverId,Notification notification) {
        log.info("Sending notification to user: {} with payload {}", receiverId, notification);
        MeessagingTemplate.convertAndSendToUser(
                receiverId,
                "/queue/notifications",
                notification
        );

    }


}
