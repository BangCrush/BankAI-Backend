package com.hana.bankai.domain.accounthistory.service;

import com.hana.bankai.domain.account.entity.Account;
import com.hana.bankai.domain.accounthistory.entity.AccountHistory;
import com.hana.bankai.domain.accounthistory.entity.HisType;
import com.hana.bankai.domain.accounthistory.repository.AccHisRepository;
import com.hana.bankai.global.common.enumtype.BankCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccHisService {

    final private AccHisRepository accHisRepository;

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
}
