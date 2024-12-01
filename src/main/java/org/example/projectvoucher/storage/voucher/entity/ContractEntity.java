package org.example.projectvoucher.storage.voucher.entity;

import jakarta.persistence.*;
import org.example.projectvoucher.storage.BaseEntity;
import java.time.LocalDate;


@Entity
@Table(name = "contract")
public class ContractEntity extends BaseEntity {
    private String code; // 계약의 고유 코드
    private LocalDate validFrom; // 계약의 유효기간 시작일
    private LocalDate validTo; // 계약의 유효기간 종료일
    private Integer voucherValidPeriodDayCount; // 상품권 유효기간 일자

    // 2/22 일에 상품권을 발행할 수 있을까? (1/10 ~ 3/10)
    // voucherValidPeriodDayCount; // 상품권 유효기간 일자
    // 2/22 일에 발행한 상품권은 언제까지 쓸 수 있는가?

    public ContractEntity() {
    }

    public ContractEntity(String code, LocalDate validFrom, LocalDate validTo, Integer voucherValidPeriodDayCount) {
        this.code = code;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.voucherValidPeriodDayCount = voucherValidPeriodDayCount;

    }

    public Boolean isExpired() {
        return LocalDate.now().isAfter(validTo);
    }

    public String getCode() {
        return code;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public Integer getVoucherValidPeriodDayCount() {
        return voucherValidPeriodDayCount;
    }
}
