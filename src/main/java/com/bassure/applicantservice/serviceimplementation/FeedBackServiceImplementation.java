package com.bassure.applicantservice.serviceimplementation;

import com.bassure.applicantservice.model.feedbackform.FeedBack;
import com.bassure.applicantservice.model.feedbackform.FeedBackQuestion;
import com.bassure.applicantservice.model.feedbackform.OfferedAnswer;
import com.bassure.applicantservice.model.feedbackform.Question;
import com.bassure.applicantservice.model.scheduling.InterviewMode;
import com.bassure.applicantservice.model.scheduling.InterviewSchedule;
import com.bassure.applicantservice.model.scheduling.InterviewStatus;
import com.bassure.applicantservice.model.scheduling.Interviewers;
import com.bassure.applicantservice.repo.*;
import com.bassure.applicantservice.request.FeedBackRequest;
import com.bassure.applicantservice.response.*;
import com.bassure.applicantservice.service.FeedBackService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Timestamp;
import java.util.*;

@Service
@Slf4j
public class FeedBackServiceImplementation implements FeedBackService {

    @Autowired
    private FeedBackRepository feedbackRepository;

    @Autowired
    private ScheduleImplementation scheduleImplementation;

    @Autowired
    private InterviewScheduleRepository interviewSchedule;

    private final Logger LOGGER = LoggerFactory.getLogger(FeedBackServiceImplementation.class);

    @Autowired
    FeedBackQuestionsRepository feedbackQuestionsRepository;

    @Autowired
    InterviewPanelRepository interviewPanelRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    TenentEmployeeServiceImplementation tenentService;

    @Autowired
    private ResponseCode code;

    @Autowired
    private ResponseMessage message;

    @Autowired
    InterviewScheduleRepository interviewRepo;

