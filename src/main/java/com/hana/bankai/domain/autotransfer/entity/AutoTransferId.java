package com.hana.bankai.domain.autotransfer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Getter
public class AutoTransferId implements Serializable {

    @Column(name = "at_date")
    private int atDate;

    @Column(name = "in_acc_code")
    private String inAccCode;

    @Column(name = "out_acc_code")
    private String outAccCode;
}
