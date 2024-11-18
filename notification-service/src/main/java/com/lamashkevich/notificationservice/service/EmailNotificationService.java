package com.lamashkevich.notificationservice.service;

import com.lamashkevich.notificationservice.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {

    private final JavaMailSender mailSender;

    @Override
    @KafkaListener(topics = "order-placed")
    public void sendNotification(OrderPlacedEvent event) {
        log.info("Order placed: {}", event);

        var messagePreparator = createMimeMessagePreparator(event);

        try {
            mailSender.send(messagePreparator);
            log.info("Order message sent!");
        } catch (MailException e) {
            log.error(e.getMessage());
        }

    }

    private MimeMessagePreparator createMimeMessagePreparator(OrderPlacedEvent event) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(event.getEmail());
            messageHelper.setSubject("Order %s placed".formatted(event.getOrderId()));
            messageHelper.setText(String.format("""
                            Hi, %s!

                            Your order %s placed successfully.
                            
                            Auto-parts-store
                            """,
                    event.getName(),
                    event.getOrderId()));
        };
    }
}
