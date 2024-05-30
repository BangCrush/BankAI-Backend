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
//    @Query("select p from product p where p.prodType = :prodType")
//    Optional<ProductResponseDto.GetProduct> findProdByProdType(@Param("ProdType") String prodType);
    List<Product> findByProdType(ProdType prodtype);
}
