package com.bassure.applicantservice.model;
 
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table
@Entity(name = "job_posting")
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "requirement")
    private String requirement;

    @Column(name = "experience")
    private String experience;

    @Column(name = "no_of_vaccancies")
    private int noOfVaccancies;

    @Column(name = "draft")
    private boolean draft;

    @OneToMany(mappedBy = "jobPosting")
    private List<JobRecruiter> jobRecruiters;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "job_posting_skill", joinColumns = {
        @JoinColumn(name = "job_posting_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "skill_id")})
    private List<Skill> skills;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private JobPostingType type;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private JobPostingStatus status;

    @Column(name = "location")
    private String location;

    @Column(name = "open_date")
    @Temporal(TemporalType.DATE)
    private Date openDate;

    @Column(name = "close_date")
    @Temporal(TemporalType.DATE)
    private Date closeDate;

    @Column(name = "created_by")
    private int createdBy;

    @Column(name = "modified_by")
    private int modifiedBy;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "modified_at")
    private Timestamp modifiedAt;

    @Column(name = "branch_id")
    private int branchId;

    @OneToMany(mappedBy = "jobId")
    private List<JobHistory> jobStatusHistory;
}
