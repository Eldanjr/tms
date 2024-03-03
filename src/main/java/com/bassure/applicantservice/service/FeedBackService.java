package com.bassure.applicantservice.service;

import com.bassure.applicantservice.request.FeedBackRequest;
import com.bassure.applicantservice.response.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface FeedBackService {

    //   public List<QuestionResponse> getAnswersByApplicantId(InterviewSchedule applicantId);
    public ApiResponse addFeedBack(@RequestBody FeedBackRequest feedBackRequest, @PathVariable int scheduleId, @PathVariable int interviewer);

    public ApiResponse getBasicDetailsByInterviewId(int interviewId, int jobId);

    public ApiResponse findInterviewerFeedbackStatus(int InterviewerId, int interviewId);

    public ApiResponse findInterviewFeedbackDetails(int id);
}
