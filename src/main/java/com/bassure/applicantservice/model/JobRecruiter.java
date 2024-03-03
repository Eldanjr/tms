package com.bassure.applicantservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table
@Entity(name = "job_recruiter")
public class JobRecruiter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "job_posting_id", nullable = false)
    private JobPosting jobPosting;

    @Column(name = "recruiter_id")
    private int recruiterId;

    @Column(name = "assigned_counts")
    private int assignedCounts;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private JobRecruiterStatus status;

    @Column(name = "assigned_by")
    private int assignedBy;

    @Column(name = "modified_by")
    private int modifiedBy;

    @Column(name = "assigned_at")
    private Timestamp assignedAt;

    @Column(name = "modified_at")
    private Timestamp modifiedAt;

}
