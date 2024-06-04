package com.hana.bankai.domain.admin.entity;

import com.hana.bankai.domain.account.entity.Account;
import com.hana.bankai.domain.product.entity.Product;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class JoinCnt {
    private int ageGroup20;
    private int ageGroup30;
    private int ageGroup40;
    private int ageGroup50;
    private int ageGroup60;
    private int ageGroup70;
    private int ageGroup80;

    // Product Entity를 인자로 받는 생성자
    public JoinCnt(Product product) {
        List<Account> accountList = product.getAccountList();

        for(Account account : accountList) {
            int age = getAgeFromResidentNumber(account.getUser().getUserInherentNumber());

            if(age < 30) {
                ageGroup20++;
            } else if(age < 40) {
                ageGroup30++;
            } else if(age < 50) {
                ageGroup40++;
            } else if(age < 60) {
                ageGroup50++;
            } else if(age < 70) {
                ageGroup60++;
            } else if(age < 80) {
                ageGroup70++;
            } else {
                ageGroup80++;
            }
        }
    }

    private static int getAgeFromResidentNumber(String residentNumber) {
        // 주민등록번호의 앞 두 자리와 뒷자리 첫 숫자를 추출
        String yearPrefix = residentNumber.substring(0, 2);
        char genderDigit = residentNumber.charAt(7);

        // 현재 연도를 가져옴
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();

        // 출생연도 계산
        int birthYear = getFullYear(yearPrefix, genderDigit);

        // 나이 계산
        int age = currentYear - birthYear;

        // 생일이 지났는지 확인하여 조정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate birthDate = LocalDate.parse(birthYear + residentNumber.substring(2, 6), formatter);
        if (currentDate.isBefore(birthDate.withYear(currentYear))) {
            age--;
        }

        return age;
    }

    private static int getFullYear(String yearPrefix, char genderDigit) {
        int year = Integer.parseInt(yearPrefix);
        switch (genderDigit) {
            case '1': case '2': case '5': case '6':
                return 1900 + year;
            case '3': case '4': case '7': case '8':
                return 2000 + year;
            default:
                throw new IllegalArgumentException("Invalid resident number format");
        }
    }
}
