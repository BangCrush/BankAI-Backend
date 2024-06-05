package com.hana.bankai.domain.product.repsoitory;

import com.hana.bankai.domain.product.dto.ProductResponseDto;
import com.hana.bankai.domain.product.entity.ProdType;
import com.hana.bankai.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProdType(ProdType prodtype);

    // 상품 가입 top-three 쿼리
    @Query(value = "SELECT p.* " +
            "FROM product p " +
            "Left Join account a ON p.prod_code = a.prod_code " +
            "GROUP BY p.prod_code " +
            "ORDER BY COUNT(a.prod_code) DESC " +
            "LIMIT 3", nativeQuery = true)
    List<Product> findTopProducts();

    Optional<List<Product>> findByProdNameContaining(String keyword);
}
