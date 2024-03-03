package com.bassure.applicantservice.response;

import com.bassure.applicantservice.model.applicantModel.ApplicantDetails;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantEducationalDetailsResponse {

    private int applicantEducationalDetailsId;

    private String instituteOrSchoolName;

    private String department;

    private String degree;

    private Date yearOfExperience;

    private ApplicantDetails applicantId;
}
