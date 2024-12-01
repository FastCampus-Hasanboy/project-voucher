package org.example.projectvoucher.storage.voucher.repository;

import org.example.projectvoucher.storage.voucher.entity.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Long> {
    Optional<ContractEntity> findByCode(String code);
}
