package com.bassure.applicantservice.serviceimplementation;

import com.bassure.applicantservice.model.*;
import com.bassure.applicantservice.model.applicantModel.ApplicantDetails;
import com.bassure.applicantservice.model.applicantModel.ApplicantStatus;
import com.bassure.applicantservice.model.scheduling.*;
import com.bassure.applicantservice.repo.*;
import com.bassure.applicantservice.request.InterviewScheduleRequest;
import com.bassure.applicantservice.request.InterviewScheduleStatusRequest;
import com.bassure.applicantservice.response.*;
import com.bassure.applicantservice.service.Scheduling;
import com.bassure.applicantservice.service.TenentEmployeeService;
import com.bassure.applicantservice.util.ServiceCall;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
@Slf4j
public class ScheduleImplementation implements Scheduling {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Value("${applicantInterviewSubject}")
    private String applicantInterviewSubject;

    @Value("${interviewscheduleBody}")
    private String interviewscheduleBody;
    @Value("${interviewerMailSubject}")
    private String interviewerMailSubject;

    @Value("${interviewerMailBody}")
    private String interviewerMailBody;
    @Autowired
    private ServiceCall serviceCall;

    @Autowired
    private ResponseMessage message;
    @Autowired
    private ResponseCode code;

    @Autowired
    private InterviewRoundRepository interviewRoundRepository;

    private static final Logger logger = LoggerFactory.getLogger(ScheduleImplementation.class);
    @Autowired
    private InterviewScheduleRepository interviewScheduleRepository;

    @Autowired
    Environment env;

    @Autowired
    private JobRecruiterRepository jobRecruiterRepository;

    private String path = "/Send-email";


    @Autowired
    private InterviewPanelRepository interviewPanelRepository;

    @Autowired
    private ApplicantDetailsRepository applicantDetailsRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    TenentEmployeeService tenentEmployeeService;

    @Autowired
    InterviewRoundsRepo rounds;

    @Autowired
    private TenentEmployeeServiceImplementation tenentEmployeeServiceImplementation;

    @Autowired
    private InterviewHistoryRepository interviewHistoryRepository;

    @Override
    public ApiResponse addRounds(InterviewRound round) {
        try {
            List<InterviewRound> allRounds = rounds.findAll();
            String roundName = round.getName().toUpperCase();
            round.setName(roundName);
            rounds.save(round);
        } catch (Exception ex) {
            throw ex;
        }
        return new ApiResponse(code.getSuccess(), message.getSuccess(), null);
    }

    @Override
    public ApiResponse roundforApplicant(int applicantId, int jobId) {
        List<InterviewRound> roundList = rounds.findByinterviewRounds(applicantId, jobId);
        if (roundList.size() > 0) {
            return new ApiResponse(code.getSuccess(), roundList, null);
        } else {
            return new ApiResponse(code.getNotFound(), null, Map.of(message.getErrorKey(), message.getNoDataFound()));
        }
    }

