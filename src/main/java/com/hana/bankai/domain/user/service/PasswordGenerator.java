package com.hana.bankai.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@RequiredArgsConstructor
@Component
public class PasswordGenerator {

    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARS = "~!@#$%^&*()+|=";
    private static final String ALL_CHARS = UPPER_CASE + LOWER_CASE + NUMBERS + SPECIAL_CHARS;
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 16;
    private static final Random RANDOM = new SecureRandom();

    // 랜덤 비밀번호 생성 메서드
    public String generatePassword() {
        StringBuilder newPassword = new StringBuilder();

        // 각 종류의 문자를 하나씩 추가
        newPassword.append(UPPER_CASE.charAt(RANDOM.nextInt(UPPER_CASE.length())));
        newPassword.append(LOWER_CASE.charAt(RANDOM.nextInt(LOWER_CASE.length())));
        newPassword.append(NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length())));
        newPassword.append(SPECIAL_CHARS.charAt(RANDOM.nextInt(SPECIAL_CHARS.length())));

        // 나머지 자리를 랜덤하게 채운다
        int remainingLength = MIN_LENGTH + RANDOM.nextInt(MAX_LENGTH - MIN_LENGTH + 1) - 4; // 최소 4자리, 최대 12자리 추가
        for (int i = 0; i < remainingLength; i++) {
            newPassword.append(ALL_CHARS.charAt(RANDOM.nextInt(ALL_CHARS.length())));
        }

        // 비밀번호를 섞는다
        return shuffleString(newPassword.toString());
    }

    // 문자열을 랜덤하게 섞는 메서드
    private String shuffleString(String input) {
        char[] array = input.toCharArray();
        for (int i = array.length - 1; i > 0; i--) {
            int index = RANDOM.nextInt(i + 1);
            char temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }

        return new String(array);
    }
}