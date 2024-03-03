package com.bassure.applicantservice.request;

import com.bassure.applicantservice.model.tenentemployeemodel.MartialStatus;
import com.bassure.applicantservice.model.tenentemployeemodel.Gender;
import com.bassure.applicantservice.model.tenentemployeemodel.Nationality;
import com.bassure.applicantservice.model.tenentemployeemodel.Tenantemployeeaddress;
import com.bassure.applicantservice.model.tenentemployeemodel.employeeStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.sql.Date;
import java.util.List;

public class TenentEmployeeUpdateRequest {


    @NotNull(message = "name is mandatory")
    @Size(min = 2, max = 30, message = "The length should be minimum 2 and maximum 70")
    private String firstName;

    @NotNull(message = "name is mandatory")
    @Size(min = 1, max = 30, message = "The length should be minimum 1 and maximum 3")
    private String lastName;

    @NotNull(message = "gender is mandatory")
    private Gender gender;

    @NotNull(message = "Marital status is mandatory")
    private MartialStatus maritalStatus;

    @NotNull(message = "contact number is mandatory")
    private String contactNumber;

    @NotNull(message = "email is mandatory")
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "email should be an format")
    private String email;

//    @NotNull(message = "joining date is mandatory")
//    private Date dateOfJoining;

    private employeeStatus status;

    @NotNull(message = "Blood Group is mandatory")
    private String bloodGroup;

    @NotNull(message = "DOB is mandatory")
    private Date dateOfBirth;

    @NotNull(message = "Branch details is mandatory")
    private int branchId;

    private Tenantemployeeaddress presentAddressId;

    private Tenantemployeeaddress permanentAddressId;

//    @NotNull(message = "Created By is mandatory")
//    private int createdBy;
//
//    private Timestamp createdAt;

    private int modifiedBy;

//    private Timestamp modifiedAt;

    @NotNull(message = "Aadhar Number is Mandatory")
    private String adhaarPath;

    @NotNull(message = "Pan no is Mandatory")
    private String panPath;

    @NotNull(message = "Role Id is Mandatory")
    private List<Integer> roles;

    @NotNull(message = "designation is Mandatory")
    private String designation;

    @NotNull(message = "nationality is Mandatory")
    private Nationality nationality;

    @NotNull(message = "Employee code is Mandatory")
    private String code;
}
