package com.bassure.applicantservice.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantStatResponse {

    private String firstName;
    private String level;
    private String applicantResponse;
    private String resumePath;
}
