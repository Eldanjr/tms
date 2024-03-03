package com.bassure.applicantservice.request;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantSkillRequest {

    private int applicantSkillId;

    private String name;

    private String description;

}