    @Override
    @Transactional
    public ApiResponse scheduleInterview(InterviewScheduleRequest schedule) {
        List<InterviewSchedule> responseList = new ArrayList<>();
        Optional<InterviewRound> round = interviewRoundRepository.findById(schedule.getInterviewRound());
        Optional<JobRecruiter> recruiterwithJd = jobRecruiterRepository.findById(schedule.getJobRecruiterId());
        Optional<ApplicantDetails> applicants = applicantDetailsRepository.findById(schedule.getApplicantId());

        List<InterviewSchedule> interviewScheduleList = interviewScheduleRepository.findByjobRecruiterId(schedule.getJobRecruiterId());
        List<Interviewers> interviewPanelList = new ArrayList<>();
        logger.info("{}", recruiterwithJd);
        boolean isFree = true;
        InterviewSchedule interviewSchedule = new InterviewSchedule();
        BeanUtils.copyProperties(schedule, interviewSchedule);
        interviewSchedule.setModeOfInterview(schedule.getMode());

        System.out.println(interviewSchedule);
        List<InterviewSchedule> schedulesList = new ArrayList<>();
        for (int id : schedule.getInterviewerId()) {
            InterviewSchedule response = new InterviewSchedule();
            List<Interviewers> panelResponse = interviewPanelRepository.findByinterviewerId(id);
            logger.info("{}", panelResponse);
            interviewPanelList.addAll(panelResponse);
            response = interviewScheduleRepository.findbystartedAtANDendedAt(id, schedule.getStartedAt(), schedule.getEndedAt());
            responseList.add(response);

            if (Objects.nonNull(response)) {
                isFree = false;
                schedulesList.add(response);
            }
        }
        if (isFree) {
            int desiredCount = 0;
            int count = 0;
            if (recruiterwithJd.isPresent()) {
                count = recruiterwithJd.get().getAssignedCounts();
            } else {
                return new ApiResponse(code.getNotValid(), null, Map.of(message.getErrorKey(), message.getNoDataFound()));
            }
            round.ifPresent(interviewSchedule::setInterviewRound);
            if (recruiterwithJd.get().getStatus().equals(JobRecruiterStatus.OPEN)) {
                recruiterwithJd.get().setStatus(JobRecruiterStatus.INPROGRESS);
            }
            interviewSchedule.setJobRecruiter(recruiterwithJd.get());
            applicants.ifPresent(interviewSchedule::setApplicantId);
            interviewSchedule.setInterviewStatus(InterviewStatus.SCHEDULED);
            interviewSchedule.setInterviewers(interviewPanelList);
            logger.info("{}", interviewSchedule);
            for (InterviewSchedule inter : interviewScheduleList) {
                if (inter.getJobRecruiter() == interviewSchedule.getJobRecruiter() && inter.getApplicantId() != interviewSchedule.getApplicantId() && inter.getInterviewRound().getId() == 1) {
                    desiredCount++;
                }

                if (inter.getApplicantId() == interviewSchedule.getApplicantId() && inter.getInterviewRound() == interviewSchedule.getInterviewRound()) {
                    return new ApiResponse("2231", null, Map.of("errors", "The applicant is already assigned in this level"));
                }
            }
            logger.info(" count{}", desiredCount);
            if (desiredCount <= count) {
                JobPosting posting = jobPostingRepository.findByjobRecruiters(interviewSchedule.getJobRecruiter());
                if (posting.getStatus().equals(JobPostingStatus.OPEN)) {
                    posting.setStatus(JobPostingStatus.INPROCESS);
                }
                jobPostingRepository.save(posting);
                interviewSchedule = interviewScheduleRepository.save(interviewSchedule);
                ApplicantDetails applicantDetails = applicantDetailsRepository.findById(schedule.getApplicantId()).get();
//                JobRecruiter jobRecruiter = jobRecruiterRepository.findById(schedule.getJobRecruiterId()).get();
//                applicantDetails.setJobRecruiterId(jobRecruiter);
                applicantDetails.getApplicantResponseId().getApplicantId().setApplicantStatus(ApplicantStatus.INPROCESS);
                applicantDetailsRepository.save(applicantDetails);
                List<InterviewerResponse> interviewerResponseList = new ArrayList<>();
                for (int id : schedule.getInterviewerId()) {
                    InterviewerResponse response = findInterviewEmail(id);
                    interviewerResponseList.add(response);
                    Interviewers panel = new Interviewers();
                    panel.setInterviewerId(id);
                    panel.setInterviewId(interviewSchedule);
                    interviewPanelRepository.save(panel);
                }
                String applicantBody = interviewscheduleBody + " your interview is scheduled at " + interviewSchedule.getStartedAt().toLocalDateTime();
                String applicantSubject = applicantInterviewSubject;
                sendMail(applicantDetails.getApplicantResponseId().getApplicantId().getEmail(), path, applicantBody, applicantSubject);
                interviewerMailBody = interviewerMailBody + "for the applicant named " + applicantDetails.getApplicantResponseId().getApplicantId().getFirstName() + " on " + interviewSchedule.getStartedAt().toLocalDateTime();
                for (InterviewerResponse response : interviewerResponseList) {
                    sendMail(response.getEmail(), path, interviewerMailBody, interviewerMailSubject);
                }
                return new ApiResponse(code.getSuccess(), "Interview Scheduled Successfully", null);
            } else {
                return new ApiResponse(code.getNoChangeRequired(), null, Map.of("errors", "You Have Scheduled All the Jobs Allocated...! "));
            }

        }
        Map<String, Integer> interviewers = new HashMap<>();
        for (Interviewers panel1 : interviewPanelList) {
            interviewers.put("id", panel1.getInterviewerId());
        }

        return new ApiResponse(code.getAlreadyExists(), null, Map.of("errors", "There is already a schedule for the interviewer from" + schedulesList.iterator().next().getStartedAt() + " to " + schedulesList.iterator().next().getEndedAt()));

    }

