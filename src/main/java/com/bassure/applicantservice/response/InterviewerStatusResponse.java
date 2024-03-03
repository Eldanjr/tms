package com.bassure.applicantservice.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class InterviewerStatusResponse {

    private int interviewerId;
    private String interviewerName;
    private String jobName;
    private boolean feedbackStatus;

}
