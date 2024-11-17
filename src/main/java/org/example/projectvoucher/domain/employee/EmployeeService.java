package org.example.projectvoucher.domain.employee;

import org.example.projectvoucher.app.controller.response.EmployeeResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmployeeService {

    private final Map<Long, EmployeeResponse> employeeResponseMap = new HashMap<>();

    // 사원 생성하기 creating employee
    public Long create(final String name, final String position, final String department) {
        Long no = employeeResponseMap.size() + 1L;
        employeeResponseMap.put(no, new EmployeeResponse(no, name, position, department));
        return no;
    }

    // 사원 조회 searching employee
    public EmployeeResponse get(final Long no) {
        return employeeResponseMap.get(no);
    }
}
