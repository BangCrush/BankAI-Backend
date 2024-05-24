package com.hana.bankai.domain.accounthistory.repository;

import com.hana.bankai.domain.accounthistory.entity.AccountHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccHisRepository extends JpaRepository<AccountHistory, Long> {
}
