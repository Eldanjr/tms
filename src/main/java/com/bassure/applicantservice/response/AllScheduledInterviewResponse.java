package com.bassure.applicantservice.response;

import com.bassure.applicantservice.model.applicantModel.ApplicantDetails;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AllScheduledInterviewResponse {

    private int id;
    private String roundName;
    private ApplicantDetails applicant;
    private String mode;
    private String status;
    private String feedBack;
    private String startedAt;
    private String endedAt;
    private List<Object> interviewersList;
    private String location;
    private String title;
    private Object recuiter;
}
