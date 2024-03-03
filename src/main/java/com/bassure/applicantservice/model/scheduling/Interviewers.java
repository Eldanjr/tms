package com.bassure.applicantservice.model.scheduling;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "interviewers")
public class Interviewers {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interviewers")
    private int id;

    @Column(name = "interviewer_id")
    private int interviewerId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "interview_id")
    private InterviewSchedule interviewId;
//
//    @OneToMany(mappedBy = "panelId")
//    private List<FeedBack> feedbacks;

}
