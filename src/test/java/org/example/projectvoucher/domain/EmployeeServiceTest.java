package org.example.projectvoucher.domain;

import org.example.projectvoucher.app.controller.response.EmployeeResponse;
import org.example.projectvoucher.domain.employee.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class EmployeeServiceTest {
    @Autowired
    private EmployeeService employeeService;

    @DisplayName("회원 생성후 조회가 가능하다.")
    @Test
    public void test1() {
        // given
        String name = "홍길동";
        String position = "Junior";
        String department = "SpringDeveloper";

        // when
        Long no = employeeService.create(name, position, department);
        EmployeeResponse employeeResponse = employeeService.get(no);

        // then
        assertThat(employeeResponse).isNotNull();
        assertThat(employeeResponse.no()).isEqualTo(no);
        assertThat(employeeResponse.name()).isEqualTo(name);
        assertThat(employeeResponse.position()).isEqualTo(position);
        assertThat(employeeResponse.department()).isEqualTo(department);
        System.out.println(employeeResponse);


    }
}
