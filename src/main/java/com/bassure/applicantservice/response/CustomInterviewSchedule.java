package com.bassure.applicantservice.response;

import com.bassure.applicantservice.model.JobRecruiter;
import com.bassure.applicantservice.model.applicantModel.ApplicantDetails;
import com.bassure.applicantservice.model.scheduling.InterviewMode;
import com.bassure.applicantservice.model.scheduling.InterviewRound;
import com.bassure.applicantservice.model.scheduling.InterviewStatus;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class CustomInterviewSchedule {


    private int id;

    private InterviewRound interviewRound;

    private ApplicantDetails applicantId;

    private JobRecruiter jobRecruiter;

    private InterviewMode modeOfInterview;

    private InterviewStatus interviewStatus;

    private String feedback;

    private int scheduledBy;

    private Timestamp startedAt;

    private Timestamp endedAt;

    private String location;

    
}