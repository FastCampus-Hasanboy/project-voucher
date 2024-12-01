package org.example.projectvoucher.storage.employee.repository;

import org.example.projectvoucher.storage.employee.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

}
