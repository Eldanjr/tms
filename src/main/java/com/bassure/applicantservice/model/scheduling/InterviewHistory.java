 package com.bassure.applicantservice.model.scheduling;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "interview_history")
public class InterviewHistory {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "interview_id")
    private InterviewSchedule interviewId;

    @Column(name = "modified_by")
    private int modifiedBy;

    @Column(name = "modified_at")
    @NotNull(message = "modified_at date time must not be empty")
    private Timestamp modifiedAt;

    @Column(name = "remarks")
    private String remarks;

}
