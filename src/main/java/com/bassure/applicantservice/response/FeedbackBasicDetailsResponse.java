package com.bassure.applicantservice.response;

import com.bassure.applicantservice.model.feedbackform.OfferedAnswer;
import com.bassure.applicantservice.model.scheduling.InterviewMode;
import com.bassure.applicantservice.model.scheduling.InterviewStatus;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackBasicDetailsResponse {

    private int id;
    private InterviewStatus status;
    private int applicantDetailId;
    private String firstName;
    private String middleName;
    private String lastName;
    private Timestamp startedAt;
    private InterviewMode mode;
    private String description;
    private Map<String, OfferedAnswer> rating;
    private List<FeedbackInterviewRoundResponse> interviewReponses;
    private Object Interviewers;
}
