
package com.bassure.applicantservice.request;

import com.bassure.applicantservice.model.JobPosting;
import com.bassure.applicantservice.model.applicantModel.Applicant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantResponseRequest {
 
    private int applicantRequestId;

    private Boolean interested;

    private JobPosting jobPostingId;

    private Applicant applicantId;
}
