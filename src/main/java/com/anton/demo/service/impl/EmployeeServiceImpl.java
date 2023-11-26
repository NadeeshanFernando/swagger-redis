package com.anton.demo.service.impl;

import com.anton.demo.dto.ContactDto;
import com.anton.demo.dto.EmployeeDto;
import com.anton.demo.dto.ResponseDto;
import com.anton.demo.model.Contact;
import com.anton.demo.model.Employee;
import com.anton.demo.repo.ContactRepository;
import com.anton.demo.repo.EmployeeRepository;
import com.anton.demo.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author by nadeeshan_fdz
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "employeeCount", allEntries = true),
                    @CacheEvict(value = "employeeList", allEntries = true),
                    @CacheEvict(value = "employeeById", allEntries = true)
            },
            put   = {
                    @CachePut(value = "employeeCount"),
                    @CachePut(value = "employeeList"),
                    @CachePut(value = "employeeById")
            }
    )
    public ResponseDto<EmployeeDto> createEmployee(EmployeeDto employeeDto) {
        ResponseDto responseDto = new ResponseDto<>();

        for(ContactDto c : employeeDto.getContactList()) {
            if (contactRepository.existsByMobile(c.getMobile())) {
                responseDto.setMessage("Contact number " + c.getMobile() + " already exist");
                responseDto.setStatus(HttpStatus.CONFLICT.value());
                responseDto.setData(null);
                responseDto.setTimestamp(LocalDateTime.now());

                return responseDto;
            }
        }

        boolean uniqueEmployee = !employeeRepository.existsByFirstNameAndLastName(employeeDto.getFirstName(), employeeDto.getLastName());

        if (uniqueEmployee) {
            Employee employee = new Employee();
            employee.setFirstName(employeeDto.getFirstName());
            employee.setLastName(employeeDto.getLastName());
            employee.setEmpSalary(employeeDto.getEmpSalary());

            for(ContactDto c : employeeDto.getContactList()){
                Contact contact = new Contact();
                contact.setEmployee(employee);
                contact.setMobile(c.getMobile());
                contactRepository.save(contact);
            }

            employeeRepository.save(employee);
            EmployeeDto newEmployee = modelMapper.map(employee, EmployeeDto.class);

            responseDto.setMessage("Employee created successfully");
            responseDto.setStatus(HttpStatus.CREATED.value());
            responseDto.setData(newEmployee);
            responseDto.setTimestamp(LocalDateTime.now());
        } else {
            responseDto.setMessage("Employee already exist");
            responseDto.setStatus(HttpStatus.CONFLICT.value());
            responseDto.setData(null);
            responseDto.setTimestamp(LocalDateTime.now());
        }
        return responseDto;
    }

    @Override
    @Cacheable(value="employeeList", sync = true)
    public ResponseDto<List<EmployeeDto>> getEmployee() {
        ResponseDto responseDto = new ResponseDto<>();

        List<Employee> employeeList = employeeRepository.findAll();

        if (employeeList.isEmpty()) {
            responseDto.setMessage("Employees not exist");
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            responseDto.setData(null);
            responseDto.setTimestamp(LocalDateTime.now());
        } else {
            List<EmployeeDto> employeeDtoList = employeeList.stream()
                            .map(eList -> modelMapper.map(eList, EmployeeDto.class))
                                    .collect(Collectors.toList());

            responseDto.setMessage("Employees retrieve successfully");
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setData(employeeDtoList);
            responseDto.setTimestamp(LocalDateTime.now());
        }
        return responseDto;
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "employeeList", allEntries = true),
                    @CacheEvict(value = "employeeById", key = "#id")
            },
            put   = {
                    @CachePut(value = "employeeList"),
                    @CachePut(value = "employeeById", key = "#id")
            }
    )
    public ResponseDto<EmployeeDto> updateEmployee(EmployeeDto employee, long id) {
        ResponseDto responseDto = new ResponseDto<>();
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isEmpty()) {
            responseDto.setMessage("Employee not exist");
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            responseDto.setData(null);
            responseDto.setTimestamp(LocalDateTime.now());
            return responseDto;
        }
        if (employeeRepository.existsByFirstNameAndLastNameAndEmployeeIdNot(employee.getFirstName(), employee.getLastName(), id)) {

            responseDto.setMessage("Employee already exist");
            responseDto.setStatus(HttpStatus.CONFLICT.value());
            responseDto.setData(null);
            responseDto.setTimestamp(LocalDateTime.now());
        } else {
            Employee currentEmployee = employeeOptional.get();

            currentEmployee.setFirstName(employee.getFirstName());
            currentEmployee.setLastName(employee.getLastName());
            currentEmployee.setEmpSalary(employee.getEmpSalary());

            employeeRepository.save(currentEmployee);

            responseDto.setMessage("Employee updated successfully");
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setData(currentEmployee);
            responseDto.setTimestamp(LocalDateTime.now());
        }
        return responseDto;
    }

    @Override
    @Cacheable(value="employeeById", key = "#id", sync = true)
    public ResponseDto<EmployeeDto> getEmployeeById(Long id){
        ResponseDto responseDto = new ResponseDto<>();
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if(employeeOptional.isEmpty()){
            responseDto.setMessage("Employee not found");
            responseDto.setStatus(HttpStatus.NOT_FOUND.value());
            responseDto.setData(null);
            responseDto.setTimestamp(LocalDateTime.now());
        }
        else{
            EmployeeDto employeeDto = modelMapper.map(employeeOptional.get(), EmployeeDto.class);
            responseDto.setMessage("Employee data retrieve");
            responseDto.setStatus(HttpStatus.OK.value());
            responseDto.setData(employeeOptional.get());
            responseDto.setTimestamp(LocalDateTime.now());
        }

        return responseDto;
    }

    @Override
    @Cacheable(value="employeeCount", sync = true, key = "'employeeCount'")
    public ResponseDto<?> employeeCount(){
        ResponseDto responseDto = new ResponseDto<>();

        responseDto.setMessage("Employee count retrieved");
        responseDto.setStatus(HttpStatus.OK.value());
        responseDto.setData(employeeRepository.count());
        responseDto.setTimestamp(LocalDateTime.now());

        return responseDto;
    }
}
