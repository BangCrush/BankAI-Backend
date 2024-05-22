package com.hana.bankai.domain.autotransfer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class AutoTransferId implements Serializable {

    @Column(name = "at_date")
    private LocalDate atDate;

    @Column(name = "in_acc_code")
    private String inAccCode;

    @Column(name = "out_acc_code")
    private String outAccCode;
}
