package com.bassure.applicantservice.response;

import com.bassure.applicantservice.model.applicantModel.ApplicantDetails;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ViewInterviewGroupByApplicantResponse {

    private ApplicantDetails applicant;

    private List<CustomJobPostingResponse> jobPost;
    
    private boolean alreadtInterviewScheduled;

}