    @Override
    public ApiResponse findAvailableInterviewer(int branchId) {
        System.out.println("executed in interviewer");
        ApiResponse interviewersApi = tenentEmployeeService.viewInterviewerByBranchId(branchId);
        List<Object> interviewersList = (List<Object>) interviewersApi.getValue();
        ObjectMapper mapper = new ObjectMapper();
        List<EmployeeRespone> employeeList = new ArrayList<>();
        logger.info("{}", interviewersList);
        for (Object interviewers : interviewersList) {
            EmployeeRespone employee = new EmployeeRespone();

            try {
                Map<String, Object> interviewer = mapper.convertValue(interviewers, Map.class);
                int id = (Integer) interviewer.get("employeeId");
                String name = interviewer.get("firstName").toString();
                String email = interviewer.get("email").toString();
                employee.setEmployeeId(id);
                employee.setFirstName(name);
                employee.setEmail(email);
                employeeList.add(employee);
                logger.info("{}", employee);
                logger.info("{} list", employeeList);
            } catch (Exception e) {
                e.printStackTrace();
                return new ApiResponse(code.getNotFound(), null, Map.of("errors", "cannot fetch interviewers"));
            }
        }
        if (employeeList.isEmpty()) {
            return new ApiResponse(code.getNotFound(), null, Map.of("errors", "no Interviewer Allocated in this branch"));
        }
        System.out.println(employeeList);
        return new ApiResponse(code.getSuccess(), employeeList, null);
    }

    @Override
    public ApiResponse findJobsforRecruiter(int id) {
        logger.info("{}", id);
        List<Map<Integer, String>> jobsforRecruiter = jobRecruiterRepository.findByrecruiter(id);
        if (!jobsforRecruiter.isEmpty()) {
            return new ApiResponse(code.getSuccess(), jobsforRecruiter, null);
        }
        return new ApiResponse(code.getNotFound(), null, Map.of("errors", "No jobs for this recruiter"));
    }

    @Override
    public ApiResponse getJobRecruiter(int jobId, int recruiterId) {
        List<Object[]> jobRecruiter = jobRecruiterRepository.getByJobPostingAndRecruiterId(jobId, recruiterId);
        ScheduleJobRecruiterResponse jobRecuriterResponse = new ScheduleJobRecruiterResponse();

        for (Object[] row : jobRecruiter) {
            jobRecuriterResponse.setJobRecruiterId((Integer) row[0]);
            jobRecuriterResponse.setJobTitle((String) row[1]);
            jobRecuriterResponse.setJobId((Integer) row[2]);
        }
        return new ApiResponse(code.getSuccess(), jobRecuriterResponse, null);
    }

    public ApiResponse getInterviewProgressOfRecruiter(int recruiterId) {
        List<Object[]> progress = interviewScheduleRepository.getRecruiterInterviewProgress(recruiterId);
        List<InterviewProgress> interviewProgressList = new ArrayList<>();
        for (Object[] interview : progress) {
            InterviewProgress interviewProgress = new InterviewProgress();

            interviewProgress.setInterviewScheduleId((int) interview[0]);
            interviewProgress.setInterviewLevel((String) interview[1]);
            interviewProgress.setStartTime((Timestamp) interview[2]);
            interviewProgress.setEndTime((Timestamp) interview[3]);
            String interviewIds = (String) interview[4];
            String[] idStrings = interviewIds.split(","); // Split the string by comma
            int[] ids = new int[idStrings.length];
            List<Object> interviewers = new ArrayList<>();
            for (int i = 0; i < idStrings.length; i++) {
                ids[i] = Integer.parseInt(idStrings[i].trim()); // Convert each id string to an integer
            }
            for (Integer id : ids) {
                interviewers.add(tenentEmployeeService.viewInterviewer(id).getValue());
            }
            interviewProgress.setInterviewersId(interviewers);
            interviewProgressList.add(interviewProgress);
        }
        return new ApiResponse(code.getSuccess(), interviewProgressList, null);

    }

