package com.bassure.applicantservice.controller;

import com.bassure.applicantservice.response.ApiResponse;
import com.bassure.applicantservice.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/skill")
@CrossOrigin
public class SkillController {

    @Autowired
    private SkillService skillService;


    @GetMapping("/get-skills")
    public ApiResponse getSkills() {
        return skillService.getSkills();
    }


}
