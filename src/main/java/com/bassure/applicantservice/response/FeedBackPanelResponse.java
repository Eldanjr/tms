package com.bassure.applicantservice.response;

import com.bassure.applicantservice.model.scheduling.InterviewSchedule;
import com.bassure.applicantservice.model.scheduling.Interviewers;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FeedBackPanelResponse {

    private List<Interviewers> interviewPanels;
    private InterviewSchedule schedule;
}
