package com.bassure.applicantservice.service;

import com.bassure.applicantservice.response.ApiResponse;

import java.util.Date;
import org.springframework.data.domain.Pageable;

public interface ViewInterviewService {

//    public ApiResponse viewInterviewStatusByManager();
//    public ApiResponse viewInterviewStatusByManagerGroupByApplicant(int branchId, String status, Date openDate, Date closeDate);
    public ApiResponse viewInterviewByRecruiter(int id, String status, Date openDate, Pageable pageable);

    public ApiResponse viewInterviewSchedulerDetails(int jobId);

    public ApiResponse viewScheduledInterviewByBranch(int branchId, String status, Date openDate, Pageable pageable);

    public ApiResponse getApplicantInterviewsById(int applicantId);

    public ApiResponse getApplicantById(int id);

    public ApiResponse getApplicantInterviewsByJob(int applicantId, int jobPostId, String interviewStatus);

    public ApiResponse getApplicantsByJobId(int jobId, String status);

    public ApiResponse getRecuriterStaticsByJobId(int branchId, int jobId);

    public ApiResponse getRandomRecuriterStatics(int branchId);

    public ApiResponse getApplicantStatusByJob(int jobId, int applicantId);

    public ApiResponse getRandomApplicantByBranch(int branchId);

    public ApiResponse getRandomApplicantByRecuriter(int recuriterId);

    public ApiResponse updateApplicantResponseStatus(int applicantResponseId, String status);

}
