package com.hana.bankai.domain.user.service;

import com.hana.bankai.global.common.response.ApiResponse;
import com.hana.bankai.global.error.exception.CustomException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.hana.bankai.global.common.response.SuccessCode.SEND_EMAIL_CODE_SUCCESS;
import static com.hana.bankai.global.error.ErrorCode.SEND_EMAIL_CODE_FAIL;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    private static int number;

    public ApiResponse<String> authenticateEmail(String email) {
        try {
            int codeNum = sendMail(email);
            String code = "" + codeNum;

            return ApiResponse.success(SEND_EMAIL_CODE_SUCCESS, code);
        } catch (Exception e) {
            throw new CustomException(SEND_EMAIL_CODE_FAIL);
        }

    }

    private int sendMail(String email) {
        MimeMessage message = CreateMail(email);
        javaMailSender.send(message);

        return number;
    }

    private MimeMessage CreateMail(String email) {
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("[BankAi] 회원가입 이메일 인증번호");
            String body = "";
            body += "<h3>" + "BankAi 인증번호입니다." + "</h3>";
            body += "<h3>" + "아래 인증번호를 이용하여 인증을 진행해주세요.";
            body += "<h1 style=\"color: #8785F6;\">" + number + "</h1>";
            body += "<h3>" + "감사합니다.";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    private void createNumber() {
        number = (int)(Math.random() * (90000)) + 100000;
    }
}
