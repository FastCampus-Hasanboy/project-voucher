package org.example.projectvoucher.domain.service.voucher;

import jakarta.transaction.Transactional;
import org.example.projectvoucher.common.dto.RequestContext;
import org.example.projectvoucher.common.type.VoucherAmountType;
import org.example.projectvoucher.common.type.VoucherStatusType;
import org.example.projectvoucher.storage.voucher.entity.ContractEntity;
import org.example.projectvoucher.storage.voucher.entity.VoucherEntity;
import org.example.projectvoucher.storage.voucher.entity.VoucherHistoryEntity;
import org.example.projectvoucher.storage.voucher.repository.ContractRepository;
import org.example.projectvoucher.storage.voucher.repository.VoucherRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class VoucherService {
    private final VoucherRepository voucherRepository;
    private final ContractRepository contractRepository;


    public VoucherService(VoucherRepository voucherRepository, ContractRepository contractRepository) {
        this.voucherRepository = voucherRepository;
        this.contractRepository = contractRepository;
    }

    // 상품권 발행 v1
    @Transactional
    public String publish(final LocalDate validFrom, final LocalDate validTo, final VoucherAmountType amount) {
        final String code = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        final VoucherEntity voucherEntity = new VoucherEntity(code, VoucherStatusType.PUBLISH,  amount, null, null);
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
        final VoucherEntity voucherEntity = new VoucherEntity(code, VoucherStatusType.PUBLISH, amount,  voucherHistoryEntity, null);
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

    // 상품권 발행 v3
    @Transactional
    public String publishV3(final RequestContext requestContext, final String contractCode, final VoucherAmountType amount) {
        final String code = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        final String orderId = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");

        final ContractEntity contractEntity = contractRepository.findByCode(contractCode).orElseThrow(() -> new IllegalArgumentException("존재하지 않은 계약입니다."));

        final VoucherHistoryEntity voucherHistoryEntity = new VoucherHistoryEntity(orderId, requestContext.requesterType(), requestContext.requesterId(), VoucherStatusType.PUBLISH, "태스트 발행");
        final VoucherEntity voucherEntity = new VoucherEntity(code, VoucherStatusType.PUBLISH, amount, voucherHistoryEntity, contractEntity);

        return voucherRepository.save(voucherEntity).getCode();
    }


}
