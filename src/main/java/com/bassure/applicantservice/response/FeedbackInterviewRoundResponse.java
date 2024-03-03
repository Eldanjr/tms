package com.bassure.applicantservice.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackInterviewRoundResponse {

    private int interviewRoundId;
    private int interviewId;
    private String roundName;

}
