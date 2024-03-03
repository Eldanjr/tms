package com.bassure.applicantservice.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SimilarProfileResponse {

    private String applicantName;
    private int applicantDetailsId;
    private String jobTitle;
    private String experience;

}
