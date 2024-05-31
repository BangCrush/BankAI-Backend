package com.hana.bankai.domain.accounthistory.repository;

import com.hana.bankai.domain.accounthistory.entity.AccountHistory;
import com.hana.bankai.global.common.enumtype.BankCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccHisRepository extends JpaRepository<AccountHistory, Long> {

    @Query("SELECT ah FROM account_history ah " +
            "WHERE (ah.inAccCode = :accCode AND ah.inBankCode = :bankCode) " +
            "OR (ah.outAccCode = :accCode AND ah.outBankCode = :bankCode) " +
            "ORDER BY ah.hisDateTime DESC")
    Page<AccountHistory> findByAccCodeAndBankCode(@Param("accCode") String accCode,
                                                  @Param("bankCode") BankCode bankCode,
                                                  Pageable pageable);
}
