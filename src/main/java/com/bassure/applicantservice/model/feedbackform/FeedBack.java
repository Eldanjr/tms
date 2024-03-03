package com.bassure.applicantservice.model.feedbackform;

import com.bassure.applicantservice.model.scheduling.Interviewers;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "feedback")
public class FeedBack {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "interview_id")
    private Interviewers interviewerId;

//    @ManyToOne
//    @JoinColumn(name = "interview_id")
//    private InterviewSchedule interviewId;
    @Column(name = "description")
    private String desc;

    @Column(name = "status")
//    @Enumerated(EnumType.STRING)
    private String status;

}
