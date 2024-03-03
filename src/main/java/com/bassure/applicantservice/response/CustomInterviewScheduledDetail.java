package com.bassure.applicantservice.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomInterviewScheduledDetail {

    private CustomJobPostingResponse jobPost;

    private List<ViewScheduledInterviewResponse> applicantDetails;

}
