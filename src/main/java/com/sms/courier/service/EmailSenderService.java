package com.sms.courier.service;

import com.sms.courier.chat.modal.Email;
import org.springframework.mail.javamail.JavaMailSender;

public interface EmailSenderService {

    void sendEmail(JavaMailSender mailSender, Email email);
}
