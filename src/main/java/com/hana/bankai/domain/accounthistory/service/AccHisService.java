package com.hana.bankai.domain.accounthistory.service;

import com.hana.bankai.domain.account.dto.AccountRequestDto;
import com.hana.bankai.domain.account.dto.AccountResponseDto;
import com.hana.bankai.domain.account.entity.Account;
import com.hana.bankai.domain.account.repository.AccountRepository;
import com.hana.bankai.domain.accounthistory.entity.AccountHistory;
import com.hana.bankai.domain.accounthistory.entity.HisType;
import com.hana.bankai.domain.accounthistory.repository.AccHisRepository;
import com.hana.bankai.domain.user.entity.User;
import com.hana.bankai.domain.user.repository.UserRepository;
import com.hana.bankai.global.common.enumtype.BankCode;
import com.hana.bankai.global.common.response.ApiResponse;
import com.hana.bankai.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.hana.bankai.global.common.enumtype.BankCode.C04;
import static com.hana.bankai.global.common.response.SuccessCode.ACCOUNT_HISTORY_CHECK_SUCCESS;
import static com.hana.bankai.global.error.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AccHisService {

    private final AccHisRepository accHisRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public void createAccHis(Long hisAmount, HisType hisType, Account inAcc, Account outAcc) {
        AccountHistory accHis = AccountHistory.builder()
                .hisAmount(hisAmount)
                .hisType(hisType)
                .inAccCode(inAcc.getAccCode())
                .inBankCode(BankCode.C04)
                .outAccCode(outAcc.getAccCode())
                .outBankCode(BankCode.C04)
                .beforeInBal(inAcc.getAccBalance() - hisAmount)
                .afterInBal(inAcc.getAccBalance())
                .beforeOutBal(outAcc.getAccBalance() + hisAmount)
                .afterOutBal(outAcc.getAccBalance())
                .build();

        accHisRepository.save(accHis);
    }

    public ApiResponse<List<AccountResponseDto.GetAccHis>> getAccHis(String accCode, String userId, int page) {
        // 예외처리
        Account acc = getAccByAccCode(accCode);
        // 사용자 인증
        User user = getUserByUserId(userId);
        authenticateUser(user, acc.getUser());

        Pageable pageable = PageRequest.of(page - 1, 20);
        Page<AccountHistory> accHistories = accHisRepository.findByAccCodeAndBankCode(accCode, C04, pageable);

        List<AccountResponseDto.GetAccHis> accHisPage = accHistories.map(accHisReq ->
                makeAccHisData(accHisReq, accCode)).getContent();
        return ApiResponse.success(ACCOUNT_HISTORY_CHECK_SUCCESS, accHisPage);
    }

    private AccountResponseDto.GetAccHis makeAccHisData(AccountHistory accHisReq, String accCode) {
        boolean isDeposit = accHisReq.getInAccCode().equals(accCode);
        String targetAccCode = isDeposit ? accHisReq.getOutAccCode() : accHisReq.getInAccCode();
        Account targetAcc = getAccByAccCode(targetAccCode);
        User targetUser = getUserByUserId(targetAcc.getUser().getUserId());
        // 이자 지급인지 확인
        String target = targetUser.getUserNameKr().equals("관리자") ? "입출금통장 이자" : targetUser.getUserNameKr();

        Long hisAmount = isDeposit ? accHisReq.getHisAmount() : -accHisReq.getHisAmount();
        Long balance = isDeposit ? accHisReq.getAfterInBal() : accHisReq.getAfterOutBal();

        return AccountResponseDto.GetAccHis.of(accHisReq, target, hisAmount, balance);
    }

    private Account getAccByAccCode(String accCode) {
        return accountRepository.findById(accCode)
                .orElseThrow(() -> new CustomException(ACCOUNT_NOT_FOUND));
    }

    private User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    private void authenticateUser(User loginUser, User accUser) {
        if (!loginUser.equals(accUser)) {
            throw new CustomException(USER_AUTHENTICATION_FAIL);
        }
    }
}
