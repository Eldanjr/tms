package com.bassure.applicantservice.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobPostResponse {

    private CustomJobPostingResponse customJobPosting;
    private List<ViewInterviewWithoutApplicantResponse> ViewInterviewResponse;

}
