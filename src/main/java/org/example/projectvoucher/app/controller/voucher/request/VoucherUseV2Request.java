package org.example.projectvoucher.app.controller.voucher.request;

import org.example.projectvoucher.common.type.RequesterType;

public record VoucherUseV2Request(
        RequesterType requesterType,
        String requesterId,
        String code) {
}
