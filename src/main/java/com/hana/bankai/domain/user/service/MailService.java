package com.hana.bankai.domain.user.service;

import com.hana.bankai.domain.user.dto.UserRequestDto;
import com.hana.bankai.domain.user.dto.UserResponseDto;
import com.hana.bankai.domain.user.entity.User;
import com.hana.bankai.domain.user.repository.UserRepository;
import com.hana.bankai.global.common.response.ApiResponse;
import com.hana.bankai.global.error.exception.CustomException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.hana.bankai.global.common.response.SuccessCode.*;
import static com.hana.bankai.global.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    /* 회원가입 이메일 인증 */

    private static int authenticateNumber; // 회원가입 인증 번호

    public ApiResponse<String> authenticateEmail(String email) {
        try {
            int codeNum = sendAuthenticateMail(email);
            String code = "" + codeNum;

            return ApiResponse.success(SEND_EMAIL_CODE_SUCCESS, code);
        } catch (Exception e) {
            throw new CustomException(SEND_EMAIL_CODE_FAIL);
        }
    }

    private int sendAuthenticateMail(String email) {
        MimeMessage message = createAuthenticateMail(email);
        javaMailSender.send(message);

        return authenticateNumber;
    }

    private MimeMessage createAuthenticateMail(String email) {
        createNumber();

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("[BankAI] 회원가입 이메일 인증번호");
            String body = "";
            body += "<h3>" + "BankAI 인증번호입니다." + "</h3>";
            body += "<h3>" + "아래 인증번호를 이용하여 인증을 진행해주세요.";
            body += "<h1 style=\"color: #8785F6;\">" + authenticateNumber + "</h1>";
            body += "<h3>" + "감사합니다.";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    private void createNumber() {
        authenticateNumber = (int)(Math.random() * (90000)) + 100000;
    }

    /* 임시 비밀번호 이메일 전송 */

    private static String tempPassword; // 임시 비밀번호
    private final UserRepository userRepository;
    private final PasswordGenerator passwordGenerator;
    private final BCryptPasswordEncoder passwordEncoder;

    public ApiResponse<UserResponseDto.LoginTempPwd> tempPasswordEmail(String email, UserRequestDto.LoginTempPwd request) {
        User user = userRepository.findByUserNameKrAndUserIdAndUserEmail(request.getUserNameKr(), request.getUserId(), request.getUserEmail())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        try {
            String password = sendTempPasswordMail(email);

            // 임시 비밀번호(암호화) DB 저장
            userRepository.updateUserPwd(user.getUserCode(), passwordEncoder.encode(password));

            return ApiResponse.success(SEND_EMAIL_TEMP_PASSWORD_SUCCESS, new UserResponseDto.LoginTempPwd(password));
        } catch (Exception e) {
            e.printStackTrace(); // userRepository.updateUserPwd() 실행 시 발생하는 오류 내용을 확인하기 위해 추가
            throw new CustomException(SEND_EMAIL_TEMP_PASSWORD_FAIL);
        }
    }

    private String sendTempPasswordMail(String email) {
        MimeMessage message = CreateTempPasswordMail(email);
        javaMailSender.send(message);

        return tempPassword;
    }

    private MimeMessage CreateTempPasswordMail(String email) {
        generatePassword();

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("[BankAI] 임시 비밀번호 발급");
            String body = "";
            body += "<h3>" + "BankAI 임시 비밀번호입니다." + "</h3>";
            body += "<h3>" + "비밀번호를 재설정하여 사용해주세요.";
            body += "<h1 style=\"color: #8785F6;\">" + tempPassword + "</h1>";
            body += "<h3>" + "감사합니다.";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    private void generatePassword() {
        tempPassword = passwordGenerator.generatePassword();
    }

}
