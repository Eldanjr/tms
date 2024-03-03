package com.bassure.applicantservice.response;

import com.bassure.applicantservice.model.JobPosting;
import com.bassure.applicantservice.model.JobRecruiterStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomJobRecuriterResponse {

    private int id;

    private int recruiterId;

    private int assignedCounts;

    private JobRecruiterStatus status;

    private Object recuriterDetails;

}
