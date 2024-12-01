package org.example.projectvoucher.domain.service.voucher;

import org.example.projectvoucher.common.dto.RequestContext;
import org.example.projectvoucher.common.type.RequesterType;
import org.example.projectvoucher.common.type.VoucherAmountType;
import org.example.projectvoucher.common.type.VoucherStatusType;
import org.example.projectvoucher.storage.voucher.entity.VoucherEntity;
import org.example.projectvoucher.storage.voucher.repository.VoucherRepository;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@SpringBootTest
public class VoucherServiceV2DynamicTest {
    @Autowired
    private VoucherService voucherService;

    @Autowired
    private VoucherRepository voucherRepository;

    @TestFactory
    Stream<DynamicTest> test() {
        final List<String> codes = new ArrayList<>();
        return Stream.of(
                dynamicTest("상품권을 발행합니다.", () -> {
                    // given
                    final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
                    final LocalDate validFrom = LocalDate.now();
                    final LocalDate validTo = LocalDate.now().plusDays(30);
                    final VoucherAmountType amount = VoucherAmountType.KRW_3000;

                    // when
                    final String code = voucherService.publishV2(requestContext, validFrom, validTo, amount);
                    codes.add(code);

                    // then
                    final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.PUBLISH);

                }),
                dynamicTest("상품권을 사용 불가 처리 합니다.", () -> {

                    // given
                    final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
                    final String code = codes.get(0);

                    // when
                    voucherService.disableV2(requestContext, code);

                    // then
                    final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.DISABLE);

                }),
                dynamicTest("사용 불가 상태의 상품권을 사용할 수 없읍니다.", () -> {
                    //
                    final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
                    final String code = codes.get(0);

                    // when
                    assertThatThrownBy(() ->  voucherService.useV2(requestContext, code))
                            .isInstanceOf(IllegalStateException.class)
                            .hasMessage("사용할 수 없는 상태의 상품권입니다.");

                    // then
                    final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.DISABLE);

                }),
                dynamicTest("상품권을 사용합니다.", () -> {

                    // given
                    final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
                    final LocalDate validFrom = LocalDate.now();
                    final LocalDate validTo = LocalDate.now().plusDays(30);
                    final VoucherAmountType amount = VoucherAmountType.KRW_3000;

                    final String code = voucherService.publishV2(requestContext, validFrom, validTo, amount);
                    codes.add(code);

                    // when
                    voucherService.useV2(requestContext, code);

                    // then
                    final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.USE);
                }),
                dynamicTest("사용한 상품권은 사용 불가 처리 할 수 없다.", () -> {

                    // given
                    final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
                    final String code = codes.get(1);

                    // when
                    assertThatThrownBy(() -> voucherService.disableV2(requestContext, code))
                            .isInstanceOf(IllegalStateException.class)
                            .hasMessage("사용 불가 처리할 수 없는 상태의 상품입니다.");

                    // then
                    final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.USE);

                }),
                dynamicTest("사용한 상품권은 또 사용할 수 없습니다.", () -> {

                    // given
                    final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
                    final String code = codes.get(1);

                    // when
                    assertThatThrownBy(() -> voucherService.useV2(requestContext, code))
                            .isInstanceOf(IllegalStateException.class)
                            .hasMessage("사용할 수 없는 상태의 상품권입니다.");

                    // then
                    final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();
                    assertThat(voucherEntity.getStatus()).isEqualTo(VoucherStatusType.USE);

                })

        );
    }
}