    @Override
    public ApiResponse updateTheStatusAndFeedBack(int id, InterviewScheduleRequest updateInfo) {
        logger.info("{}", updateInfo);
        Optional<InterviewSchedule> interviewSchedule = interviewScheduleRepository.findById(id);
        if (interviewSchedule.isPresent()) {
            interviewSchedule.get().setInterviewStatus(updateInfo.getInterviewStatus());
            interviewSchedule.get().setFeedback(updateInfo.getFeedback());
            InterviewSchedule schedule = interviewScheduleRepository.save(interviewSchedule.get());
            return new ApiResponse("2200", schedule, null);
        } else {
            return new ApiResponse("2215", null, Map.of("errors", "No such Interview Scheduled"));
        }

    }

    @Override
    public ApiResponse findInterviewRounds() {
        return new ApiResponse(code.getSuccess(), interviewRoundRepository.findAll(), null);
    }

    @Override
    public ApiResponse findScheduledInterviewsById(int id) {
        List<Object[]> scheduledInterviews = interviewScheduleRepository.findSecheduledInterviewsByJobRecruiterId(id);
        List<ScheduledInterviewResponse> customScheduledInterviewResponses = new ArrayList<>();
        for (Object[] scheduledInterview : scheduledInterviews) {
            List<Integer> scheduledInterviewersId = interviewPanelRepository.findByInterviewerByinterviewId((int) scheduledInterview[0]);
            List<Object> scheduledInterviewers = new ArrayList<>();
            for (Integer interviewerId : scheduledInterviewersId) {
                scheduledInterviewers.add(tenentEmployeeService.viewInterviewer(interviewerId).getValue());
            }
            customScheduledInterviewResponses.add(ScheduledInterviewResponse.builder().id((int) scheduledInterview[0]).roundName(interviewRoundRepository.findById((int) scheduledInterview[1]).get().getName()).applicant(applicantDetailsRepository.findById((int) scheduledInterview[2]).get()).mode((String) scheduledInterview[3].toString()).status((String) scheduledInterview[4].toString()).feedBack(Objects.nonNull(scheduledInterview[5]) ? scheduledInterview[5].toString() : "-").startedAt(scheduledInterview[6].toString()).endedAt(scheduledInterview[7].toString()).location(Objects.nonNull(scheduledInterview[8]) ? scheduledInterview[5].toString() : "-").interviewersList(scheduledInterviewers).build());
        }
        return new ApiResponse(code.getSuccess(), customScheduledInterviewResponses, null);
    }

    @Override
    public ApiResponse findAllScheduledInterviewsById(int id) {
        Object recutiter = tenentEmployeeService.getRecuriterIdByBranch(id);

        List<InterviewSchedule> schedule = interviewScheduleRepository.findingInterviewbyRecuriterIds(recutiter);

        List<AllScheduledInterviewResponse> resp = new ArrayList<>();

        for (InterviewSchedule scheduledInterview : schedule) {
            List<Object> scheduledInterviewers = new ArrayList<>();
            for (Interviewers inter : scheduledInterview.getInterviewers()) {
                scheduledInterviewers.add(tenentEmployeeService.viewInterviewer(inter.getInterviewerId()).getValue());
            }

            ApiResponse recriterName = tenentEmployeeService.viewProfileByEmployeeId(scheduledInterview.getJobRecruiter().getRecruiterId());

            AllScheduledInterviewResponse sh = AllScheduledInterviewResponse.builder().id(scheduledInterview.getId()).mode(scheduledInterview.getModeOfInterview().toString()).status(scheduledInterview.getInterviewStatus().toString()).feedBack(scheduledInterview.getFeedback()).startedAt(scheduledInterview.getStartedAt().toString()).endedAt(scheduledInterview.getEndedAt().toString()).location(scheduledInterview.getLocation()).roundName(scheduledInterview.getInterviewRound().getName()).applicant(scheduledInterview.getApplicantId()).interviewersList(scheduledInterviewers).title(scheduledInterview.getJobRecruiter().getJobPosting().getTitle()).recuiter(recriterName.getValue()).build();
            resp.add(sh);

        }
        return new ApiResponse(code.getSuccess(), resp, null);
    }

