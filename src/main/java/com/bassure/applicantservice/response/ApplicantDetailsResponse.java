package com.bassure.applicantservice.response;

import com.bassure.applicantservice.model.Skill;
import com.bassure.applicantservice.model.applicantModel.ApplicantAddress;
import com.bassure.applicantservice.model.applicantModel.ApplicantResponse;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantDetailsResponse {

    private int applicantId;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String contactNo;

    private ApplicantStatus applicantStatus;

    private int applicantDetailsId;

    private Date dateOfBirth;

    private int addedBy;

    private Timestamp addedAt;

    private String currentCtc;

    private String expectedCtc;

    private String reasonForChange;

    private Boolean holdingOffer;

    private MaritalStatus maritalStatus;

    private String resumePath;

    private String yearOfExperience;

    private Gender gender;

    private String source;

    private String currentCompany;

    private int referenceBy;

//    private int jobRecruiterId;
//    List<ApplicantEducationalDetails> applicantEducationalDetailses;
//
//    List<ApplicantExperienceDetails> applicantExperienceDetailses;
    private List<Skill> skill;

//    private com.bassure.applicantservice.model.applicantModel.ApplicantResponse applicantResponseId;
    private ApplicantResponse  ApplicantResponseId;
    
    private ApplicantAddress currentAddress;

//    private int addressId;
//
//    private String doorNo;
//
//    private String street;
//
//    private String city;
//
//    private String state;
//
//    private String country;
//
//    private String pinCode;

}
