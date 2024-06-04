package com.hana.bankai.domain.admin.service;

import com.hana.bankai.domain.account.repository.AccountRepository;
import com.hana.bankai.domain.product.repsoitory.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;


}
