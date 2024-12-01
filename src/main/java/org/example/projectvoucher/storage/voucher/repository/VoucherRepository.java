package org.example.projectvoucher.storage.voucher.repository;

import org.example.projectvoucher.storage.voucher.entity.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoucherRepository extends JpaRepository<VoucherEntity, Long> {
    Optional<VoucherEntity> findByCode(String code);
}
