package com.bassure.applicantservice.filter;

import com.bassure.applicantservice.model.JobPosting;
import com.bassure.applicantservice.model.JobPostingStatus;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.bassure.applicantservice.model.scheduling.InterviewSchedule;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class JobPostingFilterSpecifications {

    public static Specification<JobPosting> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("title"), title);
    }
    
    public static Specification<JobPosting> hasDesc() {
        return (root, query, criteriaBuilder) -> {
            criteriaBuilder.desc(root.get("id"));
            return criteriaBuilder.conjunction();
        };
    }
    

    public static Specification<JobPosting> hasLocation(String location) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("location"), location);
    }

    public static Specification<JobPosting> hasDraft(boolean draft) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("draft"), draft);
    }

    public static Specification<JobPosting> hasBranchId(int branchId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("branchId"), branchId);
    }
    
        public static Specification<JobPosting> hasCreatedBy(int createdBy) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("createdBy"), createdBy);
    }

    public static Specification<JobPosting> hasExperience(String experience) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("experience"), experience);
    }

    public static Specification<JobPosting> hasStatusIn(JobPostingStatus status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("status"), status);
    }

    public static Specification<JobPosting> hasOpenDateBetween(Date startDate, Date endDate) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.between(root.get("openDate"), startDate, endDate);
    }

    public static Specification<JobPosting> hasOpenDateAfter(Date startDate) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.greaterThanOrEqualTo(root.get("openDate"), startDate);
    }

    public static Specification<JobPosting> hasOpenDateBefore(Date endDate) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.lessThanOrEqualTo(root.get("openDate"), endDate);
    }

    public static Specification<JobPosting> hasStatus(JobPostingStatus status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<InterviewSchedule> hasIds(List<Object> ids) {
        return (root, query, cb)
                -> {
            CriteriaBuilder.In<Object> inClass = cb.in(root.get("id"));
            for (Object value : ids) {
                inClass.value(value);
            }
            Predicate condition1 = inClass;
            return condition1;
        };
    }

//    public static Specification<InterviewSchedule> hasStatus(String status) {
//        return (root, query, cb)
//                -> {
//            Predicate condition2 = cb.equal(root.get("interviewStatus"), InterviewStatus.valueOf(status));
//            return condition2;
//        };
//    }

    public static Specification<InterviewSchedule> hasInterviewOpenDate(Date openDate) {
        return (root, query, cb)
                -> {
            Timestamp ts = new Timestamp(openDate.getTime());
            System.out.println(ts);
            Predicate condition3 = cb.greaterThanOrEqualTo(root.get("startedAt"), ts);
            return condition3;
        };
    }

    public static Specification<InterviewSchedule> hasInterviewCloseDate(Date closeDate) {
        return (root, query, cb)
                -> {
            Timestamp ts = new Timestamp(closeDate.getTime());
            Predicate condition4 = cb.lessThanOrEqualTo(root.get("endedAt"), ts);
            return condition4;
        };
    }
    

}