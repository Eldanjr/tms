package com.bassure.applicantservice.request;

import com.bassure.applicantservice.model.Skill;
import com.bassure.applicantservice.model.applicantModel.Applicant;
import com.bassure.applicantservice.model.applicantModel.ApplicantAddress;
import com.bassure.applicantservice.model.applicantModel.ApplicantEducationalDetails;
import com.bassure.applicantservice.model.applicantModel.ApplicantExperienceDetails;
import com.bassure.applicantservice.model.applicantModel.ApplicantStatus;
import com.bassure.applicantservice.model.applicantModel.Gender;
import com.bassure.applicantservice.model.applicantModel.MaritalStatus;
import com.bassure.applicantservice.model.applicantModel.yearOfExp;
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
public class ViewApplicantRequest {

    private int applicantId;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String contactNo;

    private int applicantDetailsId;

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

    private yearOfExp yearOfExperience;

    private Gender gender;

    private String source;

    private String currentCompany;

    private int referenceBy;

    private String contactNo1;

    private int jobRecruiterId;

    List<ApplicantEducationalDetails> applicantEducationalDetailses;

    List<ApplicantExperienceDetails> applicantExperienceDetailses;

    private List<Skill> skill;
}
