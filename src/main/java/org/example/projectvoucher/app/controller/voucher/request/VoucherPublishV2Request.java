package org.example.projectvoucher.app.controller.voucher.request;

import org.example.projectvoucher.common.type.RequesterType;
import org.example.projectvoucher.common.type.VoucherAmountType;

public record VoucherPublishV2Request(
        RequesterType requesterType,
        String requesterId,
        VoucherAmountType amountType) {

}
