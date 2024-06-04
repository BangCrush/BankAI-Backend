package com.hana.bankai.domain.account.repository;

import com.hana.bankai.domain.account.entity.Account;
import com.hana.bankai.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    @Query("select a.accBalance from account a where a.accCode = :accCode")
    Optional<Long> findAccBalanceByAccCode(@Param("accCode") String accCode);

    @Query("select a.accPwd from account a where a.accCode = :accCode")
    Optional<String> findAccPwdByAccCode(@Param("accCode") String accCode);

    Optional<Account> findByAccCode(String accCode);

    boolean existsByAccCode(String accCode);

    @Query("SELECT a FROM account a WHERE str(a.product.prodCode) LIKE '1%'")
    List<Account> findByProdCodeStartingWithOne();
}
