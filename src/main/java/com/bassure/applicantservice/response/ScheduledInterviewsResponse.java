package com.bassure.applicantservice.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ScheduledInterviewsResponse {

    private List<ViewScheduledInterviewResponse> scheduledInterviewResponse;
    private int totalCount;

}
