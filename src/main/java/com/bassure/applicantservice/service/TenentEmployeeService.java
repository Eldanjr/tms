package com.bassure.applicantservice.service;

import com.bassure.applicantservice.response.ApiResponse;


public interface TenentEmployeeService {

    public ApiResponse viewProfileByEmployeeId(int employeeId);
    public ApiResponse viewRecuriterByBranchId(int branchId);

    public ApiResponse viewInterviewerByBranchId(int branchId);
     public ApiResponse viewInterviewerById(int id);
     public ApiResponse viewInterviewer(int id);

    public Object getRecuriterIdByBranch(int branchId);
    
      public ApiResponse getRecruiterJobCount(int id);
}
