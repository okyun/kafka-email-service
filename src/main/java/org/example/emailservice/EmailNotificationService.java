package org.example.emailservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {

    private final JavaMailSender mailSender;
    private final String fromAddress;

    public EmailNotificationService(
            JavaMailSender mailSender,
            @Value("${email.from:#{null}}") String fromAddress
    ) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    public void sendAdminSignupAlert(String adminEmail, UserSignedUpEvent event) {
        SimpleMailMessage message = new SimpleMailMessage();

        if (fromAddress != null && !fromAddress.isBlank()) {
            message.setFrom(fromAddress);
        }

        message.setTo(adminEmail);
        message.setSubject("[회원가입 알림] " + event.getName() + " (" + event.getEmail() + ")");
        message.setText(buildBody(event));

        mailSender.send(message);
    }

    private static String buildBody(UserSignedUpEvent event) {
        return """
                신규 회원가입이 발생했습니다.

                - userId: %d
                - name: %s
                - email: %s
                """.formatted(event.getUserId(), event.getName(), event.getEmail());
    }
}

