package com.bassure.applicantservice.service;

import com.bassure.applicantservice.model.scheduling.InterviewHistory;
import com.bassure.applicantservice.model.scheduling.InterviewRound;
import com.bassure.applicantservice.model.scheduling.InterviewSchedule;
import com.bassure.applicantservice.request.InterviewScheduleRequest;
import com.bassure.applicantservice.request.InterviewScheduleStatusRequest;
import com.bassure.applicantservice.response.ApiResponse;

import java.sql.SQLException;

public interface Scheduling {

    public ApiResponse scheduleInterview(InterviewScheduleRequest schedule);

    public ApiResponse findAvailableInterviewer(int branchId);

    public ApiResponse findJobsforRecruiter(int id);

    public ApiResponse updateTheStatusAndFeedBack(int id, InterviewScheduleRequest updateInfo);

    public ApiResponse findInterviewRounds();

    public ApiResponse findScheduledInterviewsById(int id);

    public ApiResponse findAllScheduledInterviewsById(int id);

    public ApiResponse findFilterAllScheduledInterviewsById(String searchfield, String values);

    public ApiResponse viewScheduledInterviews(int id);

    public ApiResponse addRounds(InterviewRound round) throws SQLException;

    public ApiResponse getinterviewbyinterviewer(int id);

    public ApiResponse updateStatusByInterViewer(InterviewScheduleStatusRequest interviewScheduleStatusRequest);

    public InterviewHistory interviewHistoryServices(int interviewerId, InterviewSchedule interviewId, String Remark);

    public ApiResponse roundforApplicant(int applicantId, int jobId);

    ApiResponse getJobRecruiter(int jobId, int recruiterId);

    ApiResponse getInterviewProgressOfRecruiter(int recruiterId);

    ApiResponse getinterview(int interviewId);

    ApiResponse interviewReschedule(InterviewScheduleRequest interviewScheduleRequest);

    ApiResponse cancelInterview(int interviewId);

    ApiResponse changeInterviewStatus(int interviewId, String status);
//    public List<InterviewSchedule> getScheduledInterviews(int interviewId)
}
