package org.example.projectvoucher.common.type;

import javax.naming.ldap.PagedResultsControl;

public enum VoucherAmountType {
    KRW_3000(30_000L),
    KRW_5000(50_000L),
    KRW_100000(100_000L),
    ;

    private final Long amount;

    VoucherAmountType(Long amount) {
        this.amount = amount;
    }

    public Long getAmount() {
        return amount;
    }
}
