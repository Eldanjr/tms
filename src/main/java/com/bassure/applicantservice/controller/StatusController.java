package com.bassure.applicantservice.controller;

import com.bassure.applicantservice.response.ApiResponse;
import com.bassure.applicantservice.serviceimplementation.ApplicantServiceImplementation;
import com.bassure.applicantservice.serviceimplementation.ViewInterviewStatusServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RequestMapping("/applicant-service/api")
@RestController
public class StatusController {

    @Autowired
    ApplicantServiceImplementation appSer;

    @Autowired
    ViewInterviewStatusServiceImplementation interviewService;

    @GetMapping("/view-applicant-status-count/{id}")
    public ApiResponse viewApplicant(@PathVariable int id) {
        return appSer.applicantStatusCount(id);
    }

    @GetMapping("/get-job-statics/{jobId}")
    public ApiResponse getJobStatics(@PathVariable int jobId) {
        return interviewService.getJobStatics(jobId);
    }

}