package com.hana.bankai.domain.product.entity;

public enum ProdRepay {
    DEFERRED,          // 거치식상환
    BULLET,            // 만기일시상환
    EQUAL_INSTALLMENT, // 원리금균등상환
    EQUAL_PRINCIPAL    // 원금균등분할상환
}
