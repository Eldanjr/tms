package com.bassure.applicantservice.response;

import com.bassure.applicantservice.request.*;
import com.bassure.applicantservice.model.applicantModel.ApplicantDetails;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantExperienceDetailsResponse {

    private int applicantExpDetailsId;

    private String occupationName;

    private String companyName;

    private String summary;

    private String yearOfExperience;

    private ApplicantDetails applicantId;
}
