package com.bassure.applicantservice.filter;

import com.bassure.applicantservice.model.applicantModel.Applicant;
import com.bassure.applicantservice.model.applicantModel.ApplicantStatus;
import org.springframework.data.jpa.domain.Specification;

public class ApplicationFilterSpecifications {

    public static Specification<Applicant> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("firstName"), firstName);
    }

    public static Specification<Applicant> hasMiddleName(String middleName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("middleName"), middleName);
    }

    public static Specification<Applicant> hasLastName(String lastName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("lastName"), lastName);
    }

    public static Specification<Applicant> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email);
    }

    public static Specification<Applicant> hasContactNo(String contactNo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("contactNo"), contactNo);
    }

    public static Specification<Applicant> hasApplicantStatus(ApplicantStatus applicantStatus) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("applicantStatus"), applicantStatus);
    }

}
