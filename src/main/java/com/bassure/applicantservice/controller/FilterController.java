package com.bassure.applicantservice.controller;

import com.bassure.applicantservice.model.JobPostingStatus;
import com.bassure.applicantservice.model.applicantModel.ApplicantStatus;
import com.bassure.applicantservice.response.ApiResponse;
import com.bassure.applicantservice.service.FilterService;
import com.bassure.applicantservice.service.ViewInterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;



@RestController
@CrossOrigin
@RequestMapping("/applicant-service/api")
public class FilterController {

    @Autowired
    private FilterService filterService;

    @Autowired
    ViewInterviewService viewInterview;

    @GetMapping("/jobListings/{branchId}")
    public ApiResponse filterJobListings(
            @RequestParam(required = false) String title,
            @PathVariable(name = "branchId") int branchId,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false) JobPostingStatus status,
            @RequestParam(required = false) String experience
    ) {
        return filterService.filterJobPostings(branchId, title, location, startDate, endDate, status, experience);
    }

//    @GetMapping("/view-interview/{id}")
//    public ApiResponse filterInterviewSheduleList(
//            @PathVariable(value = "id") int id,
//            @RequestParam(required = false) String status,
//            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date openDate,
//            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date closeDate
//    ) {
//        return viewInterview.viewInterviewStatusByManagerGroupByApplicant(id, status, openDate, closeDate);
//    }

    @GetMapping("/applicant")
    public ApiResponse filterApplicant(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String middleName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String contactNo,
            @RequestParam(required = false) ApplicantStatus applicantStatus
    ) {
        return filterService.filterApplicant(firstName, middleName, lastName, email, contactNo, applicantStatus);
    }

}