    @Override
    public ApiResponse findFilterAllScheduledInterviewsById(String searchfield, String values) {

        return null;
    }

    @Override
    public ApiResponse viewScheduledInterviews(int id) {
        return new ApiResponse(code.getSuccess(), interviewScheduleRepository.findById(id).get(), null);
    }

    //     @Override
//    public List<InterviewSchedule> getScheduledInterviews(int interviewId) {
//        List<InterviewSchedule> schedule = interviewScheduleRepository.getScheduledInterview(interviewId);
//        return schedule;
    @Override
    public ApiResponse getinterviewbyinterviewer(int id) {
        List<InterviewSchedule> interviewSchedule = interviewScheduleRepository.getinterviewbyinterviewer(id);
        if (interviewSchedule.isEmpty()) {
            return ApiResponse.builder().statusCode(code.getNotFound()).errors(Map.of(message.getErrorKey(), message.getNoDataFound())).build();
        }
        return ApiResponse.builder().statusCode(code.getSuccess()).value(interviewSchedule).build();
    }

    @Override
    public ApiResponse updateStatusByInterViewer(InterviewScheduleStatusRequest interviewScheduleStatusRequest) {
        InterviewSchedule schedule = interviewScheduleRepository.findById(interviewScheduleStatusRequest.getInterviewId()).get();

        InterviewSchedule updateSchedule = new InterviewSchedule();
        BeanUtils.copyProperties(schedule, updateSchedule, "panel");

        if (updateSchedule.getInterviewStatus().equals(InterviewStatus.valueOf(interviewScheduleStatusRequest.getStatus()))) {
            return new ApiResponse(code.getAlreadyExists(), message.getAlreadyExists(), null);
        }

        updateSchedule.setInterviewStatus(InterviewStatus.valueOf(interviewScheduleStatusRequest.getStatus()));
        InterviewSchedule sche = interviewScheduleRepository.save(updateSchedule);
        System.out.println("jhgsafhjasfjsifdhjsfjsgdfsdfhsfdhdsfiu" + interviewScheduleStatusRequest.getRemarks());
        interviewHistoryServices(interviewScheduleStatusRequest.getInterviewerId(), sche, interviewScheduleStatusRequest.getRemarks());
        return new ApiResponse(code.getSuccess(), message.getSuccess(), null);

    }

    @Override
    public InterviewHistory interviewHistoryServices(int interviewerId, InterviewSchedule interviewId, String remarks) {
        InterviewHistory interview = new InterviewHistory();
        interview.setInterviewId(interviewId);
        interview.setRemarks(remarks);
        interview.setModifiedBy(interviewerId);
        interview.setModifiedAt(Timestamp.valueOf(java.time.LocalDateTime.now()));
        interviewHistoryRepository.save(interview);
        return interview;
    }

    @Override
    public ApiResponse getinterview(int interviewId) {
        Optional<InterviewSchedule> interviewSchedule = interviewScheduleRepository.findById(interviewId);
        if (interviewSchedule.isPresent()) {
            return new ApiResponse(code.getSuccess(), interviewSchedule, null);
        } else {
            return new ApiResponse(code.getNotFound(), null, Map.of(message.getErrorKey(), message.getNoDataFound()));
        }
    }

