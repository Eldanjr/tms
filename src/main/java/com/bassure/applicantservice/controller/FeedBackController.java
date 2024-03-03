package com.bassure.applicantservice.controller;

import com.bassure.applicantservice.request.FeedBackRequest;
import com.bassure.applicantservice.response.ApiResponse;
import com.bassure.applicantservice.serviceimplementation.FeedBackServiceImplementation;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/applicant-service/api")
public class FeedBackController {

    private final FeedBackServiceImplementation feedBackService;

    public FeedBackController(FeedBackServiceImplementation feedBackService) {
        this.feedBackService = feedBackService;
    }

    @PostMapping(value = "/add-feedback/{scheduleId}/{interviewer}")
    public ApiResponse addFeedBack(@RequestBody FeedBackRequest feedBackRequest, @PathVariable int scheduleId, @PathVariable int interviewer) {
        return feedBackService.addFeedBack(feedBackRequest, scheduleId, interviewer);
    }

    @GetMapping("/{interviewId}/{jobId}/questions")
    public ApiResponse getInterviewData(@PathVariable int interviewId, @PathVariable int jobId) {
        return feedBackService.getBasicDetailsByInterviewId(interviewId, jobId);
    }

    @GetMapping("/feedback-detail/{id}")
    public ApiResponse getInterviewerStatus(@PathVariable int id) {
        return feedBackService.findInterviewFeedbackDetails(id);
    }

//    @GetMapping("/find-interviewer-status")
//    public ApiResponse getInterviewerStatus(@PathVariable int interviewerId, @PathVariable int interviewId) {
//        return feedBackService.findInterviewerFeedbackStatus(interviewerId, interviewId);
//    }
}
