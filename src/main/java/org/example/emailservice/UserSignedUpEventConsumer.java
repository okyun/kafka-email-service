package org.example.emailservice;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
public class UserSignedUpEventConsumer {

    private static final String ADMIN_EMAIL = "dhrdbs2980@gmail.com";

    private final EmailLogRepository emailLogRepository;
    private final EmailNotificationService emailNotificationService;

    public UserSignedUpEventConsumer(
            EmailLogRepository emailLogRepository,
            EmailNotificationService emailNotificationService
    ) {
        this.emailLogRepository = emailLogRepository;
        this.emailNotificationService = emailNotificationService;
    }

    @KafkaListener(
            topics = "user.signed-up",//userService에서 보내는 이벤트 listener
            groupId = "email-service", //컨슈머 그룹이름
            concurrency = "3"
    )
    @RetryableTopic(//재시도 설정
            attempts = "5",
            backoff = @Backoff(delay = 1000, multiplier = 2),
            dltTopicSuffix = ".dlt"
            //  topics = "user.signed-up.dlt"로 topics을 보내서 UserSignedUpEventDltConsumer에서 처리한다.
    )
    public void consume(String message) {
        UserSignedUpEvent userSignedUpEvent = UserSignedUpEvent.fromJson(message);

        // 관리자에게 회원가입 알림 발송
        emailNotificationService.sendAdminSignupAlert(ADMIN_EMAIL, userSignedUpEvent);
        String receiverEmail = ADMIN_EMAIL;
        String subject = "[회원가입 알림] " + userSignedUpEvent.getName() + " (" + userSignedUpEvent.getEmail() + ")";

        /// //이메일 로그에 로그를 추가하는 로직
        EmailLog emailLog = new EmailLog(
                userSignedUpEvent.getUserId(),
                receiverEmail,
                subject
        );

        emailLogRepository.save(emailLog);
    }
}
