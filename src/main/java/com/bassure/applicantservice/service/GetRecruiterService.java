package com.bassure.applicantservice.service;

import com.bassure.applicantservice.response.ApiResponse;

public interface GetRecruiterService {

    public ApiResponse getRecruiterProgress(int id);

    ApiResponse getRecruiterProgressOnJd(int jobId, int recruiterId);

    ApiResponse getJobByRecruiterId(int recruiterId);

    ApiResponse getCandidatesByRecruiterIdAndJobId(int jobId, int recruiterId);

    ApiResponse searchByNameandStatus(int recruiter, String status, String title);
}