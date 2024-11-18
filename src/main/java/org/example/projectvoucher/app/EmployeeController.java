package org.example.projectvoucher.app;

import org.example.projectvoucher.app.controller.request.EmployeeCreateRequest;
import org.example.projectvoucher.app.controller.response.EmployeeResponse;
import org.example.projectvoucher.domain.employee.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // 사원 생성하기 creating employee
    @PostMapping("/api/v1/employee")
    public Long create(@RequestBody final EmployeeCreateRequest request) {
        return employeeService.create(request.name(), request.position(), request.department());

    }

    // 사원 조회 searching employee
    @GetMapping("/api/v1/employee/{no}")
    public EmployeeResponse get(@PathVariable(value = "no") final Long no) {
        return employeeService.get(no);
    }

}