    @Override
    @Transactional
    public ApiResponse interviewReschedule(InterviewScheduleRequest interviewScheduleRequest) {
        logger.info("{}", interviewScheduleRequest);
        InterviewSchedule interviewSchedule = new InterviewSchedule();
        if (Objects.nonNull(interviewScheduleRequest)) {
            Optional<InterviewRound> round = interviewRoundRepository.findById(interviewScheduleRequest.getInterviewRound());
            Optional<JobRecruiter> jobRecruiter = jobRecruiterRepository.findById(interviewScheduleRequest.getJobRecruiterId());
            List<Interviewers> interviewersList = new ArrayList<>();

            Optional<ApplicantDetails> applicantDetails = applicantDetailsRepository.findById(interviewScheduleRequest.getApplicantId());
            BeanUtils.copyProperties(interviewScheduleRequest, interviewSchedule, "applicantId");
            interviewSchedule.setModeOfInterview(interviewScheduleRequest.getMode());
            applicantDetails.ifPresent(interviewSchedule::setApplicantId);
            round.ifPresent(interviewSchedule::setInterviewRound);
            interviewSchedule.setInterviewStatus(InterviewStatus.RESCHEDULED);
            jobRecruiter.ifPresent(interviewSchedule::setJobRecruiter);
            interviewPanelRepository.deleteByinterviewId(interviewScheduleRequest.getId());

            for (int interview : interviewScheduleRequest.getInterviewerId()) {
                Interviewers members = new Interviewers();
                members.setInterviewId(interviewSchedule);
                members.setInterviewerId(interview);
                interviewersList.add(members);
            }
            interviewPanelRepository.saveAll(interviewersList);

            interviewScheduleRepository.save(interviewSchedule);
            return new ApiResponse(code.getSuccess(), message.getReschedule(), null);
        } else {
            return new ApiResponse(code.getNotFound(), null, Map.of(message.getErrorKey(), message.getNoDataFound()));
        }
    }

    @Override
    public ApiResponse cancelInterview(int interviewId) {
        Optional<InterviewSchedule> interviewSchedule = interviewScheduleRepository.findById(interviewId);
        if (interviewSchedule.isPresent()) {
            interviewSchedule.get().setInterviewStatus(InterviewStatus.CANCELLED);
            interviewScheduleRepository.save(interviewSchedule.get());
            return new ApiResponse(code.getSuccess(), "Interview Has Been Cancelled...!", null);
        } else {
            return new ApiResponse(code.getServerError(), null, Map.of(message.getErrorKey(), message.getServerError()));
        }
    }

    @Override
    public ApiResponse changeInterviewStatus(int interviewId, String status) {
        Optional<InterviewSchedule> interviewSchedule = interviewScheduleRepository.findById(interviewId);
        if (interviewSchedule.isPresent()) {
            switch (status) {
                case "FEEDBACKPENDING":
                    interviewSchedule.get().setInterviewStatus(InterviewStatus.FEEDBACKPENDING);
                    break;
                case "COMPLETED":
                    interviewSchedule.get().setInterviewStatus(InterviewStatus.COMPLETED);
                    break;
                case "ONHOLD":
                    interviewSchedule.get().setInterviewStatus(InterviewStatus.ONHOLD);
                    break;
                case "WAITLIST":
                    interviewSchedule.get().setInterviewStatus(InterviewStatus.WAITLIST);
                    break;
            }
            interviewScheduleRepository.save(interviewSchedule.get());
            return new ApiResponse(code.getSuccess(), "Interview Has Been Cancelled...!", null);
        } else {
            return new ApiResponse(code.getServerError(), null, Map.of(message.getErrorKey(), message.getServerError()));
        }
    }

    public ApiResponse sendMail(String email, String path, String subject, String body) {
        try {
            return serviceCall.performPost(message.getApplicantServiceBaseEmailUrl(), path, email, ApiResponse.class, subject, body);

        } catch (Exception ex) {
            return new ApiResponse(code.getServerError(), null, Map.of(message.getErrorKey(), message.getServerError()));
        }
    }

    public InterviewerResponse findInterviewEmail(int id) {
        ApiResponse response = tenentEmployeeServiceImplementation.viewInterviewer(id);
        ObjectMapper mapper = new ObjectMapper();
        InterviewerResponse interviewerResponse = new InterviewerResponse();
        try {
            Object interviewer = response.getValue();
           Map<String,Object> interviewerMap = mapper.convertValue(interviewer, Map.class);
//            System.out.println(interviewerResponse+"response");
            interviewerResponse.setFirstName((String)interviewerMap.get("firstName"));
            interviewerResponse.setEmail((String)interviewerMap.get("email"));
            interviewerResponse.setEmployeeId((int) interviewerMap.get("employeeId"));
            return interviewerResponse;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
