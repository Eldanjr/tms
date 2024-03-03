package com.bassure.applicantservice.serviceimplementation;

import com.bassure.applicantservice.model.JobPosting;
import com.bassure.applicantservice.model.JobRecruiter;
import com.bassure.applicantservice.model.applicantModel.Applicant;
import com.bassure.applicantservice.model.applicantModel.ApplicantDetails;
import com.bassure.applicantservice.model.applicantModel.ApplicantResponse;
import com.bassure.applicantservice.model.applicantModel.ApplicantResponseStatus;
import com.bassure.applicantservice.model.applicantModel.ApplicantStatus;
import com.bassure.applicantservice.model.scheduling.InterviewSchedule;
import com.bassure.applicantservice.model.scheduling.InterviewStatus;
import com.bassure.applicantservice.repo.*;
import com.bassure.applicantservice.response.*;
import com.bassure.applicantservice.service.TenentEmployeeService;
import com.bassure.applicantservice.service.ViewInterviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;
import org.springframework.data.domain.Pageable;

@Service
public class ViewInterviewStatusServiceImplementation implements ViewInterviewService {

    @Autowired
    InterviewScheduleRepository interviewScheduleRepo;

    @Autowired
    ApplicantDetailsRepository applicantRepo;

    @Autowired
    ApplicantRepository applicantBasicRepo;

    @Autowired
    ApplicantResponeRepository applicantRespone;

    @Autowired
    ApplicantEducationalRepo eduRepo;

    @Autowired
    ApplicantExperienceRepo expRepo;

    @Autowired
    TenentEmployeeServiceImplementation tenetEmployee;

    @Autowired
    JobPostingRepository jobPostingRepo;

    @Autowired
    ResponseCode code;

    @Autowired
    ResponseMessage message;

    @Autowired
    FilterServiceImplementation filterService;

    @Autowired
    private TenentEmployeeService tenentEmployeeService;

    public List<ViewScheduledInterviewResponse> getScheduledInterviews(List<Object[]> scheduledInterviews) {
        List<ViewScheduledInterviewResponse> scheduledInterviewResponse = new ArrayList<>();

        if (Objects.isNull(scheduledInterviews)) {
            return null;
        }

        try {
            for (Object[] row : scheduledInterviews) {
                ViewScheduledInterviewResponse response = new ViewScheduledInterviewResponse();
                response.setApplicantId((int) row[0]);
                response.setFirstName((String) row[1]);
                response.setMiddleName((String) row[2]);
                response.setLastName((String) row[3]);
                response.setApplicantStatus((String) row[4]);
                response.setResumePath((String) row[5]);
                response.setEmail((String) row[6]);
                response.setContactNo((String) row[7]);
                response.setInterviewDate((Timestamp) row[8]);
                response.setJobId((int) row[9]);
                response.setRecuriterId(tenentEmployeeService.viewProfileByEmployeeId((int) row[10]).getValue());
                response.setJobTitle((String) row[11]);
                response.setInterviewId((int) row[12]);
                response.setInterviewStatus((String) row[13]);
                response.setRoundName((String) row[14]);
                List<Integer> interviewerId = interviewScheduleRepo.getRecuritersIdByInterviewSchedule(response.getInterviewId());
                response.setPanel(tenetEmployee.getGroupOfEmployeesByIds(interviewerId));
                scheduledInterviewResponse.add(response);
            }

            return scheduledInterviewResponse;
        } catch (Exception ex) {
            return null;
        }

    }

    public ViewInterviewGroupByApplicantResponse groupDataByApplicant(int applicantId) {
        ViewInterviewGroupByApplicantResponse oneResponse = new ViewInterviewGroupByApplicantResponse();

        try {
            ApplicantDetails aDetails = applicantRepo.findById(applicantId).get();

            if (Objects.isNull(aDetails)) {
                return null;
            }
            oneResponse.setApplicant(aDetails);
            oneResponse.setAlreadtInterviewScheduled(jobPostingRepo.getScheduledInterviewsIfAvailable(applicantId) == null ? false : true);
            List<JobPosting> jobApplicant = jobPostingRepo.getJobPostingAppliedByApplicant(aDetails.getApplicantResponseId().getApplicantId().getApplicantId());
            List<JobPostResponse> jobPost = new ArrayList<>();
            List<CustomJobPostingResponse> jobs = new ArrayList<>();

            for (JobPosting job : jobApplicant) {

                CustomJobPostingResponse customJobPosting = new CustomJobPostingResponse();
                BeanUtils.copyProperties(job, customJobPosting);
                try {
                    customJobPosting.setJobRecruiterDetails(tenetEmployee.viewProfileByEmployeeId(interviewScheduleRepo.getRecuriterIdByApplicantDetailsId(aDetails.getApplicantDetailsId(), customJobPosting.getId())).getValue());
                } catch (Exception ex) {
                    customJobPosting.setJobRecruiterDetails(null);
                }
                jobs.add(customJobPosting);
            }
            oneResponse.setJobPost(jobs);
            return oneResponse;
        } catch (Exception ex) {
            return null;
        }

    }

