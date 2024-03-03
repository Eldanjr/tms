package com.bassure.applicantservice.request;

import com.bassure.applicantservice.model.applicantModel.ApplicantStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApplicantRequest {

    private int applicantId;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String contactNo;

    private ApplicantStatus applicantStatus;

    private int jobPostingId;
    
    private Boolean interested;
    
    private boolean applicantFlag;
}
