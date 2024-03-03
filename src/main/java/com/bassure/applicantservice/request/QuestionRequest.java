package com.bassure.applicantservice.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequest {

    private String analytical;
    private String communication;
    private String finalproject;
    private String problemsolving;
    private String technical;
    private String workexp;

}