    @Override
    public ApiResponse getApplicantInterviewsByJob(int applicantId, int jobPostId, String interviewStatus) {
        List<ViewInterviewWithoutApplicantResponse> viewInterviewResponse = new ArrayList<>();
        ApiResponse apiResponse = new ApiResponse();
        List<InterviewSchedule> interviews = new ArrayList<>();
        if (Objects.isNull(interviewStatus)) {
            interviews = interviewScheduleRepo.getInterviewByApplicantIdandJobPostingId(applicantId, jobPostId);
        } else {
            interviews = interviewScheduleRepo.getInterviewByApplicantIdandJobPostingIdWithInterviewStatus(applicantId, jobPostId, interviewStatus);

        }

        if (Objects.isNull(interviews)) {
            return apiResponse.builder().statusCode(code.getNotFound()).errors(Map.of(message.getSuccess(), message.getNoDataFound())).build();
        }

        for (InterviewSchedule interview : interviews) {
            ViewInterviewWithoutApplicantResponse iResp = new ViewInterviewWithoutApplicantResponse();
            BeanUtils.copyProperties(interview, iResp, "panel");
            ApiResponse apiResp = tenetEmployee.viewProfileByEmployeeId(interview.getScheduledBy());
            iResp.setJobRecruiterDetails(apiResp.getValue());
            try {
                List<Integer> interviewerId = interviewScheduleRepo.getRecuritersIdByInterviewSchedule(interview.getId());
                iResp.setPanel(tenetEmployee.getGroupOfEmployeesByIds(interviewerId));
            } catch (Exception ex) {
                iResp.setPanel(null);
            }
            viewInterviewResponse.add(iResp);
        }
        return apiResponse.builder().statusCode(code.getSuccess()).value(viewInterviewResponse).errors(null).build();
    }

    @Override
    public ApiResponse viewInterviewSchedulerDetails(int jobId) {
        CustomInterviewScheduledDetail interviewCustomJobResponse = new CustomInterviewScheduledDetail();
        ApiResponse apiResponse = new ApiResponse();
        CustomJobPostingResponse customJobResponse = new CustomJobPostingResponse();
        List<ViewScheduledInterviewResponse> applicantDetails = new ArrayList<>();

        List<Object[]> applicants = new ArrayList<>();

        applicants = interviewScheduleRepo.getApplicantsByJob(jobId);

        applicants.forEach(app -> {
            Object[] row = app;
            ViewScheduledInterviewResponse response = new ViewScheduledInterviewResponse();
            response.setApplicantId((int) row[0]);
            response.setFirstName((String) row[1]);
            response.setMiddleName((String) row[2]);
            response.setLastName((String) row[3]);
            response.setApplicantStatus((String) row[4]);
            response.setResumePath((String) row[5]);
            response.setEmail((String) row[6]);
            response.setContactNo((String) row[7]);
            response.setRecuriterId(tenentEmployeeService.viewProfileByEmployeeId((int) row[8]).getValue());

            applicantDetails.add(response);
        });
        interviewCustomJobResponse.setApplicantDetails(applicantDetails);

        JobPosting jobDetail = jobPostingRepo.findById(jobId).get();
        List<CustomJobRecuriterResponse> customRecuriterResponse = new ArrayList<>();
        for (JobRecruiter jr
                : jobDetail.getJobRecruiters()) {
            CustomJobRecuriterResponse recuriter = new CustomJobRecuriterResponse();
            recuriter.setRecuriterDetails(tenetEmployee.viewProfileByEmployeeId(jr.getRecruiterId()).getValue());
            BeanUtils.copyProperties(jr, recuriter);
            customRecuriterResponse.add(recuriter);
        }

        customJobResponse.setJobRecruiters(customRecuriterResponse);
        BeanUtils.copyProperties(jobDetail, customJobResponse);
        interviewCustomJobResponse.setJobPost(customJobResponse);

        apiResponse.setValue(interviewCustomJobResponse);
        apiResponse.setStatusCode(code.getSuccess());
        return apiResponse;
    }

