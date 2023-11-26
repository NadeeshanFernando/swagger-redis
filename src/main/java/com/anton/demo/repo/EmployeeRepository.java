package com.anton.demo.repo;


import com.anton.demo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    boolean existsByFirstNameAndLastNameAndEmployeeIdNot(String firstName, String lastName, long id);

    long count();
}
