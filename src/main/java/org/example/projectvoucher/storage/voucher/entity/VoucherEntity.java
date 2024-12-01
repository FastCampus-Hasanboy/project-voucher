package org.example.projectvoucher.storage.voucher.entity;

import jakarta.persistence.*;
import org.example.projectvoucher.common.type.VoucherAmountType;
import org.example.projectvoucher.common.type.VoucherStatusType;
import org.example.projectvoucher.storage.BaseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "voucher")
public class VoucherEntity extends BaseEntity {
    private String code;
    @Enumerated(EnumType.STRING)
    private VoucherStatusType status;
    private LocalDate validFrom;
    private LocalDate validTo;
    @Enumerated(EnumType.STRING)
    private VoucherAmountType amount;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "voucher_id")
    private List<VoucherHistoryEntity> histories = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "contract_id")
    private ContractEntity contract;

    public VoucherEntity() {

    }

    public VoucherEntity(String code, VoucherStatusType status, VoucherAmountType amount, VoucherHistoryEntity voucherHistoryEntity, ContractEntity contractEntity) {
        this.code = code;
        this.status = status;
        this.validFrom = LocalDate.now();
        this.validTo = LocalDate.now().plusDays(contractEntity.getVoucherValidPeriodDayCount());
        this.amount = amount;

        this.histories.add(voucherHistoryEntity);
        this.contract = contractEntity;
    }

    public String getCode() {
        return code;
    }

    public VoucherStatusType getStatus() {
        return status;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public VoucherAmountType getAmount() {
        return amount;
    }

    public List<VoucherHistoryEntity> getHistories() {
        return histories;
    }

    public void disabel(final VoucherHistoryEntity voucherHistoryEntity) {
        if (!this.status.equals(VoucherStatusType.PUBLISH)) {
            throw new IllegalStateException("사용 불가 처리할 수 없는 상태의 상품입니다.");
        }
        this.status = VoucherStatusType.DISABLE;
        this.histories.add(voucherHistoryEntity);
    }

    public void use(final VoucherHistoryEntity voucherHistoryEntity) {
        if (!this.status.equals(VoucherStatusType.PUBLISH)) {
            throw new IllegalStateException("사용할 수 없는 상태의 상품권입니다.");
        }

        this.status = VoucherStatusType.USE;
        this.histories.add(voucherHistoryEntity);
    }
}
