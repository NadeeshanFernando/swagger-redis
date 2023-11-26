package com.anton.demo.repo;

import com.anton.demo.dto.ContactDto;
import com.anton.demo.model.Contact;
import com.anton.demo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    boolean existsByMobile(String mobile);

    List<Contact> findByEmployee(Employee employee);
}
