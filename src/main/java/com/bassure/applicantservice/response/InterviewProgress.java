package com.bassure.applicantservice.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InterviewProgress {
    private int interviewScheduleId;
    private String interviewLevel;
    private Timestamp startTime;
    private Timestamp endTime;
    private List<Object> interviewersId;
}
