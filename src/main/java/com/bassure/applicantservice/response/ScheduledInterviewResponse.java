package com.bassure.applicantservice.response;

import com.bassure.applicantservice.model.applicantModel.ApplicantDetails;
import java.sql.Timestamp;
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
public class ScheduledInterviewResponse {
    
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
    
    
}