    @Override
    public ApiResponse getApplicantInterviewsById(int applicantId) {
        ApiResponse apiResponse = new ApiResponse();
        ViewInterviewGroupByApplicantResponse response = new ViewInterviewGroupByApplicantResponse();

        response = groupDataByApplicant(applicantId);
        Map<String, String> errors = new HashMap<>();

        if (Objects.isNull(response)) {
            errors.put(message.getMessage(), message.getNoDataFound());
            apiResponse.setStatusCode(code.getNotFound());
            apiResponse.setErrors(errors);
            return apiResponse;
        }

        apiResponse.setStatusCode(code.getSuccess());
        apiResponse.setValue(response);
        return apiResponse;
    }

    @Override
    public ApiResponse viewInterviewByRecruiter(int id, String status, Date openDate, Pageable pageable) {
        ApiResponse apiResp = new ApiResponse();
        Map<String, String> errors = new HashMap<>();
        List<ViewScheduledInterviewResponse> scheduledInterviewResponse = new ArrayList<>();
        List<String> applicantStatus = new ArrayList<>();

        if (Objects.isNull(status)) {
            applicantStatus.add(InterviewStatus.SCHEDULED.toString());
            applicantStatus.add(InterviewStatus.WAITLIST.toString());
            applicantStatus.add(InterviewStatus.ONHOLD.toString());
            applicantStatus.add(InterviewStatus.RESCHEDULED.toString());
            applicantStatus.add(InterviewStatus.MOVEDTONEXTROUND.toString());
            applicantStatus.add(InterviewStatus.NEW.toString());
        } else {
            applicantStatus.add(status);
        }
        List<Object[]> scheduledInterviews = new ArrayList<>();
        int totalCount = 0;

        if (Objects.nonNull(openDate)) {
            Calendar calan = Calendar.getInstance();
            calan.setTime(openDate);
            calan.add(Calendar.HOUR_OF_DAY, 24);
            Date endDate = calan.getTime();
            Timestamp openTs = new Timestamp(openDate.getTime());
            Timestamp closeTs = new Timestamp(endDate.getTime());
            scheduledInterviews = interviewScheduleRepo.getScheduledApplicantsWithDate(id, applicantStatus, openTs, closeTs, pageable);
            totalCount = interviewScheduleRepo.getScheduledApplicantsWithDateCount(id, applicantStatus, endDate, endDate);
        } else {
            scheduledInterviews = interviewScheduleRepo.getScheduledApplicants(id, applicantStatus, pageable);
        }

        if (scheduledInterviews.isEmpty()) {
            errors.put(message.getMessage(), message.getNoDataFound());
            apiResp.setStatusCode(code.getNotFound());
            apiResp.setErrors(errors);
            return apiResp;
        }

        scheduledInterviewResponse = getScheduledInterviews(scheduledInterviews);

        if (Objects.isNull(scheduledInterviewResponse) || scheduledInterviewResponse.isEmpty()) {
            errors.put(message.getMessage(), message.getError());
            apiResp.setStatusCode(code.getNotFound());
            apiResp.setErrors(errors);
            return apiResp;
        }

        apiResp.setStatusCode(code.getSuccess());
        apiResp.setValue(scheduledInterviewResponse);
        return apiResp;
    }

