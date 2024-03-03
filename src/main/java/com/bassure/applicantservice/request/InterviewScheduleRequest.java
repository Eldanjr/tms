package com.bassure.applicantservice.request;

import com.bassure.applicantservice.model.scheduling.InterviewMode;
import com.bassure.applicantservice.model.scheduling.InterviewStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InterviewScheduleRequest {

    private int id;

    @NotNull
    private int jobRecruiterId;

    @NotNull
    private Timestamp startedAt;

    @NotNull
    private Timestamp endedAt;

    private int applicantId;

    @NotNull
    private InterviewMode mode;

    @NotNull
    private int scheduledBy;

    @NotNull
    private int interviewRound;


    private List<Integer> interviewerId;

    private String feedback;

    private String location;

    private InterviewStatus interviewStatus;


}
