package org.example.projectvoucher.domain.service.voucher;

import org.example.projectvoucher.common.dto.RequestContext;
import org.example.projectvoucher.common.type.RequesterType;
import org.example.projectvoucher.common.type.VoucherAmountType;
import org.example.projectvoucher.common.type.VoucherStatusType;
import org.example.projectvoucher.storage.voucher.entity.ContractEntity;
import org.example.projectvoucher.storage.voucher.entity.VoucherEntity;
import org.example.projectvoucher.storage.voucher.entity.VoucherHistoryEntity;
import org.example.projectvoucher.storage.voucher.repository.ContractRepository;
import org.example.projectvoucher.storage.voucher.repository.VoucherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class VoucherServiceV3Test {
    @Autowired
    private VoucherService voucherService;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private ContractRepository contractRepository;

    @DisplayName("유효기간이 지난 계약으로 상품권 발행할 수 없읍니다.")
    @Test
    public void test0() {
        // given
        final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        final VoucherAmountType amount = VoucherAmountType.KRW_3000;

        String contractCode = "CT0010";

        // when
        assertThatThrownBy(() -> voucherService.publishV3(requestContext, contractCode, amount))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("유효기간이 지난 계약입니다.");
    }

    @DisplayName("발행된 상품권은 계약정보의 voucherValidPeriodDayCount 만큼 유효 기간을 가져야 된다.")
    @Test
    public void test1() {
        // given
        final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        final VoucherAmountType amount = VoucherAmountType.KRW_3000;

        String contractCode = "CT001";

        // when
        final String code = voucherService.publishV3(requestContext, contractCode, amount);
        final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();

        final ContractEntity contractEntity = contractRepository.findByCode(contractCode).get();


        // then
        assertThat(voucherEntity.getCode()).isEqualTo(code);
        assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.PUBLISH);
        assertThat(voucherEntity.getValidFrom()).isEqualTo(LocalDate.now());
        assertThat(voucherEntity.getValidTo()).isEqualTo(LocalDate.now().plusDays(contractEntity.getVoucherValidPeriodDayCount()));
        System.out.println("### voucher validTo=" + voucherEntity.getValidTo());
        assertThat(voucherEntity.getAmount()).isEqualTo(amount);

        // history
        final VoucherHistoryEntity voucherHistoryEntity = voucherEntity.getHistories().get(0);
        assertThat(voucherHistoryEntity.getOrderId()).isNotNull();
        assertThat(voucherHistoryEntity.getRequesterType()).isEqualTo(requestContext.requesterType());
        assertThat(voucherHistoryEntity.getStatus()).isEqualTo(VoucherStatusType.PUBLISH);
        assertThat(voucherHistoryEntity.getDescription()).isEqualTo("태스트 발행");
    }
}

