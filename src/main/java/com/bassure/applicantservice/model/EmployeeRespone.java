package com.bassure.applicantservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRespone {

    private int employeeId;

    private String firstName;
    
    private String email;
}