    @Override
    public ApiResponse viewScheduledInterviewByBranch(int branchId, String status, Date openDate, Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        List<ViewScheduledInterviewResponse> scheduledInterviewResponse = new ArrayList<>();
        List<String> applicantStatus = new ArrayList<>();
        Map<String, String> errors = new HashMap<>();

        if (Objects.isNull(status)) {
            applicantStatus.add(InterviewStatus.SCHEDULED.toString());
            applicantStatus.add(InterviewStatus.WAITLIST.toString());
            applicantStatus.add(InterviewStatus.ONHOLD.toString());
            applicantStatus.add(InterviewStatus.RESCHEDULED.toString());
            applicantStatus.add(InterviewStatus.MOVEDTONEXTROUND.toString());
            applicantStatus.add(InterviewStatus.NEW.toString());
        } else {
            applicantStatus.add(status);
        }

        Object recuriterIds = tenetEmployee.getRecuriterIdByBranch(branchId);

        List<Object[]> scheduledInterviews = new ArrayList<>();
        int totalCount = 0;

        if (Objects.nonNull(openDate)) {
            Calendar calan = Calendar.getInstance();
            calan.setTime(openDate);
            calan.add(Calendar.HOUR_OF_DAY, 24);
            Date endDate = calan.getTime();
            Timestamp openTs = new Timestamp(openDate.getTime());
            Timestamp closeTs = new Timestamp(endDate.getTime());
            scheduledInterviews = interviewScheduleRepo.getScheduledApplicantsWithDate(recuriterIds, applicantStatus, openTs, closeTs, pageable);
            totalCount = interviewScheduleRepo.getScheduledApplicantsWithDateCount(recuriterIds, applicantStatus, endDate, endDate);
        } else {
            scheduledInterviews = interviewScheduleRepo.getScheduledApplicants(recuriterIds, applicantStatus, pageable);
            totalCount = interviewScheduleRepo.getScheduledApplicantsCount(recuriterIds, applicantStatus);
        }

        if (scheduledInterviews.isEmpty() || scheduledInterviews.size() == 0) {
            errors.put(message.getMessage(), message.getNoDataFound());
            apiResponse.setStatusCode(code.getNotFound());
            apiResponse.setErrors(errors);
            return apiResponse;
        }

        scheduledInterviewResponse = getScheduledInterviews(scheduledInterviews);

        if (Objects.isNull(scheduledInterviewResponse) || scheduledInterviewResponse.isEmpty() || scheduledInterviewResponse.size() == 0) {
            errors.put(message.getMessage(), message.getError());
            apiResponse.setStatusCode(code.getNotFound());
            apiResponse.setErrors(errors);
            return apiResponse;
        }

        apiResponse.setStatusCode(code.getSuccess());
        apiResponse.setValue(scheduledInterviewResponse);
        return apiResponse;
    }

    public ApiResponse getJobStatics(int jobId) {
        JobStaticsResponseInfo jobStatics = new JobStaticsResponseInfo();
        List<String> statu = new ArrayList<>();
        int totalCount = jobPostingRepo.findById(jobId).get().getNoOfVaccancies();
        for (int i = 0; i < InterviewStatus.values().length; i++) {
            statu.add(InterviewStatus.values()[i].toString());
        }
        for (String s : statu) {
            int count = jobPostingRepo.getJobTotalCountIdStatus(jobId, s);
            double percent = ((double) count / totalCount) * 100;
            DecimalFormat dec = new DecimalFormat("#");
            String p = dec.format(percent);

            if (s.equals("RESCHEDULED")) {
                jobStatics.setRescheduled(count);
                jobStatics.setRescheduledPercent(p);
            } else if (s.equals("CANCELLED")) {
                jobStatics.setCancelled(count);
                jobStatics.setCancelledPercent(p);
            } else if (s.equals("SCHEDULED")) {
                jobStatics.setScheduled(count);
                jobStatics.setScheduledPercent(p);
            } else if (s.equals("COMPLETED")) {
                jobStatics.setCompleted(count);
                jobStatics.setCompletedPercent(p);
            } else if (s.equals("INPROGRESS")) {
                jobStatics.setInprogressPercent(p);
                jobStatics.setInprogress(count);
            }
        }
        return ApiResponse.builder().statusCode(code.getSuccess()).value(jobStatics).build();
    }

    @Override
    public ApiResponse getApplicantById(int id) {
        ApiResponse apiResponse = new ApiResponse();
        Map<String, String> errors = new HashMap<>();
        Applicant applicant = applicantBasicRepo.findById(id).get();
        apiResponse.setValue(applicant);
        return apiResponse;

    }

