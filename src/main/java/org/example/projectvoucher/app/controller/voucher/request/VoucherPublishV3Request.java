package org.example.projectvoucher.app.controller.voucher.request;

import org.example.projectvoucher.common.type.RequesterType;
import org.example.projectvoucher.common.type.VoucherAmountType;

public record VoucherPublishV3Request(
        RequesterType requesterType,
        String requesterId,
        String contractCode,
        VoucherAmountType amountType) {

}
