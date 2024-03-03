package com.bassure.applicantservice.model.applicantModel;

import com.bassure.applicantservice.model.JobRecruiter;
import com.bassure.applicantservice.model.Skill;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "applicant_details")
public class ApplicantDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int applicantDetailsId;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "added_by")
    private int addedBy;

    @Column(name = "added_at")
    @CreationTimestamp
    private Timestamp addedAt;

    @Column(name = "current_ctc")
    private String currentCtc;

    @Column(name = "expected_ctc")
    private String expectedCtc;

    @Column(name = "reason_for_change")
    private String reasonForChange;

    @Column(name = "holding_offer")
    private Boolean holdingOffer;

    @Column(name = "current_company")
    private String currentCompany;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "current_address_id")
    private ApplicantAddress currentAddress;

    @Column(name = "resume_path")
    private String resumePath;

    @Column(name = "source", nullable = true)
    private String source;

    @Column(name = "reference_by")
    private String referenceBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = true)
    private Gender gender;

    @Column(name = "year_of_experience")
    private String yearOfExperience;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_recruiter_id")
    private JobRecruiter jobRecruiterId;

    @ManyToMany
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinTable(name = "applicant_skill", joinColumns = @JoinColumn(name = "applicant_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skill;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "applicant_response_id")
    private ApplicantResponse applicantResponseId;

    @OneToMany(mappedBy = "applicantId")
    private List<ApplicantEducationalDetails> applicantEducationDetails;

    @OneToMany(mappedBy = "applicantId")
    private List<ApplicantExperienceDetails> applicantExperienceDetails;

}
