package com.bassure.applicantservice.controller;

import com.bassure.applicantservice.response.ApiResponse;
import com.bassure.applicantservice.service.TenentEmployeeService;
import com.bassure.applicantservice.service.ViewInterviewService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/applicant-service/api")
public class TenentEmployeeViewProfileController {

    @Autowired
    TenentEmployeeService tenentEmployeeService;

    @Autowired
    ViewInterviewService viewInterviewService;

    @GetMapping("/employee/all/view-my-profile/{employeeId}")
    public ApiResponse tenentEmployeeViewMyProfile(@PathVariable(name = "employeeId") int employeeId) {
        return tenentEmployeeService.viewProfileByEmployeeId(employeeId);
    }

    @GetMapping("/employee/view-recuriter-by-branchId/{branchId}")
    public ApiResponse viewTenentRecuriterByBranchId(@PathVariable(name = "branchId") int branchId, Principal principal) {
        return tenentEmployeeService.viewRecuriterByBranchId(branchId);
    }

    @GetMapping("/employee/ar/view-interview-by/{id}/recruiter")
    public ApiResponse viewInterviewStatusByRecruiter(@PathVariable(name = "id") int id, @RequestParam(name = "status", required = false) String status, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date openDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return viewInterviewService.viewInterviewByRecruiter(id, status, openDate, pageable);
    }

    @GetMapping("/admin/view-scheduler-details/{jobid}")
    public ApiResponse viewInterviewSchedulerDetail(@PathVariable(name = "jobid") int jobId) {
        return viewInterviewService.viewInterviewSchedulerDetails(jobId);
    }

    @GetMapping("/admin/view-scheduled-interview-status/{branchId}")
    public ApiResponse viewScheduledInterviewStatusByManager(@PathVariable(name = "branchId") int branchId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "openDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date openDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        System.out.println(page + " " + size);
        Pageable pageable = PageRequest.of(page, size);
        return viewInterviewService.viewScheduledInterviewByBranch(branchId, status, openDate, pageable);
    }

    @GetMapping("/employee/all/get-applicant-jobs/{id}")
    public ApiResponse getApplicantJobsById(@PathVariable(name = "id") int id) {
        return viewInterviewService.getApplicantInterviewsById(id);
    }

    @GetMapping("/get-applicant-interviews/{applicantid}/{jobId}")
    public ApiResponse getApplicantInterviewsByJobId(@PathVariable(name = "applicantid") int id, @PathVariable(name = "jobId") int jobId, @RequestParam(value = "interviewStatus", required = false) String interviewStatus) {
        System.out.println(id + " " + jobId);
        return viewInterviewService.getApplicantInterviewsByJob(id, jobId, interviewStatus);
    }

    @GetMapping("/get-recruiter-assigned-job/{id}")
    public ApiResponse getRecruiterAssignedJobCount(@PathVariable(name = "id") int id) {
        return tenentEmployeeService.getRecruiterJobCount(id);
    }

    @GetMapping("/get-applicants-jobId/{jobId}")
    public ApiResponse getApplicantByJobId(@PathVariable(name = "jobId") int jobId, @RequestParam(value = "status", required = false) String status) {
        return viewInterviewService.getApplicantsByJobId(jobId, status);
    }

    @GetMapping("/get-recuriter-statics-jobId/{branchId}/{jobId}")
    public ApiResponse getRecuriterStaticsByJobId(@PathVariable(name = "jobId") int jobId, @PathVariable(name = "branchId") int branchId) {
        return viewInterviewService.getRecuriterStaticsByJobId(branchId, jobId);
    }

    @GetMapping("/get-recuriter-statics/{branchId}")
    public ApiResponse getRecuriterStatics(@PathVariable(name = "branchId") int branchId) {
        return viewInterviewService.getRandomRecuriterStatics(branchId);
    }

    @GetMapping("/get-applicant-status-jobId/{jobId}/{applicantId}")
    public ApiResponse getStatusByJobId(@PathVariable(name = "jobId") int jobId, @PathVariable(name = "applicantId") int applicantId) {
        return viewInterviewService.getApplicantStatusByJob(jobId, applicantId);
    }

    @GetMapping("/get-similar-profile-branch/{branchId}")
    public ApiResponse getRandomApplicantForBranch(@PathVariable(name = "branchId") int branchId) {
        return viewInterviewService.getRandomApplicantByBranch(branchId);
    }

    @GetMapping("/get-similar-profile-recuriter/{recuriterId}")
    public ApiResponse getRandomApplicantForRecuriter(@PathVariable(name = "recuriterId") int recuriterId) {
        return viewInterviewService.getRandomApplicantByRecuriter(recuriterId);
    }

    @PostMapping("/update-applicant-response/{applicantResponseId}")
    public ApiResponse updateAppicantResponseStatus(@PathVariable(name = "applicantResponseId") int applicantResponseId, @RequestParam(name = "status") String status) {
        return viewInterviewService.updateApplicantResponseStatus(applicantResponseId, status);
    }

}
