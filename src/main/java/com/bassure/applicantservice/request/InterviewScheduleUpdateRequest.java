package com.bassure.applicantservice.request;

import com.bassure.applicantservice.model.scheduling.InterviewStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewScheduleUpdateRequest {

    private int interviewScheduleId;

    private InterviewStatus status;

    private String feedback;

}
