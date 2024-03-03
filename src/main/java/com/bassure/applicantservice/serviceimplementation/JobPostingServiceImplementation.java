package com.bassure.applicantservice.serviceimplementation;

import com.bassure.applicantservice.model.*;
import com.bassure.applicantservice.repo.*;
import com.bassure.applicantservice.request.JobPostingRequest;
import com.bassure.applicantservice.request.JobTemplateRequest;
import com.bassure.applicantservice.response.*;
import com.bassure.applicantservice.service.JobPostingService;
import com.bassure.applicantservice.service.TenentEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class JobPostingServiceImplementation implements JobPostingService {

    @Autowired
    JobPostingRepository jobPostingRepo;

    @Autowired
    JobHistoryRepository jobHistoryRepo;

    @Autowired
    SkillRepository skillRepo;

    @Autowired
    JobRecruiterRepository jobRecruiterRepo;

    @Autowired
    JobTemplateRepository jobTemplateRepo;

    private static final Logger logger = LoggerFactory.getLogger(JobPostingServiceImplementation.class);

    @Autowired
    ResponseCode code;

    @Autowired
    ResponseMessage message;

    @Autowired
    ResponseMessage responseMessage;

    @Autowired
    InterviewScheduleRepository interviewScheduledRepo;

    @Autowired
    SchedularImplementation schedularImple;

    @Autowired
    TenentEmployeeService tenentEmployeeService;

    @Override
    public ApiResponse getjobsIprocess(int branchId) {
        List<Object> jobs = jobPostingRepo.getJobsInprocess(branchId);
        if (!jobs.isEmpty()) {
            logger.info("{}", jobs.iterator().next());
            return new ApiResponse(code.getSuccess(), jobs, null);
        } else {
            return new ApiResponse(code.getNotFound(), null, Map.of(message.getErrorKey(), message.getNoDataFound()));
        }
    }

    @Override
    public ApiResponse addJobPosting(JobPostingRequest joBPostingRequest) {

        JobPosting jobPosting = new JobPosting();

        BeanUtils.copyProperties(joBPostingRequest, jobPosting);

        //    List<JobRecruiter> jobRecruiters = new ArrayList<>();
        List<Skill> skills = new ArrayList<>();

        for (int i : joBPostingRequest.getSkillIds()) {
            Optional<Skill> skillById = skillRepo.findById(i);
            if (skillById.isEmpty()) {
                return ApiResponse.builder().statusCode(code.getNotFound()).errors(Map.of(message.getErrorKey(), message.getSkillNotFound())).build();
            }
            skills.add(skillById.get());
        }

        jobPosting.setSkills(skills);

        jobPosting.setCreatedAt(Timestamp.valueOf(java.time.LocalDateTime.now()));
        jobPosting.setModifiedAt(Timestamp.valueOf(java.time.LocalDateTime.now()));
        JobPosting addedJobPosting = jobPostingRepo.save(jobPosting);

        Date now = new Date();
        long delay = (addedJobPosting.getCloseDate().getTime() - now.getTime()) / (60 * 1000);
        schedularImple.jobStatusChangeSchedular(delay, addedJobPosting.getId());

        for (Map<String, Integer> entry : joBPostingRequest.getRecruitersIdWithAssignedCounts()) {
            entry.forEach((key, value) -> jobRecruiterRepo.save(JobRecruiter.builder().jobPosting(addedJobPosting).recruiterId(Integer.parseInt(key)).assignedCounts(value).status(JobRecruiterStatus.OPEN).assignedBy(joBPostingRequest.getCreatedBy()).assignedAt(Timestamp.valueOf(java.time.LocalDateTime.now())).modifiedBy(joBPostingRequest.getModifiedBy()).modifiedAt(Timestamp.valueOf(java.time.LocalDateTime.now())).build()));
        }

        return ApiResponse.builder().statusCode(code.getSuccess()).value(message.getAddMessage()).build();
    }

    @Override
    public ApiResponse getRecuiterDetailsForJob(int jobPosting, int branchId) {

        List<RecruiterProcess> recruiterProcesses = new ArrayList<>();
        List<Object[]> results = jobPostingRepo.getRecruiterStatusByJobId(jobPosting);


        if (results.isEmpty()) {
            List<RecruiterProcess> recruiterProcesses1 = new ArrayList<>();
            List<Object[]> rec = jobPostingRepo.findRecruitersWithAssignedCounts(jobPosting);
            List<RecruiterProcessWithAssignedCounts> recWithCounts = new ArrayList<>();
            for (Object[] obj : rec) {
                RecruiterProcessWithAssignedCounts rec1 = new RecruiterProcessWithAssignedCounts();
                rec1.setRecruiterId((int) obj[0]);
                rec1.setAssignedCounts((int) obj[1]);
                recWithCounts.add(rec1);
            }
            return new ApiResponse("2200", recWithCounts, null);
        } else {
            Map<Integer, RecruiterProcess> statusMap = new HashMap<>();
            // Iterate over the SQL result and aggregate the counts
            for (Object[] row : results) {

                int id = (int) row[1];

                String status = (String) row[0];

                if (!statusMap.containsKey(id)) {
                    RecruiterProcess statusInfo = new RecruiterProcess();
                    statusInfo.setRecruiterId(id);
                    statusMap.put(id, statusInfo);
                }

                RecruiterProcess statusInfo = statusMap.get(id);
                statusInfo.setAssignedCounts((int) row[2]);

                switch (status) {
                    case "NEW":
                        statusInfo.setNewApplicants(statusInfo.getNewApplicants() + 1);
                        break;
                    case "OFFERED":
                        statusInfo.setPositionFilled(statusInfo.getPositionFilled() + 1);
                        break;
                    case "HIRED":
                        statusInfo.setPositionFilled(statusInfo.getPositionFilled() + 1);
                        break;
                    case "INPROCESS":
                        statusInfo.setInProcess(statusInfo.getInProcess() + 1);
                        break;
                    case "REJECTED":
                        statusInfo.setInProcess(statusInfo.getInProcess() + 1);
                        break;
                    // Add more cases for other status values if needed
                }

            }
            return new ApiResponse("2200", new ArrayList(statusMap.values()), null);
        }

    }

    @Override
    public ApiResponse updateJobPosting(int jobPostingId, JobPostingRequest joBPostingRequest) {

        Optional<JobPosting> jobPost = jobPostingRepo.findById(jobPostingId);
        if (jobPost.isEmpty()) {
            return ApiResponse.builder().statusCode(code.getNotFound()).value(Map.of(message.getErrorKey(), message.getNoDataFound())).build();
        }
        JobPosting jobPosting = jobPost.get();

        BeanUtils.copyProperties(joBPostingRequest, jobPosting);

        List<Skill> skills = new ArrayList<>();

        for (int i : joBPostingRequest.getSkillIds()) {
            Optional<Skill> skillById = skillRepo.findById(i);
            if (skillById.isEmpty()) {
                return ApiResponse.builder().statusCode(code.getNotFound()).errors(Map.of(message.getErrorKey(), message.getSkillNotFound())).build();
            }
            skills.add(skillById.get());
        }

        jobPosting.setSkills(skills);
        jobPosting.setModifiedAt(Timestamp.valueOf(java.time.LocalDateTime.now()));

        JobPosting addedJobPosting = jobPostingRepo.save(jobPosting);
        Date now = new Date();
        long delay = (addedJobPosting.getCloseDate().getTime() - now.getTime()) / (60 * 1000);
        schedularImple.jobStatusChangeSchedular(delay, addedJobPosting.getId());

        List<JobRecruiter> recruitersList = new ArrayList<>();

        for (Map<String, Integer> recruiters : joBPostingRequest.getRecruitersIdWithAssignedCounts()) {

            for (Map.Entry<String, Integer> entry : recruiters.entrySet()) {
                JobRecruiter findByRecruiterIdAndJobId = jobRecruiterRepo.findByJobPostingIdAndRecruiterId(addedJobPosting.getId(), Integer.parseInt(entry.getKey()));
                if (Objects.nonNull(findByRecruiterIdAndJobId)) {
                    if (findByRecruiterIdAndJobId.getAssignedCounts() > entry.getValue()) {

                        return ApiResponse.builder().statusCode(code.getFailed()).errors(Map.of(message.getErrorKey(), "you cant decrease the assigned jobs")).build();
                    } else {
                        recruitersList.add(findByRecruiterIdAndJobId);
                    }
                } else {
                    findByRecruiterIdAndJobId = JobRecruiter.builder().jobPosting(addedJobPosting).recruiterId(Integer.parseInt(entry.getKey())).assignedCounts(entry.getValue()).status(JobRecruiterStatus.OPEN).assignedBy(joBPostingRequest.getCreatedBy()).assignedAt(Timestamp.valueOf(java.time.LocalDateTime.now())).modifiedBy(joBPostingRequest.getModifiedBy()).modifiedAt(Timestamp.valueOf(java.time.LocalDateTime.now())).build();
                    recruitersList.add(findByRecruiterIdAndJobId);
                }

            }
        }

        for (JobRecruiter rec : recruitersList) {

            int target = 0;

            for (Map<String, Integer> map : joBPostingRequest.getRecruitersIdWithAssignedCounts()) {
                if (map.containsKey(rec.getRecruiterId())) {
                    target = map.get(rec.getRecruiterId());
                    break;
                }

            }

            Integer assignedCounts = target;
            rec.setAssignedCounts(assignedCounts);
            jobRecruiterRepo.save(rec);
        }

        return ApiResponse.builder().statusCode(code.getSuccess()).value(message.getEditMessage()).build();
    }

    @Override
    public ApiResponse getAllPostedJobs(int id) {

        List<JobPosting> jobpostings = jobPostingRepo.findAllByBranchIdAndDraft(id, true);
//        List<GetJobPostingResponse> resp = new ArrayList<>();
//
//        for (JobPosting jobPosting : jobpostings) {
//            GetJobPostingResponse jobPost = new GetJobPostingResponse();
//            // Copy properties from jobPosting to jobPost
//
//            List<JobRecruiter> jobRecruiters = jobPosting.getJobRecruiters();
//            for (JobRecruiter jobRecruiter : jobRecruiters) {
//                int recruiterId = jobRecruiter.getRecruiterId();
//                ApiResponse response = tenentEmployeeService.viewProfileByEmployeeId(recruiterId);
//
//                Object value = response.getValue();
//                if (value instanceof List<?>) {
//                    List<Object[]> respTen = (List<Object[]>) value;
//                    for (int i = 0; i < respTen.size(); i++) {
//                        Object[] row = respTen.get(i);
//                        jobPost.setFirstName((String) row[1]);
//                        jobPost.setLastName((String) row[2]);
//                    }
//                }
//            }
//
//            resp.add(jobPost);
////            BeanUtils.copyProperties(jobPosting, resp);
//        }

        if (jobpostings.isEmpty()) {
            return ApiResponse.builder().statusCode(code.getNotFound()).errors(Map.of(responseMessage.getErrorKey(), responseMessage.getNoDataFound())).build();
        }

        return ApiResponse.builder().statusCode("2200").value(jobpostings).build();
    }

    @Override
    public ApiResponse getJobById(int id) {
        Optional<JobPosting> jobOptional = jobPostingRepo.findById(id);
        if (jobOptional.isEmpty()) {
            return ApiResponse.builder().statusCode(code.getNotFound()).errors(Map.of(responseMessage.getErrorKey(), responseMessage.getNoDataFound())).build();
        }

        return ApiResponse.builder().statusCode("2200").value(jobOptional.get()).build();
    }

    @Override
    public ApiResponse getProgressedCounts(int id) {

//        List<JobRecruiter> jobRecruiters = jobRecruiterRepo.findByRecruiterId(id) ;
//        int count = 0;
//        for (JobRecruiter jobRecruiter : jobRecruiters) {
//            int tempCount = jobRecruiter.getAssignedCounts();
//            List<InterviewSchedule> scheduledInterviews = interviewScheduledRepo.findByjobRecruiterId(jobRecruiter.getId());
//            scheduledInterviews.forEach(scheduledInterviews);
//        }
        return null;
    }

    @Override
    public ApiResponse getJobTemplates() {

        List<JobTemplate> jobTemplates = jobTemplateRepo.findAll();

        return ApiResponse.builder().statusCode(code.getSuccess()).value(jobTemplates).build();
    }

    @Override
    public ApiResponse addJobTemplate(JobTemplateRequest jobTemplateRequest) {

        JobTemplate jobTemp = new JobTemplate();

        BeanUtils.copyProperties(jobTemplateRequest, jobTemp);

        List<Skill> skills = new ArrayList<>();

        for (int i : jobTemplateRequest.getSkillIds()) {

            Optional<Skill> skill = skillRepo.findById(i);
            if (skill.isEmpty()) {
                return ApiResponse.builder().statusCode(code.getNotFound()).errors(Map.of(message.getErrorKey(), message.getNoDataFound())).build();
            }

            skills.add(skill.get());
        }
        jobTemp.setSkills(skills);

        jobTemplateRepo.save(jobTemp);

        return ApiResponse.builder().statusCode(code.getSuccess()).value(message.getAddMessage()).build();

    }

    @Override
    public ApiResponse getJobDraft(int id) {
        List<JobPosting> jobOptional = jobPostingRepo.findAllByCreatedByAndDraft(id, false);
        if (jobOptional.isEmpty()) {
            return ApiResponse.builder().statusCode(code.getNotFound()).errors(Map.of(responseMessage.getErrorKey(), responseMessage.getNoDataFound())).build();
        }

        return ApiResponse.builder().statusCode("2200").value(jobOptional).build();
    }

    @Override
    public ApiResponse postJobDraft(int id) {
        Optional<JobPosting> jobPosting = jobPostingRepo.findById(id);

        if (jobPosting.isEmpty()) {
            return ApiResponse.builder().statusCode(code.getNotFound()).value(Map.of(responseMessage.getErrorKey(), responseMessage.getNoDataFound())).build();
        }

        JobPosting jobPosting1 = jobPosting.get();
        jobPosting1.setDraft(true);
        jobPostingRepo.save(jobPosting1);

        return ApiResponse.builder().statusCode(code.getSuccess()).value(message.getAddMessage()).build();
    }

    @Override
    public ApiResponse deleteJobDraft(int id) {
        Optional<JobPosting> jobPosting = jobPostingRepo.findById(id);
        if (jobPosting.isEmpty()) {
            return ApiResponse.builder().statusCode(code.getNotFound()).value(Map.of(responseMessage.getErrorKey(), responseMessage.getNoDataFound())).build();
        }

        List<JobRecruiter> jobRecruiters = jobPosting.get().getJobRecruiters();

        List<Skill> skills = jobPosting.get().getSkills();

        for (JobRecruiter jr : jobRecruiters) {
            jobRecruiterRepo.delete(jr);
        }

        for (Integer JobPostingskillId : skillRepo.jobSkillList(id)) {
            skillRepo.deleteJobPostingSkillBYJobId(JobPostingskillId);
        }

        jobPostingRepo.deleteById(id);

        return ApiResponse.builder().statusCode(code.getSuccess()).value(message.getDeleteMessage()).build();
    }

    @Override
    public ApiResponse updatejobDraft(int jobpostId, JobPostingRequest jobPostingRequest) {

        Optional<JobPosting> jobPost = jobPostingRepo.findById(jobpostId);
        if (jobPost.isEmpty()) {
            return ApiResponse.builder().statusCode(code.getNotFound()).value(Map.of(message.getErrorKey(), message.getNoDataFound())).build();
        }
        JobPosting jobPosting = jobPost.get();

        jobRecruiterRepo.deleteJobRecruiterByJobId(jobpostId);

        skillRepo.jobSkillList(jobpostId).forEach(JobPostingskillId -> {
            skillRepo.deleteJobPostingSkillBYJobId(JobPostingskillId);
        });

        BeanUtils.copyProperties(jobPostingRequest, jobPosting);

        //    List<JobRecruiter> jobRecruiters = new ArrayList<>();
        List<Skill> skills = new ArrayList<>();

        for (int i : jobPostingRequest.getSkillIds()) {
            Optional<Skill> skillById = skillRepo.findById(i);
            if (skillById.isEmpty()) {
                return ApiResponse.builder().statusCode(code.getNotFound()).errors(Map.of(message.getErrorKey(), message.getSkillNotFound())).build();
            }
            skills.add(skillById.get());
        }

        jobPosting.getSkills().clear();
        jobPosting.getSkills().addAll(skills);
//        jobPosting.setSkills(skills);

        JobPosting addedJobPosting = jobPostingRepo.save(jobPosting);

        if (jobPosting.isDraft()) {
            Date now = new Date();
            long delay = (addedJobPosting.getCloseDate().getTime() - now.getTime()) / (60 * 1000);
            schedularImple.jobStatusChangeSchedular(delay, addedJobPosting.getId());
        }

        for (Map<String, Integer> entry : jobPostingRequest.getRecruitersIdWithAssignedCounts()) {
            entry.forEach((key, value) -> jobRecruiterRepo.save(JobRecruiter.builder().jobPosting(addedJobPosting).recruiterId(Integer.parseInt(key)).assignedCounts(value).status(JobRecruiterStatus.OPEN).assignedBy(jobPostingRequest.getCreatedBy()).assignedAt(Timestamp.valueOf(java.time.LocalDateTime.now())).modifiedBy(jobPostingRequest.getModifiedBy()).modifiedAt(Timestamp.valueOf(java.time.LocalDateTime.now())).build()));
        }

        return ApiResponse.builder().statusCode(code.getSuccess()).value(message.getAddMessage()).build();
    }

    @Override
    public JobHistory jobHistory(JobPosting jobPosting, int modifiedBy, String status) {

        JobHistory jobHistory = new JobHistory();
        jobHistory.setJobId(jobPosting);
        jobHistory.setStatus(jobPosting.getStatus());
        jobHistory.setModifiedBy(modifiedBy);
        jobHistory.setModifiedAt(Timestamp.valueOf(LocalDateTime.now()));
        return jobHistoryRepo.save(jobHistory);

    }

    @Override
    public ApiResponse getJobHistory(int jobId) {
        List<JobHistory> jobHistory = jobHistoryRepo.findByJobHistory(jobId);
        return ApiResponse.builder().statusCode(code.getSuccess()).value(jobHistory).build();

    }
}
