package com.anton.demo.service;

import com.anton.demo.dto.ResponseDto;
import com.anton.demo.dto.EmployeeDto;
import com.anton.demo.model.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author by nadeeshan_fdz
 */
public interface EmployeeService {
    ResponseDto<EmployeeDto> createEmployee(EmployeeDto employee);

    ResponseDto<List<EmployeeDto>> getEmployee();

    ResponseDto<EmployeeDto> getEmployeeById(Long id);

    ResponseDto<EmployeeDto> updateEmployee(EmployeeDto employee, long id);

    ResponseDto<?> employeeCount();
}
