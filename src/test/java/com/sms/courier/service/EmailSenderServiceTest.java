//package com.sms.courier.service;
//
//import com.sms.courier.chat.modal.Email;
//import com.sms.courier.chat.sender.Sender;
//import com.sms.courier.service.impl.EmailSenderServiceImpl;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.mail.javamail.JavaMailSender;
//
//import javax.mail.internet.MimeMessage;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@DisplayName("Tests for EmailSenderService")
//public class EmailSenderServiceTest {
//
//    private final Sender<JavaMailSender> sender = mock(Sender.class);
//    private final JavaMailSender javaMailSender = mock(JavaMailSender.class);
//    private final MimeMessage mimeMessage = mock(MimeMessage.class);
//    private final EmailSenderService emailSenderService = new EmailSenderServiceImpl(sender);
//
//    @Test
//    @DisplayName("Test the sendEmail method in EmailSenderService")
//    void send_test() {
//        Email email = mock(Email.class);
//        when(email.getContent()).thenReturn("content");
//        when(email.getSubject()).thenReturn("subject");
//        when(email.getFrom()).thenReturn("from");
//        when(sender.get()).thenReturn(Optional.of(javaMailSender));
//        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
//        Map<String, String> attachments = new HashMap<>();
//        attachments.put("name", "file");
//        when(email.getAttachments()).thenReturn(attachments);
//        Map<String, String> inlines = new HashMap<>();
//        inlines.put("id", "resource");
//        when(email.getInlines()).thenReturn(inlines);
//        emailSenderService.sendEmail(email);
//        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
//    }
//
//    @Test
//    @DisplayName("An exception occurred while executing the send method")
//    void send_email_exception_test() {
//        Email email = mock(Email.class);
//        when(email.getContent()).thenReturn("content");
//        when(email.getSubject()).thenReturn("subject");
//        when(email.getFrom()).thenReturn("from");
//        when(sender.get()).thenReturn(Optional.of(javaMailSender));
//        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
//        doThrow(RuntimeException.class).when(email).getAttachments();
//        emailSenderService.sendEmail(email);
//        verify(javaMailSender, times(0)).send(any(MimeMessage.class));
//    }
//}
