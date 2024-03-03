/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bassure.applicantservice.controller;

import com.bassure.applicantservice.response.ApiResponse;
import com.bassure.applicantservice.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/applicant-service/api")
@RestController
public class JobHistoryController {

    @Autowired
    private JobPostingService jobPostingService;

    @GetMapping("/job-History/{id}")
    public ApiResponse getJobHistory(@PathVariable(value = "id") int id) {
        return jobPostingService.getJobHistory(id);
    }

}