    @Override
    @Transactional
    public ApiResponse addFeedBack(@RequestBody FeedBackRequest feedBackRequest, @PathVariable int interviewId, @PathVariable int interviewer) {
        try {
            InterviewSchedule interviewSchedule = interviewRepo.findById(interviewId).orElseThrow(() -> new IllegalArgumentException("Invalid panel ID: " + interviewId));
            FeedBack feedBackresponse = feedbackRepository.findByInterviewerId(interviewer, interviewId);
            if (Objects.nonNull(feedBackresponse)) {
                return new ApiResponse(code.getAlreadyExists(), null, Map.of(message.getErrorKey(), message.getAlreadyExists()));
            } else {

                FeedBack feedback = new FeedBack();
                feedback.setDesc(feedBackRequest.getDesc());
                Optional<Interviewers> found = Optional.empty();
                for (Interviewers interviewers : interviewSchedule.getInterviewers()) {
                    if (interviewers.getInterviewerId() == interviewer) {
                        found = Optional.of(interviewers);
                        break;
                    }
                }
                if (found.isPresent()) {
                    feedback.setInterviewerId(
                            found.get()
                    );
                } else {
                    return new ApiResponse(code.getNotFound(), null, Map.of(message.getErrorKey(), message.getNoDataFound()));
                }

                feedback = feedbackRepository.save(feedback);

                for (Map.Entry<String, OfferedAnswer> entry : feedBackRequest.getQuestion().entrySet()) {
                    LOGGER.info("{} key ==== ", entry);
                    String question = entry.getKey();
                    OfferedAnswer answer = entry.getValue();
                    LOGGER.info("{} question ", question);
                    Question questionKey = questionRepository.findByQuestions(question.toUpperCase());
                    LOGGER.info("question {}", questionKey);
                    FeedBackQuestion feedbackQuestion = new FeedBackQuestion();
                    feedbackQuestion.setFeedbackId(feedback);
                    feedbackQuestion.setQuestionId(questionKey);
                    feedbackQuestion.setOfferedAnswer(answer);
                    feedbackQuestionsRepository.save(feedbackQuestion);
                }

                return new ApiResponse(code.getSuccess(), "Feedback added Successfully", null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ApiResponse(code.getServerError(), null, Map.of(message.getErrorKey(), message.getServerError()));
        }

    }

    @Override
    public ApiResponse getBasicDetailsByInterviewId(int interviewId, int jobId) {

        List<Object[]> feed = feedbackRepository.getBasicDetailsByInterviewId(interviewId);
        FeedbackBasicDetailsResponse basicRepo = new FeedbackBasicDetailsResponse();

        for (Object[] obj : feed) {
            basicRepo.setId((int) obj[0]);
            basicRepo.setStatus((InterviewStatus) InterviewStatus.valueOf((String) obj[1]));
            basicRepo.setApplicantDetailId((int) obj[2]);
            basicRepo.setFirstName((String) obj[3]);
            basicRepo.setMiddleName((String) obj[4]);
            basicRepo.setLastName((String) obj[5]);
            basicRepo.setStartedAt((Timestamp) obj[6]);
            basicRepo.setMode((InterviewMode) InterviewMode.valueOf((String) obj[7]));
            basicRepo.setDescription((String) obj[8]);
        }

        Map<String, OfferedAnswer> ratingMap = new HashMap<>();

        List<Object[]> rating = feedbackRepository.getTableFeedbackByFeedbackId(interviewId);
        for (Object[] obj : rating) {
            ratingMap.put((String) obj[0], (OfferedAnswer) OfferedAnswer.valueOf((String) obj[1]));
        }
        basicRepo.setRating(ratingMap);

        List<Object[]> roundRepo = feedbackRepository.getInterviewRoundId(basicRepo.getApplicantDetailId(), jobId);
        List<FeedbackInterviewRoundResponse> roundList = new ArrayList<>();
        for (Object[] obj : roundRepo) {
            FeedbackInterviewRoundResponse rounds = new FeedbackInterviewRoundResponse();
            rounds.setInterviewRoundId((int) obj[0]);
            rounds.setInterviewId((int) obj[1]);
            rounds.setRoundName((String) obj[2]);
            roundList.add(rounds);
        }

        basicRepo.setInterviewReponses(roundList);
        List<Integer> interviewerId = feedbackRepository.getInterviewerByPanelId(interviewId);
        interviewerId.forEach(i -> System.out.println(i));
        System.out.println(tenentService.getGroupOfEmployeesByIds(interviewerId));
        basicRepo.setInterviewers(tenentService.getGroupOfEmployeesByIds(interviewerId));

        return new ApiResponse("2200", basicRepo, null);
    }

    @Override
    public ApiResponse findInterviewFeedbackDetails(int interviewId) {
        try {
            System.out.println("called>>>>>>>>>>>>>>");
            FeedBackDetailsResponse feedbackdetails = new FeedBackDetailsResponse();
            List<InterviewerStatusResponse> interviewerFeedback = new ArrayList<>();
            InterviewSchedule details = interviewSchedule.getInterviewDetails(interviewId);
            for (int i = 0; i < details.getInterviewers().size(); i++) {
                InterviewerResponse resp = scheduleImplementation.findInterviewEmail(details.getInterviewers().get(i).getInterviewerId());
                System.out.println("id>>>>>" + details.getInterviewers().get(i).getInterviewerId());
                FeedBack interviewersts = feedbackRepository.findByInterviewerId(details.getInterviewers().get(i).getInterviewerId(), interviewId);
                InterviewerStatusResponse status = new InterviewerStatusResponse();
                feedbackdetails.setApplicantName(details.getApplicantId().getApplicantResponseId().getApplicantId().getFirstName());
                feedbackdetails.setApplicantStatus(details.getApplicantId().getApplicantResponseId().getApplicantId().getApplicantStatus().name());
                feedbackdetails.setInterviewDate(details.getStartedAt());
                feedbackdetails.setInterviewType(details.getModeOfInterview().name());
                feedbackdetails.setRound(details.getInterviewRound().getName());
                feedbackdetails.setInterviewStatus(details.getInterviewStatus().name());
                status.setInterviewerName(resp.getFirstName());
                status.setJobName(details.getApplicantId().getApplicantResponseId().getJobPostingId().getTitle());
                if (interviewersts == null) {
                    status.setFeedbackStatus(false);
                    status.setInterviewerId(details.getInterviewers().get(i).getInterviewerId());
                    interviewerFeedback.add(status);

                    feedbackdetails.setInterviewerFeedback(interviewerFeedback);
                } else {
                    status.setFeedbackStatus(true);
                    status.setInterviewerId(details.getInterviewers().get(i).getInterviewerId());
                    interviewerFeedback.add(status);
                    feedbackdetails.setInterviewerFeedback(interviewerFeedback);
                }

            }
            System.out.println(feedbackdetails + "feed back");
            return new ApiResponse("2200", feedbackdetails, null);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ApiResponse findInterviewerFeedbackStatus(int interviewerId, int interviewId) {

        FeedBack status = feedbackRepository.findByInterviewerId(interviewerId, interviewId);
        if (status == null) {
            return null;
        } else {
            return new ApiResponse("2200", status, null);
        }
    }

}
