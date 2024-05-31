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

    @Query("SELECT a FROM account a WHERE a.accCode = :accCode AND a.accPwd = :accPwd")
    Optional<Account> findByAccCodeAndAccPwd(@Param("accCode") String accCode, @Param("accPwd") String accPwd);
    boolean existsByAccCode(String accCode);

    // top3 상품 조회
    @Query("select ap.product.prodCode, COUNT(ap.product.prodCode) as prodcnt from account ap group by ap.product.prodCode order by prodcnt desc limit 3")
    List<Long> getProdTopThree();


}
