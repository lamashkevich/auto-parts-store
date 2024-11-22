package com.lamashkevich.notificationservice.service;

import com.lamashkevich.notificationservice.event.OrderPlacedEvent;

public interface NotificationService {
    void sendNotification(OrderPlacedEvent event);
}
