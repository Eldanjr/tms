package com.bassure.applicantservice.model.applicantModel;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "applicant")
public class Applicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int applicantId;

    @NotBlank(message = "First Name Should Not Be Empty")
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @NotBlank(message = "mobile number should not be empty")
    @Column(name = "contact_no")
    private String contactNo;

    @Column(name = "applicant_flag")
    private Boolean applicantFlag;

    @Enumerated(EnumType.STRING)
    @Column(name = "applicant_status")
    private ApplicantStatus applicantStatus;
    
    @Column(name = "reason")
    private String reason;

    @Transient
    private int createdBy;

}
