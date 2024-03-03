package com.bassure.applicantservice.request;

import com.bassure.applicantservice.model.applicantModel.ApplicantAddress;
import com.bassure.applicantservice.model.applicantModel.ApplicantEducationalDetails;
import com.bassure.applicantservice.model.applicantModel.ApplicantExperienceDetails;
import com.bassure.applicantservice.model.applicantModel.ApplicantStatus;
import com.bassure.applicantservice.model.applicantModel.Gender;
import com.bassure.applicantservice.model.applicantModel.MaritalStatus;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
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
public class ApplicantDetailsRequest {

    private int applicantDetailsId;

    private String contactNo;

    private Date dateOfBirth;

    private int addedBy;

    private Timestamp addedAt;

    private String currentCtc;

    private String expectedCtc;

    private String reasonForChange;

    private Boolean holdingOffer;

    private MaritalStatus maritalStatus;

    private ApplicantAddress currentAddress;

    private ApplicantStatus applicantStatus;

    private String resumePath;

    private String yearOfExperience;

    private Gender gender;

    private String source;

    private String referenceBy;

    private String currentCompany;

    private String contactNo1;

    private int jobRecruiterId;

    List<ApplicantEducationalDetails> applicantEducationalDetailses;

    List<ApplicantExperienceDetails> applicantExperienceDetailses;

    private List<Integer> skill;

    private int applicantId;

}
