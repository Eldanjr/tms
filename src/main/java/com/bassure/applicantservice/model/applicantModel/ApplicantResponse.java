package com.bassure.applicantservice.model.applicantModel;

import com.bassure.applicantservice.model.JobPosting;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "applicant_response")
public class ApplicantResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int applicantResponseId;

    @Column(name = "interested")
    private Boolean interested;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_posting_id")
    private JobPosting jobPostingId;

//       @OnDelete(action = OnDeleteAction.CASCADE)
//    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "applicant_id")
    private Applicant applicantId;

    @Enumerated(EnumType.STRING)
    @Column(name = "applicant_response_status")
    private ApplicantResponseStatus applicantResponseStatus;

}
