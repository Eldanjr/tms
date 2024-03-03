package com.bassure.applicantservice.response;

import com.bassure.applicantservice.model.applicantModel.ApplicantStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ApplicantResponsesResponse {

    private int applicantId;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String contactNo;

    private ApplicantStatus applicantStatus;

    private int roundId;

    private int recruiterId;

    private String jobTitle;

    private boolean applicantFlag;

    private String jobRecruiterName;

    private int applicantdetailsId;

    private String resume;

    private String skillName;

}
