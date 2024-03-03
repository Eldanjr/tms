package com.bassure.applicantservice.model.scheduling;

import com.bassure.applicantservice.model.JobRecruiter;
import com.bassure.applicantservice.model.applicantModel.ApplicantDetails;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "interview_schedule")
public class InterviewSchedule {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "interview_round_id")
    private InterviewRound interviewRound;

    @ManyToOne
    @JoinColumn(name = "applicant_detail_id")
    private ApplicantDetails applicantId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_recruiter_id")
    private JobRecruiter jobRecruiter;

    @Column(name = "mode")
    @Enumerated(EnumType.STRING)
    private InterviewMode modeOfInterview;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private InterviewStatus interviewStatus;

    @Column(name = "feedback")
    private String feedback;

    @Column(name = "scheduled_by")
    private int scheduledBy;

    @Column(name = "started_at")
    @NotNull(message = "start date time must not be empty")
    private Timestamp startedAt;

    @Column(name = "ended_at")
    @NotNull(message = "end date time must not be empty")
    private Timestamp endedAt;

    @Column(name = "location")
    private String location;

    @OneToMany(mappedBy = "interviewId", cascade = CascadeType.ALL)
    private List<Interviewers> interviewers;

    @AssertTrue(message = "select a valid date and time")
    private boolean isEndedAtGreater() {
        return startedAt.getTime() < endedAt.getTime();
    }

}
