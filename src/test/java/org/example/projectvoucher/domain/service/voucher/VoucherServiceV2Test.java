package org.example.projectvoucher.domain.service.voucher;

import org.example.projectvoucher.common.dto.RequestContext;
import org.example.projectvoucher.common.type.RequesterType;
import org.example.projectvoucher.common.type.VoucherAmountType;
import org.example.projectvoucher.common.type.VoucherStatusType;
import org.example.projectvoucher.storage.voucher.entity.VoucherEntity;
import org.example.projectvoucher.storage.voucher.entity.VoucherHistoryEntity;
import org.example.projectvoucher.storage.voucher.repository.VoucherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class VoucherServiceV2Test {
    @Autowired
    private VoucherService voucherService;

    @Autowired
    private VoucherRepository voucherRepository;

    @DisplayName("발행된 상품권은 code 로 조회할 수 있어야 된다.")
    @Test
    public void test1() {
        // given
        final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        final LocalDate validFrom = LocalDate.now();
        final LocalDate validTo = LocalDate.now().plusDays(30);
        final VoucherAmountType amount = VoucherAmountType.KRW_3000;
        final String code = voucherService.publishV2(requestContext, validFrom, validTo, amount);

        // when
        final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();

        // then
        assertThat(voucherEntity.getCode()).isEqualTo(code);
        assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.PUBLISH);
        assertThat(voucherEntity.getValidFrom()).isEqualTo(validFrom);
        assertThat(voucherEntity.getValidTo()).isEqualTo(validTo);
        assertThat(voucherEntity.getAmount()).isEqualTo(amount);

        // history
        final VoucherHistoryEntity voucherHistoryEntity = voucherEntity.getHistories().get(0);
        assertThat(voucherHistoryEntity.getOrderId()).isNotNull();
        assertThat(voucherHistoryEntity.getRequesterType()).isEqualTo(requestContext.requesterType());
        assertThat(voucherHistoryEntity.getStatus()).isEqualTo(VoucherStatusType.PUBLISH);
        assertThat(voucherHistoryEntity.getDescription()).isEqualTo("태스트 발행");
    }

    @DisplayName("발행된 상품권은 사용 불가 처리 할 수 있다.")
    @Test
    public void test2() {
        // given
        final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        final LocalDate validFrom = LocalDate.now();
        final LocalDate validTo = LocalDate.now().plusDays(30);
        final VoucherAmountType amount = VoucherAmountType.KRW_3000;
        final String code = voucherService.publishV2(requestContext, validFrom, validTo, amount);

        final RequestContext disableRequestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());


        // when
        voucherService.disableV2(disableRequestContext, code);
        final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();


        // then
        assertThat(voucherEntity.getCode()).isEqualTo(code);
        assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.DISABLE);
        assertThat(voucherEntity.getValidFrom()).isEqualTo(validFrom);
        assertThat(voucherEntity.getValidTo()).isEqualTo(validTo);
        assertThat(voucherEntity.getAmount()).isEqualTo(amount);

        // createAt va updateAt qiymatlarni sekundlarga qisqartirish orqali taqqoslash
        assertThat(voucherEntity.updateAt().truncatedTo(ChronoUnit.SECONDS))
                .isEqualTo(voucherEntity.createAt().truncatedTo(ChronoUnit.SECONDS));

        // Tekshiruv maqsadida qiymatlarni konsolga chiqarish
        System.out.println("-------> voucherEntity.createAt() = " + voucherEntity.createAt());
        System.out.println("-------> voucherEntity.updateAt() = " + voucherEntity.updateAt());

        // history
        final VoucherHistoryEntity voucherHistoryEntity = voucherEntity.getHistories().get(voucherEntity.getHistories().size() -1);
        assertThat(voucherHistoryEntity.getOrderId()).isNotNull();
        assertThat(voucherHistoryEntity.getRequesterType()).isEqualTo(disableRequestContext.requesterType());
        assertThat(voucherHistoryEntity.getRequesterId()).isEqualTo(disableRequestContext.requesterId());
        assertThat(voucherHistoryEntity.getStatus()).isEqualTo(VoucherStatusType.DISABLE);
        assertThat(voucherHistoryEntity.getDescription()).isEqualTo("태스트 사용 불가");

    }

    @DisplayName("발행된 상품권은 사용할 수 있다.")
    @Test
    public void test3() {
        // given
        final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        final LocalDate validFrom = LocalDate.now();
        final LocalDate validTo = LocalDate.now().plusDays(30);
        final VoucherAmountType amount = VoucherAmountType.KRW_3000;

        final String code = voucherService.publishV2(requestContext, validFrom, validTo, amount);

        final RequestContext useRequestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());


        // when
        voucherService.useV2(useRequestContext, code);
        final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();

        // then
        assertThat(voucherEntity.getCode()).isEqualTo(code);
        assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.USE);
        assertThat(voucherEntity.getValidFrom()).isEqualTo(validFrom);
        assertThat(voucherEntity.getValidTo()).isEqualTo(validTo);
        assertThat(voucherEntity.getAmount()).isEqualTo(amount);
        assertThat(voucherEntity.createAt()).isNotEqualTo(voucherEntity.updateAt());

        System.out.println("-----> voucherEntity.createAt() = " + voucherEntity.createAt());
        System.out.println("-----> voucherEntity.updateAt() = " + voucherEntity.updateAt());

        // history
        final VoucherHistoryEntity voucherHistoryEntity = voucherEntity.getHistories().get(voucherEntity.getHistories().size() -1);
        assertThat(voucherHistoryEntity.getOrderId()).isNotNull();
        assertThat(voucherHistoryEntity.getRequesterType()).isEqualTo(useRequestContext.requesterType());
        assertThat(voucherHistoryEntity.getRequesterId()).isEqualTo(useRequestContext.requesterId());
        assertThat(voucherHistoryEntity.getStatus()).isEqualTo(VoucherStatusType.USE);
        assertThat(voucherHistoryEntity.getDescription()).isEqualTo("태스트 사용");
    }
}

