package com.anton.demo.controller;


import com.anton.demo.dto.EmployeeDto;
import com.anton.demo.dto.ResponseDto;
import com.anton.demo.model.Employee;
import com.anton.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<ResponseDto<EmployeeDto>> createEmployee(@RequestBody EmployeeDto employeeDto){
        ResponseDto responseDto = employeeService.createEmployee(employeeDto);
        responseDto.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<EmployeeDto>>> getEmployee(){
        ResponseDto responseDto = employeeService.getEmployee();
        responseDto.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<EmployeeDto>> getEmployeeById (@PathVariable long id){
        ResponseDto responseDto = employeeService.getEmployeeById(id);
        responseDto.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<EmployeeDto>> updateEmployee (@RequestBody EmployeeDto employee, @PathVariable long id){
        ResponseDto responseDto = employeeService.updateEmployee(employee, id);
        responseDto.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    @GetMapping("/count")
    public ResponseEntity<?> employeeCount(){
        ResponseDto responseDto = employeeService.employeeCount();
        responseDto.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }
}
