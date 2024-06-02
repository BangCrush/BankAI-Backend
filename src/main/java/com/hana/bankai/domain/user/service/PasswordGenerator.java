package com.hana.bankai.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@RequiredArgsConstructor
@Component
public class PasswordGenerator {

    // 랜덤 비밀번호 생성 메서드
    public String generatePassword() {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialChars = "~!@#$%^&*()+|=";

        String allChars = upperCase + lowerCase + numbers + specialChars;
        Random random = new SecureRandom();
        StringBuilder newPassword = new StringBuilder();

        // 각 종류의 문자를 하나씩 추가
        newPassword.append(upperCase.charAt(random.nextInt(upperCase.length())));
        newPassword.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        newPassword.append(numbers.charAt(random.nextInt(numbers.length())));
        newPassword.append(specialChars.charAt(random.nextInt(specialChars.length())));

        // 나머지 자리를 랜덤하게 채운다
        int remainingLength = 4 + random.nextInt(9); // 최소 4자리 추가, 최대 12자리 추가
        for (int i = 0; i < remainingLength; i++) {
            newPassword.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        // 비밀번호를 섞는다
        char[] passwordArray = newPassword.toString().toCharArray();
        for (int i = 0; i < passwordArray.length; i++) {
            int randomIndex = random.nextInt(passwordArray.length);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[randomIndex];
            passwordArray[randomIndex] = temp;
        }

        return new String(passwordArray);
    }

}
