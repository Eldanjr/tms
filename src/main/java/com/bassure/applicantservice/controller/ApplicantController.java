package com.bassure.applicantservice.controller;

import com.bassure.applicantservice.request.ApplicantAssociate;
import com.bassure.applicantservice.request.ApplicantDetailUpdateRequest;
import com.bassure.applicantservice.request.ApplicantDetailsRequest;
import com.bassure.applicantservice.request.ApplicantRequest;
import com.bassure.applicantservice.response.ApiResponse;
import com.bassure.applicantservice.service.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequestMapping("/applicant-service/api")
@RestController
public class ApplicantController {

    @Autowired
    ApplicantService appSer;

    @GetMapping("/view-applicant")
    public ApiResponse viewApplicant(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return appSer.viewAllApplicant(pageable);
    }

    @PostMapping("/add-applicant-details")
    public ApiResponse addApplicantDetails(@RequestBody ApplicantDetailsRequest applicant) {
        return appSer.addApplicantDetails(applicant);
    }

    @PostMapping("/add-applicant")
    public ApiResponse addApplicant(@RequestBody ApplicantRequest applicantRequest, @RequestParam(defaultValue = "false") boolean email) {
        return appSer.addApplicant(applicantRequest, email);
    }

    @PutMapping("/update/{id}")
    public ApiResponse updateApplicant(@RequestBody ApplicantDetailUpdateRequest applicant, @PathVariable int id) {
        return appSer.updateApplicant(applicant, id);
    }

    @PutMapping("/updateBasic/{id}")
    public ApiResponse updateApplicantBasic(@RequestBody ApplicantRequest applicant, @PathVariable int id) {
        return appSer.updateApplicantBasic(applicant, id);
    }

    @GetMapping("/view-single-applicant/{id}")
    public ApiResponse getSingleApplicant(@PathVariable int id) {
        return appSer.viewSingleApplicant(id);
    }

    @GetMapping("/view-single-applicants/{id}")
    public ApiResponse getSingleApplicants(@PathVariable int id) {
        return appSer.viewSingleApplicants(id);
    }

    @RequestMapping("/{id}/status")
    public ApiResponse statusApplicant(@PathVariable int id, @RequestParam String sts) {
        return appSer.applicantStatusChange(id, sts);
    }

    @GetMapping("/admin/verify")
    public ApiResponse verifyOtp(@RequestParam String email, @RequestParam String token) {
        return appSer.verifyApplicant(email, token);
    }

    @GetMapping("{id}/job")
    public ApiResponse findRecruiterwithJd(@PathVariable int id) {
        return appSer.findRecruiterJob(id);
    }

    @GetMapping("/applicant-job")
    public ApiResponse findApplicantwithJob() {
        return appSer.findApplicantByJob1();
    }

    @RequestMapping("/find-by-status")
    public ApiResponse findApplicantBySts(@RequestParam String sts) {
        return appSer.findApplicantByStatus(sts);
    }

    @RequestMapping("/find-by-mobile")
    public ApiResponse findApplicantByMob(@RequestParam String mobile) {
        return appSer.findApplicantByMobile(mobile);
    }

    @GetMapping("/view-applicant-job/{id}")
    public ApiResponse getApplicantByJob(@PathVariable int id) {
        return appSer.findApplicantByJob(id);
    }

    @GetMapping("/view-applicant-skill/{id}")
    public ApiResponse getApplicantBySkill(@PathVariable int id) {
        return appSer.findApplicantBySkill(id);
    }

    @GetMapping("/applicant-count")
    public ApiResponse findApplicantsCount() {
        return appSer.findApplicantsCount();
    }

    @PostMapping("/applicant-associate")
    public ApiResponse applicantAssociate(@RequestBody ApplicantAssociate applicant) {
        return appSer.associate(applicant);
    }

    @GetMapping("/applicant-status-jr/{jobRecruiter}")
    public ApiResponse applicantStatforJr(@PathVariable int jobRecruiter) {
        return appSer.applicantStat(jobRecruiter);
    }

    @RequestMapping("/find-by-email")
    public ApiResponse findApplicantByEmail(@RequestParam String email) {
        return appSer.findApplicantByEmail(email);
    }
}
