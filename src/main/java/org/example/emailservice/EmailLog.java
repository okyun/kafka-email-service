package org.example.emailservice;

import jakarta.persistence.*;

@Entity
@Table(name = "email_logs")
public class EmailLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long receiverUserId;//이메일 받은 사용자 id

    private String receiverEmail; // 수신자 email

    private String subject;//이메일 제목

    public EmailLog() {
    }

    public EmailLog(Long receiverUserId, String receiverEmail, String subject) {
        this.receiverUserId = receiverUserId;
        this.receiverEmail = receiverEmail;
        this.subject = subject;
    }

    // getter 메서드
    public Long getId() {
        return id;
    }

    public Long getReceiverUserId() {
        return receiverUserId;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public String getSubject() {
        return subject;
    }
}
