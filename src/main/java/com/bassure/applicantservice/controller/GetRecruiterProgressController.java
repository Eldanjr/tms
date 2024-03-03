package com.bassure.applicantservice.controller;

import com.bassure.applicantservice.response.ApiResponse;
import com.bassure.applicantservice.service.GetRecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/applicant-service/api")
public class GetRecruiterProgressController {

    @Autowired
    private GetRecruiterService getRecruiterService;

    @RequestMapping("/get-recruiter-progress/{id}")
    public ApiResponse getRecruiterProgress(@PathVariable int id) {
        return getRecruiterService.getRecruiterProgress(id);
    }

    @RequestMapping("/get-recruiters-progress-on-jd/{jobId}/{recruiterId}")
    public ApiResponse getRecruiterProgressOnJd(@PathVariable int jobId, @PathVariable int recruiterId) {
        return getRecruiterService.getRecruiterProgressOnJd(jobId, recruiterId);
    }


    @RequestMapping("/get-jobs-by-recId/{recruiterId}")
    public ApiResponse getJobByRecruiterId(@PathVariable int recruiterId) {
        return getRecruiterService.getJobByRecruiterId(recruiterId);
    }

    @GetMapping("/get-candidates/{jobId}/{recruiterId}")
    public ApiResponse getCandidatesByRecruiterIdAndJobId(@PathVariable int jobId, @PathVariable int recruiterId) {
        return getRecruiterService.getCandidatesByRecruiterIdAndJobId(jobId, recruiterId);
    }

    @GetMapping("/get-searchjobname-status/{recruiterId}")
    public ApiResponse getSearchjobNameAndStatus(@PathVariable int recruiterId, @RequestParam String status, @RequestParam String title) {
        return getRecruiterService.searchByNameandStatus(recruiterId, status, title);
    }
}
