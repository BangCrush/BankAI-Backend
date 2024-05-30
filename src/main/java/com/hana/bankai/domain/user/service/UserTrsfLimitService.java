package com.hana.bankai.domain.user.service;

import com.hana.bankai.domain.user.entity.UserTrsfLimit;
import com.hana.bankai.domain.user.repository.UserTrsfLimitRepository;
import com.hana.bankai.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.hana.bankai.global.error.ErrorCode.USER_TRSF_LIMIT_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class UserTrsfLimitService {

    final private UserTrsfLimitRepository trsfLimitRepository;

    public void accumulate(UUID userCode, Long amount) {
        UserTrsfLimit userTrsfLimit = findUserTrsfLimitByUserCode(userCode);
        userTrsfLimit.accumulate(amount);
    }

    public boolean checkTrsfLimit(UUID userCode, Long amount) {
        UserTrsfLimit userTrsfLimit = findUserTrsfLimitByUserCode(userCode);
        if (userTrsfLimit.getDailyAccAmount() + amount > userTrsfLimit.getDailyLimit()) {
            return true;
        }
        return false;
    }

    public void resetUserDailyTrsfLimit() {
        List<UserTrsfLimit> userTrsfLimitList = trsfLimitRepository.findAll();
        for (UserTrsfLimit userTrsfLimit : userTrsfLimitList) {
            userTrsfLimit.resetDailyAccAmount();
        }
    }

    private UserTrsfLimit findUserTrsfLimitByUserCode(UUID userCode) {
        UserTrsfLimit userTrsfLimit = trsfLimitRepository.findById(userCode)
                .orElseThrow(() -> new CustomException(USER_TRSF_LIMIT_NOT_FOUND));

        return userTrsfLimit;
    }
}