    public List<ApplicantResponsesResponse> mapApplicantsForGetApplicantsByJobId(List<Object[]> applicants) {
        if (Objects.isNull(applicants)) {
            return null;
        }
        List<ApplicantResponsesResponse> applicantsResponse = new ArrayList<>();
        for (Object[] row : applicants) {
            ObjectMapper mapper = new ObjectMapper();
            ApiResponse employeeResp = tenentEmployeeService.viewProfileByEmployeeId((int) row[8]);
//            List<Object> interviewers = (List<Object>) employeeResp.getValue();
            Map<String, Object> interviewer = mapper.convertValue(employeeResp.getValue(), Map.class);

            ApplicantResponsesResponse respo = ApplicantResponsesResponse.builder()
                    .applicantId((int) row[0])
                    .firstName((String) row[1])
                    .middleName((String) row[2])
                    .lastName((String) row[3])
                    .contactNo((String) row[4])
                    .applicantStatus(ApplicantStatus.valueOf((String) row[5]))
                    .resume((String) row[6])
                    .applicantdetailsId((int) row[7])
                    .jobRecruiterName(interviewer.get("firstName").toString() + " " + interviewer.get("lastName").toString())
                    .build();
            applicantsResponse.add(respo);
        }
        return applicantsResponse;
    }

    @Override
    public ApiResponse getApplicantsByJobId(int jobId, String status) {
        System.out.println(status);
        List<Object[]> applicants = new ArrayList<>();
        if (Objects.isNull(status)) {
            applicants = applicantRepo.getApplicantByJobid(jobId);
        } else {
            applicants = applicantRepo.getApplicantByJobidAndStatus(jobId, status);
        }
        if (Objects.isNull(applicants)) {
            return ApiResponse.builder().statusCode(code.getNotFound()).value(null).errors(Map.of(message.getMessage(), message.getNoDataFound())).build();
        }
        List<ApplicantResponsesResponse> mappedApplicant = mapApplicantsForGetApplicantsByJobId(applicants);
        if (Objects.isNull(mappedApplicant)) {
            return ApiResponse.builder().statusCode(code.getNotFound()).value(null).errors(Map.of(message.getMessage(), message.getNoDataFound())).build();
        }
        return ApiResponse.builder().statusCode(code.getSuccess()).value(mappedApplicant).errors(null).build();
    }

