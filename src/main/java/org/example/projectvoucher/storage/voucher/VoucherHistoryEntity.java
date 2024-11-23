package org.example.projectvoucher.storage.voucher;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import org.example.projectvoucher.common.type.RequesterType;
import org.example.projectvoucher.common.type.VoucherAmountType;
import org.example.projectvoucher.common.type.VoucherStatusType;
import org.example.projectvoucher.storage.BaseEntity;

import java.time.LocalDate;

@Entity
@Table(name = "voucher_history")
public class VoucherHistoryEntity extends BaseEntity {
    private String orderId;
    @Enumerated(EnumType.STRING)
    private RequesterType requesterType;
    private String requesterId;
    @Enumerated(EnumType.STRING)
    private VoucherStatusType status;
    private String description;

    public VoucherHistoryEntity() {
    }

    public VoucherHistoryEntity(String orderId, RequesterType requesterType, String requesterId, VoucherStatusType status, String description) {
        this.orderId = orderId;
        this.requesterType = requesterType;
        this.requesterId = requesterId;
        this.status = status;
        this.description = description;
    }

    public String getOrderId() {
        return orderId;
    }

    public RequesterType getRequesterType() {
        return requesterType;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public VoucherStatusType getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
