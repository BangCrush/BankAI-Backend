package com.hana.bankai.domain.accounthistory.repository;

import com.hana.bankai.domain.accounthistory.entity.AccountHistory;
import com.hana.bankai.global.common.enumtype.BankCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccHisRepository extends JpaRepository<AccountHistory, Long> {

    List<AccountHistory> findByInAccCodeAndInBankCode(String inAccCode, BankCode inBankCode);
    List<AccountHistory> findByOutAccCodeAndOutBankCode(String outAccCode, BankCode outBankCode);
}
