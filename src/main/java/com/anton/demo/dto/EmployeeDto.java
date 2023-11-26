package com.anton.demo.dto;

import com.anton.demo.model.Contact;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author by nadeeshan_fdz
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class EmployeeDto implements Serializable {
    public  long employeeId;
    public String firstName;
    public String lastName;
    public double empSalary;
    public Set<ContactDto> contactList;
}
