package com.anton.demo.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.io.Serializable;

/**
 * @author by nadeeshan_fdz
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactDto implements Serializable {
    public long contactId;
    public String mobile;
    public long employeeId;
}
