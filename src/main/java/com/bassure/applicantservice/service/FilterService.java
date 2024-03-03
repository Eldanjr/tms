package com.bassure.applicantservice.service;

import com.bassure.applicantservice.model.JobPostingStatus;
import com.bassure.applicantservice.model.applicantModel.ApplicantStatus;
import com.bassure.applicantservice.model.scheduling.InterviewSchedule;
import com.bassure.applicantservice.response.ApiResponse;

import java.util.Date;
import java.util.List;

public interface FilterService {

    ApiResponse filterJobPostings(int branchId, String title, String location, Date startDate, Date endDate,
            JobPostingStatus status,
            String experience);

    ApiResponse filterApplicant(String firstName, String middleName, String lastName, String email, String contactNo, ApplicantStatus applicantStatus);

    List<InterviewSchedule> withfilter(List<Object> ids, Date openDate, Date closeDate);

}
