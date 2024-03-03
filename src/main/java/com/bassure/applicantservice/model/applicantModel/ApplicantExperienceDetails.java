package com.bassure.applicantservice.model.applicantModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "applicant_experience_details")
public class ApplicantExperienceDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int applicantExpDetailsId;

    @Column(name = "occupation_name")
    private String occupationName;

    @Column(name = "company")
    private String companyName;

    @Column(name = "description")
    private String description;

    @Column(name = "from_date")
    private String fromDate;

    @Column(name = "to_date")
    private String toDate;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "applicant_id")
    private ApplicantDetails applicantId;

}
