package com.bassure.applicantservice.controller;

import com.bassure.applicantservice.model.scheduling.InterviewRound;
import com.bassure.applicantservice.request.InterviewScheduleRequest;
import com.bassure.applicantservice.request.InterviewScheduleStatusRequest;
import com.bassure.applicantservice.response.ApiResponse;
import com.bassure.applicantservice.service.Scheduling;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@CrossOrigin
@RequestMapping("/applicant-service/api")
public class InterviewSchedulingController {

    @Autowired
    Scheduling scheduling;

    @PostMapping("/interview-schedule")
    public ApiResponse scheduleInterview(@Valid @RequestBody InterviewScheduleRequest interviewSchedule) {
        return scheduling.scheduleInterview(interviewSchedule);
    }

    @GetMapping("/interviewer/{branchId}")
    public ApiResponse findInterviewer(@PathVariable int branchId) {
        return scheduling.findAvailableInterviewer(branchId);
    }

    @GetMapping("/recruit/{id}/jobs")
    public ApiResponse findRecruiterwithJd(@PathVariable int id) {
        return scheduling.findJobsforRecruiter(id);
    }

    @PutMapping("interview/{id}/schedules")
    public ApiResponse editFeedBackAndStatus(@PathVariable int id, InterviewScheduleRequest request) {
        return scheduling.updateTheStatusAndFeedBack(id, request);
    }

    @GetMapping("/progress/{recruiterId}")
    public ApiResponse getProgressOfRecruiter(@PathVariable int recruiterId) {
        return scheduling.getInterviewProgressOfRecruiter(recruiterId);
    }

    @GetMapping("/interview/rounds")
    public ApiResponse findInterviewRounds() {
        return scheduling.findInterviewRounds();
    }

    @GetMapping("/interview/{id}/schedules")
    public ApiResponse findScheduledInterviewsById(@PathVariable int id) {
        return scheduling.findScheduledInterviewsById(id);
    }

    @GetMapping("/allinterview/{id}/schedules")
    public ApiResponse findAllScheduledInterviewsById(@PathVariable int id) {
        return scheduling.findAllScheduledInterviewsById(id);
    }

    @PutMapping("/scheduledfilter")
    public ApiResponse findAllScheduledInterviewsById(@RequestParam("option") String option, @RequestParam("value") String value) {
        return scheduling.findFilterAllScheduledInterviewsById(option, value);
    }


    @GetMapping("/rounds/{applicantId}/{jobId}/applicant")
    public ApiResponse getRoundForApplicant(@PathVariable int applicantId, @PathVariable int jobId) {
        return scheduling.roundforApplicant(applicantId, jobId);
    }


    @PostMapping("/rounds")
    public ApiResponse addInterviewRound(@RequestBody @Valid InterviewRound round) throws SQLException {
        return scheduling.addRounds(round);
    }

    @GetMapping("/scheduled-interview/{id}")
    public ApiResponse viewScheduledInterview(@PathVariable int id) {
        return scheduling.viewScheduledInterviews(id);
    }

    @GetMapping("/view-interview-by/{id}/interviewer")
    public ApiResponse getinterviewbyinterviewer(@PathVariable int id) {
        return scheduling.getinterviewbyinterviewer(id);
    }

    @GetMapping("/admins/{jobId}/{recruiterId}")
    public ApiResponse getJobRecruiter(@PathVariable int jobId, @PathVariable int recruiterId) {
        return scheduling.getJobRecruiter(jobId, recruiterId);
    }


    @GetMapping("/{interviewId}/detail")
    public ApiResponse interviewDetail(@PathVariable int interviewId) {
        return scheduling.getinterview(interviewId);
    }

    //update interviewer status
    @PutMapping("/interview/updatestatus")
    public ApiResponse updateStatus(@RequestBody InterviewScheduleStatusRequest interviewScheduleStatusRequest) {
        return scheduling.updateStatusByInterViewer(interviewScheduleStatusRequest);
    }

    @PutMapping("/update/interview")
    public ApiResponse reSchedule(@RequestBody InterviewScheduleRequest interviewScheduleRequest) {
        return scheduling.interviewReschedule(interviewScheduleRequest);
    }

    @PutMapping("/cancel-interview/{interviewId}")
    public ApiResponse cancelInterview(@PathVariable int interviewId) {
        return scheduling.cancelInterview(interviewId);
    }

    @PutMapping("/change-feedBack/{id}")
    public ApiResponse changeStatus(@PathVariable int id,@RequestParam String status) {
        return scheduling.changeInterviewStatus(id, status);
    }

}
