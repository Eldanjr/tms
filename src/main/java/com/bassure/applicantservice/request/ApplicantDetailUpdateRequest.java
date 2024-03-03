package com.bassure.applicantservice.request;

import com.bassure.applicantservice.model.applicantModel.ApplicantAddress;
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
public class ApplicantDetailUpdateRequest {

    private int applicantId;

    private int applicantDetailsId;

    private Date dateOfBirth;

//    private int addedBy;
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

//    private int referenceBy;
    private String currentCompany;

//    private int jobRecruiterId;
    private List<Integer> skill;

    private int applicantResponseId;

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
