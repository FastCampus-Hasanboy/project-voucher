package org.example.projectvoucher.domain.service.voucher;

import jakarta.transaction.Transactional;
import org.example.projectvoucher.common.dto.RequestContext;
import org.example.projectvoucher.common.type.VoucherAmountType;
import org.example.projectvoucher.common.type.VoucherStatusType;
import org.example.projectvoucher.storage.voucher.VoucherEntity;
import org.example.projectvoucher.storage.voucher.VoucherHistoryEntity;
import org.example.projectvoucher.storage.voucher.VoucherRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class VoucherService {
    private final VoucherRepository voucherRepository;

    public VoucherService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    // 상품권 발행 v1
    @Transactional
    public String publish(final LocalDate validFrom, final LocalDate validTo, final VoucherAmountType amount) {
        final String code = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        final VoucherEntity voucherEntity = new VoucherEntity(code, VoucherStatusType.PUBLISH, validFrom, validTo, amount, null);
        return voucherRepository.save(voucherEntity).getCode();
    }

    // 상품권 사용 불가 처리 v1
    @Transactional
    public void disable(String code) {
        final VoucherEntity voucherEntity = voucherRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 상품권입니다."));

        voucherEntity.disabel(null);
    }

    // 상품권 사용 v1
    @Transactional
    public void use(String code) {
        final VoucherEntity voucherEntity = voucherRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 상품권입니다."));

        voucherEntity.use(null);
    }



    // 상품권 발행 v2
    @Transactional
    public String publishV2(final RequestContext requestContext, final LocalDate validFrom, final LocalDate validTo, final VoucherAmountType amount) {
        final String code = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        final String orderId = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");

        final VoucherHistoryEntity voucherHistoryEntity = new VoucherHistoryEntity(orderId, requestContext.requesterType(), requestContext.requesterId(), VoucherStatusType.PUBLISH, "태스트 발행");
        final VoucherEntity voucherEntity = new VoucherEntity(code, VoucherStatusType.PUBLISH, validFrom, validTo, amount, voucherHistoryEntity);
        return voucherRepository.save(voucherEntity).getCode();
    }

    // 상품권 사용 불가 처리 v2
    @Transactional
    public void disableV2(final RequestContext requestContext, final String code) {
        final String orderId = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");

        final VoucherEntity voucherEntity = voucherRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 상품권입니다."));
        final VoucherHistoryEntity voucherHistoryEntity = new VoucherHistoryEntity(orderId, requestContext.requesterType(), requestContext.requesterId(), VoucherStatusType.DISABLE, "태스트 사용 불가");

        voucherEntity.disabel(voucherHistoryEntity);
    }

    // 상품권 사용 v2
    @Transactional
    public void useV2(final RequestContext requestContext, final String code) {
        final String orderId = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");

        final VoucherEntity voucherEntity = voucherRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 상품권입니다."));
        final VoucherHistoryEntity voucherHistoryEntity = new VoucherHistoryEntity(orderId, requestContext.requesterType(), requestContext.requesterId(), VoucherStatusType.USE, "태스트 사용");


        voucherEntity.use(voucherHistoryEntity);
    }
}