    public String getRecuriterNameById(int recuriterId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object profile = tenentEmployeeService.viewProfileByEmployeeId(recuriterId).getValue();
            if (Objects.nonNull(profile)) {
                Map<String, Object> interviewer = mapper.convertValue(profile, Map.class);
                return interviewer.get("firstName").toString() + " " + interviewer.get("lastName").toString();
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public List<RecuriterStaticsResponse> mapRecuriterStaticsData(List<Object[]> statics) {
        List<RecuriterStaticsResponse> responses = new ArrayList<>();
        statics.forEach(s -> {
            RecuriterStaticsResponse response = new RecuriterStaticsResponse();
            response.setRecuriterName(getRecuriterNameById((int) s[0]));
            response.setAssignedCounts((BigDecimal) s[1]);
            response.setRecuriterId((int) s[0]);
            response.setHiredCounts((long) s[2]);
            responses.add(response);
        });
        return responses;
    }

    @Override
    public ApiResponse getRecuriterStaticsByJobId(int branchId, int jobId) {
        Object recuriterIds = tenetEmployee.getRecuriterIdByBranch(branchId);
        List<Object[]> statics = applicantRepo.getRecuriterStaticsByJobId(recuriterIds, jobId);
        List<RecuriterStaticsResponse> recuriterResponse = new ArrayList<>();

        if (Objects.nonNull(statics) || !statics.isEmpty()) {
            recuriterResponse = mapRecuriterStaticsData(statics);
        } else {
            return ApiResponse.builder().statusCode(code.getNotFound()).value(null).errors(Map.of(message.getMessage(), message.getNoDataFound())).build();
        }
        return ApiResponse.builder().statusCode(code.getSuccess()).value(recuriterResponse).errors(null).build();
    }

    @Override
    public ApiResponse getRandomRecuriterStatics(int branchId) {
        Object recuriterIds = tenetEmployee.getRecuriterIdByBranch(branchId);
        List<Object[]> statics = applicantRepo.getRandomRecuriterStatics(recuriterIds);
        List<RecuriterStaticsResponse> recuriterResponse = new ArrayList<>();

        if (Objects.nonNull(statics) || !statics.isEmpty()) {
            recuriterResponse = mapRecuriterStaticsData(statics);
        } else {
            return ApiResponse.builder().statusCode(code.getNotFound()).value(null).errors(Map.of(message.getMessage(), message.getNoDataFound())).build();
        }
        return ApiResponse.builder().statusCode(code.getSuccess()).value(recuriterResponse).errors(null).build();
    }

    public ApiResponse getApplicantStatusByJob(int jobId, int applicantId) {
        String applicantStatus = applicantRepo.getApplicantStatusByJob(jobId, applicantId);
        if (Objects.isNull(applicantId)) {
            return ApiResponse.builder().statusCode(code.getNotFound()).value(null).errors(Map.of(message.getMessage(), message.getNoDataFound())).build();
        }
        return ApiResponse.builder().statusCode(code.getSuccess()).value(applicantStatus).errors(null).build();
    }

    @Override
    public ApiResponse getRandomApplicantByBranch(int branchId) {
        List<String> applicantStatus = Arrays.asList(
                InterviewStatus.SCHEDULED.toString(), InterviewStatus.WAITLIST.toString(), InterviewStatus.ONHOLD.toString(), InterviewStatus.RESCHEDULED.toString(), InterviewStatus.MOVEDTONEXTROUND.toString(), InterviewStatus.NEW.toString()
        );

        Object recuriterIds = tenetEmployee.getRecuriterIdByBranch(branchId);
        List<Object[]> details = interviewScheduleRepo.getRandomApplicantByBranch(recuriterIds, applicantStatus);

        if (Objects.isNull(details) || details.isEmpty()) {
            return ApiResponse.builder().statusCode(code.getNotFound()).value(null).errors(Map.of(message.getMessage(), message.getSuccess())).build();
        }

        List<SimilarProfileResponse> response = mapdataForSimilarProfile(details);

        return ApiResponse.builder().statusCode(code.getSuccess()).value(response).errors(null).build();
    }

    @Override
    public ApiResponse getRandomApplicantByRecuriter(int recuriterId) {
        List<String> applicantStatus = Arrays.asList(
                InterviewStatus.SCHEDULED.toString(), InterviewStatus.WAITLIST.toString(), InterviewStatus.ONHOLD.toString(), InterviewStatus.RESCHEDULED.toString(), InterviewStatus.MOVEDTONEXTROUND.toString(), InterviewStatus.NEW.toString()
        );

        List<Object[]> details = interviewScheduleRepo.getRandomApplicantByBranch(recuriterId, applicantStatus);

        if (Objects.isNull(details) || details.isEmpty()) {
            return ApiResponse.builder().statusCode(code.getNotFound()).value(null).errors(Map.of(message.getMessage(), message.getSuccess())).build();
        }

        List<SimilarProfileResponse> response = mapdataForSimilarProfile(details);

        return ApiResponse.builder().statusCode(code.getSuccess()).value(response).errors(null).build();
    }

    public List<SimilarProfileResponse> mapdataForSimilarProfile(List<Object[]> details) {
        List<SimilarProfileResponse> responses = new ArrayList<>();
        details.forEach(d -> {
            responses.add(SimilarProfileResponse.builder().applicantDetailsId((int) d[0]).applicantName((String) d[1] + " " + (String) d[2] + " " + (String) d[3]).jobTitle((String) d[4]).experience((String) d[5]).build());
        });
        return responses;
    }

    @Override
    public ApiResponse updateApplicantResponseStatus(int applicantResponseId, String status) {
        ApplicantResponse getApplicantResponse = applicantRespone.findById(applicantResponseId).get();

        if (ApplicantResponseStatus.valueOf(status) != null) {
            getApplicantResponse.setApplicantResponseStatus(ApplicantResponseStatus.valueOf(status));
            Applicant applicant = getApplicantResponse.getApplicantId();
            applicant.setApplicantStatus(ApplicantStatus.valueOf(status));
            getApplicantResponse.setApplicantId(applicant);
            getApplicantResponse = applicantRespone.save(getApplicantResponse);
            if (getApplicantResponse.getApplicantResponseStatus() == ApplicantResponseStatus.valueOf(status)) {
                return ApiResponse.builder().statusCode(code.getSuccess()).value(message.getSuccess()).errors(null).build();
            } else {
                return ApiResponse.builder().statusCode(code.getFailed()).value(null).errors(Map.of(message.getMessage(), message.getNoDataFound())).build();
            }
        } else {
            return ApiResponse.builder().statusCode(code.getFailed()).value(null).errors(Map.of(message.getMessage(), message.getNoDataFound())).build();
        }
    }

}
