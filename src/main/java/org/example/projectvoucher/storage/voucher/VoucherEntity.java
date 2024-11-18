package org.example.projectvoucher.storage.voucher;

import jakarta.persistence.*;
import org.example.projectvoucher.common.type.VoucherStatusType;
import org.example.projectvoucher.storage.BaseEntity;

import java.time.LocalDate;

@Entity
@Table(name = "voucher")
public class VoucherEntity extends BaseEntity {
    private String code;
    private VoucherStatusType status;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Long amount;

    public VoucherEntity() {

    }

    public VoucherEntity(String code, VoucherStatusType status, LocalDate validFrom, LocalDate validTo, Long amount) {
        this.code = code;
        this.status = status;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.amount = amount;
    }

}