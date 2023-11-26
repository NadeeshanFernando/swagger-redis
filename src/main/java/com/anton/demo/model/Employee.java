package com.anton.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
@Entity
@ToString
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "employeeId")
    public  long employeeId;

    @Column(name = "first_name")
    public String firstName;

    @Column(name = "last_name")
    public String lastName;

    @Column(name = "emp_salary")
    public double empSalary;

    @OneToMany(mappedBy = "employee")
    @JsonIgnoreProperties("employee")
    Set<Contact> contactList;
}
