package com.bassure.applicantservice.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewerResponse {


    private String email;
    private int employeeId;
    private String firstName;
}