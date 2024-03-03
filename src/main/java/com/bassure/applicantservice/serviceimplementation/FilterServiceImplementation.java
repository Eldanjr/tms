package com.bassure.applicantservice.serviceimplementation;

import com.bassure.applicantservice.filter.ApplicationFilterSpecifications;
import com.bassure.applicantservice.filter.JobPostingFilterSpecifications;
import com.bassure.applicantservice.model.JobPosting;
import com.bassure.applicantservice.model.JobPostingStatus;
import com.bassure.applicantservice.model.applicantModel.Applicant;
import com.bassure.applicantservice.model.applicantModel.ApplicantStatus;
import com.bassure.applicantservice.model.scheduling.InterviewSchedule;
import com.bassure.applicantservice.repo.ApplicantRepository;
import com.bassure.applicantservice.repo.InterviewScheduleRepository;
import com.bassure.applicantservice.repo.JobPostingRepository;
import com.bassure.applicantservice.response.ApiResponse;
import com.bassure.applicantservice.response.GetJobPostingResponse;
import com.bassure.applicantservice.response.ResponseCode;
import com.bassure.applicantservice.response.ResponseMessage;
import com.bassure.applicantservice.service.FilterService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.BeanUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class FilterServiceImplementation implements FilterService {

    @Autowired
    JobPostingRepository jobPostingFilterRepository;

    @Autowired
    ApplicantRepository applicantRepository;
    @Autowired
    ResponseCode code;
    @Autowired
    ResponseMessage message;

    @Autowired
    InterviewScheduleRepository interviewRepo;

    @Autowired
    JobPostingRepository jobReposit;

    @Override
    public ApiResponse filterJobPostings(int id, String title, String location, Date startDate, Date endDate,
            JobPostingStatus status,
            String experience) {
        Specification<JobPosting> spec = Specification.where(null);
        spec = spec.and(JobPostingFilterSpecifications.hasCreatedBy(id));
        spec = spec.and(JobPostingFilterSpecifications.hasDraft(true));
        spec = spec.and(JobPostingFilterSpecifications.hasDesc());

        if (Objects.isNull(status)) {
            spec = spec.and(JobPostingFilterSpecifications.hasStatusIn(JobPostingStatus.CLOSE));
        }

        if (title != null) {
            spec = spec.and(JobPostingFilterSpecifications.hasTitle(title));
        }

        if (location != null) {
            spec = spec.and(JobPostingFilterSpecifications.hasLocation(location));
        }

        if (startDate != null && endDate != null) {
            spec = spec.and(JobPostingFilterSpecifications.hasOpenDateAfter(startDate));
            spec = spec.and(JobPostingFilterSpecifications.hasOpenDateBefore(endDate));
        }
        if (status != null) {
            spec = spec.and(JobPostingFilterSpecifications.hasStatus(status));
        }

        if (experience != null) {
            spec = spec.and(JobPostingFilterSpecifications.hasExperience(experience));
        }

        List<JobPosting> jobposting = jobPostingFilterRepository.findAll(spec);
        Collections.sort(jobposting, Comparator.comparing(JobPosting::getId).reversed());
        List<GetJobPostingResponse> jobrespo = new ArrayList<>();
        if (jobposting.isEmpty()) {
            return ApiResponse.builder()
                    .statusCode(code.getNotFound())
                    .errors(Map.of(message.getErrorKey(), message.getNoDataFound()))
                    .build();
        }
        for (JobPosting j : jobposting) {
            GetJobPostingResponse jp = new GetJobPostingResponse();
            BeanUtils.copyProperties(j, jp);
            jp.setPositionFilled(jobReposit.getFilledJobCount(j.getId()));
            jobrespo.add(jp);
        }
        return ApiResponse.builder()
                .statusCode("2200")
                .value(jobrespo)
                .build();

    }

    @Override
    public ApiResponse filterApplicant(String firstName, String middleName, String lastName, String email, String contactNo, ApplicantStatus applicantStatus) {
        Specification<Applicant> spec = Specification.where(null);

        if (firstName != null) {
            spec = spec.and(ApplicationFilterSpecifications.hasFirstName(firstName));
        }

        if (middleName != null) {
            spec = spec.and(ApplicationFilterSpecifications.hasMiddleName(middleName));
        }
        if (lastName != null) {
            spec = spec.and(ApplicationFilterSpecifications.hasLastName(lastName));
        }
        if (email != null) {
            spec = spec.and(ApplicationFilterSpecifications.hasEmail(email));
        }

        if (contactNo != null) {
            spec = spec.and(ApplicationFilterSpecifications.hasContactNo(contactNo));
        }

        if (applicantStatus != null) {
            spec = spec.and(ApplicationFilterSpecifications.hasApplicantStatus(applicantStatus));
        }

        return new ApiResponse(code.getSuccess(), applicantRepository.findAll(spec), null);
    }

    @Override
    public List<InterviewSchedule> withfilter(List<Object> ids, Date openDate, Date closeDate) {
        Specification<InterviewSchedule> spec = Specification.where(null);

        spec = spec.and(JobPostingFilterSpecifications.hasIds(ids));

//        if (status != null) {
//            spec = spec.and(JobPostingFilterSpecifications.hasStatus(status));
//        }
        if (openDate != null) {
            spec = spec.and(JobPostingFilterSpecifications.hasInterviewOpenDate(openDate));
        }

        if (closeDate != null) {
            spec = spec.and(JobPostingFilterSpecifications.hasInterviewCloseDate(closeDate));
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");

        return interviewRepo.findAll(spec);

    }

}