package com.bassure.applicantservice.controller;

import com.bassure.applicantservice.request.JobPostingRequest;
import com.bassure.applicantservice.request.JobTemplateRequest;
import com.bassure.applicantservice.response.ApiResponse;
import com.bassure.applicantservice.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/applicant-service/api")
@RestController
public class JobPostingController {

    @Autowired
    JobPostingService jobPostingService;

    @PostMapping("/job-post")
    private ApiResponse addJobPost(@RequestBody JobPostingRequest jobPostingRequest) {
        return jobPostingService.addJobPosting(jobPostingRequest);
    }

    @PutMapping("/update-job-post/{id}")
    private ApiResponse updateJobPost(@RequestBody JobPostingRequest jobPostingRequest, @PathVariable("id") int id) {
        return jobPostingService.updateJobPosting(id, jobPostingRequest);
    }

    @GetMapping("/get-posted-jobs/{id}")
    private ApiResponse getPosting(@PathVariable("id") int id) {
        return jobPostingService.getAllPostedJobs(id);
    }

    @GetMapping("/view-job/{id}")
    private ApiResponse getJobById(@PathVariable("id") int id) {
        return jobPostingService.getJobById(id);
    }

    @GetMapping("/get-progressed-counts/{id}")
    private ApiResponse getProgressedCounts(@PathVariable("id") int id) {
        return jobPostingService.getProgressedCounts(id);
    }

    @GetMapping("/get-job-templates")
    private ApiResponse getJobTemplateIds() {
        return jobPostingService.getJobTemplates();
    }

    @PostMapping("/job-template")
    private ApiResponse addJobTemplate(@RequestBody JobTemplateRequest jobTemplateRequest) {
        return jobPostingService.addJobTemplate(jobTemplateRequest);
    }

    @GetMapping("/get-job-draft/{id}")
    public ApiResponse findJobDrafts(@PathVariable("id") int id) {
        return jobPostingService.getJobDraft(id);
    }

    @PutMapping("/update-draft/{id}")
    public ApiResponse postJobDraft(@PathVariable("id") int id) {
        return jobPostingService.postJobDraft(id);
    }

    @GetMapping("/get-recruiters-statics")
    private ApiResponse recruiterJobDetails(@RequestParam("jobId") int jobPosting, @RequestParam("branchId") int branchId) {
        return jobPostingService.getRecuiterDetailsForJob(jobPosting, branchId);
    }

    @GetMapping("/jobs-inprocess/{branchId}")
    private ApiResponse jobsInprocess(@PathVariable("branchId") int branchId) {
        return jobPostingService.getjobsIprocess(branchId);
    }

    @DeleteMapping("/delete-job-draft/{id}")
    private ApiResponse deleteJobDraft(@PathVariable int id) {
        return jobPostingService.deleteJobDraft(id);
    }

    @PutMapping("/job-draft-update/{jobpostId}")
    private ApiResponse jobDraftUpdate(@PathVariable int jobpostId, @RequestBody JobPostingRequest jobPostingRequest) {
        return jobPostingService.updatejobDraft(jobpostId, jobPostingRequest);
    }
}
