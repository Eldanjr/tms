package com.bassure.applicantservice.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ViewScheduledInterviewResponse {

    private int applicantId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String applicantStatus;
    private String resumePath;
    private Timestamp InterviewDate;
    private int jobId;
    private Object recuriterId;
    private String jobTitle;
    private int interviewId;
    private String InterviewStatus;
    private String roundName;
    private String email;
    private String contactNo;
    private Object panel;

}
