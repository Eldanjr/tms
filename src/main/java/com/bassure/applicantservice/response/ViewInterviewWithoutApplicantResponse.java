package com.bassure.applicantservice.response;

import com.bassure.applicantservice.model.scheduling.InterviewMode;
import com.bassure.applicantservice.model.scheduling.InterviewRound;
import com.bassure.applicantservice.model.scheduling.InterviewStatus;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ViewInterviewWithoutApplicantResponse {

    private int id;

    private InterviewRound interviewRound;

//    private JobRecruiter jobRecruiter;

    private InterviewMode modeOfInterview;

    private InterviewStatus interviewStatus;

    private String feedback;

    private int scheduledBy;

    private Timestamp startedAt;

    private Timestamp endedAt;

    private Object jobRecruiterDetails;

    private Object panel;

}
