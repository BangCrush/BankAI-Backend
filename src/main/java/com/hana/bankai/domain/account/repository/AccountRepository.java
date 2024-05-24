package com.hana.bankai.domain.account.repository;

import com.hana.bankai.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    @Query("select a.accBalance from account a where a.accCode = :accCode")
    Optional<Long> findAccBalanceByAccCode(@Param("accCode") String accCode);
}
