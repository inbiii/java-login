package com.beobia.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void send(String to, String email){
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Beobia: Confirm Email");
            helper.setFrom("hello@beobia.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e){

            log.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email");

        }
    }

    @Async
    public void sendResetEmail(String to, String email) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Beobia: Reset Password");
            helper.setFrom("hello@beobia.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e){

            log.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email");

        }
    }


    public String buildEmail(String name, String link, String content){
        return "<div> Hello " + name +
                " please click <a href=" + link + "> here </a>  to " + content + " </div>";
    }

    public String buildResetEmail(String link){
        return "<div> please click <a href=" + link + "> here </a>  to reset your password </div>";
    }
}
