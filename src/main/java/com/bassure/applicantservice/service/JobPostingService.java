package com.bassure.applicantservice.service;

import com.bassure.applicantservice.model.JobHistory;
import com.bassure.applicantservice.model.JobPosting;
import com.bassure.applicantservice.request.JobPostingRequest;
import com.bassure.applicantservice.request.JobTemplateRequest;
import com.bassure.applicantservice.response.ApiResponse;

public interface JobPostingService {

    public ApiResponse addJobPosting(JobPostingRequest joBPostingRequest);

    public ApiResponse updateJobPosting(int jobPostingId, JobPostingRequest jopBPostingRequest);

    public ApiResponse getAllPostedJobs(int id);

    public ApiResponse getJobById(int id);

    public ApiResponse getProgressedCounts(int id);

    public ApiResponse getJobTemplates();

    public ApiResponse addJobTemplate(JobTemplateRequest jobTemplateRequest);

    public ApiResponse getJobDraft(int id);

    public ApiResponse postJobDraft(int id);

    public ApiResponse deleteJobDraft(int id);

    public ApiResponse getjobsIprocess(int branchId);

    public ApiResponse getRecuiterDetailsForJob(int jobPosting, int branchId);

    public ApiResponse updatejobDraft(int jobpostId, JobPostingRequest jobPostingRequest);

    public JobHistory jobHistory(JobPosting jobPosting, int modifiedBy, String status);

    public ApiResponse getJobHistory(int jobId);
    
}

