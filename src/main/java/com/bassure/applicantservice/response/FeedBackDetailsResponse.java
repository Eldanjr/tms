package com.bassure.applicantservice.response;

import java.util.Date;
import java.util.List;
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
public class FeedBackDetailsResponse {
    
    private String applicantName;
    private Date interviewDate;
    private String interviewType;
    private String round;
    private String interviewStatus;
    private String applicantStatus;
    List<InterviewerStatusResponse> interviewerFeedback;
}
