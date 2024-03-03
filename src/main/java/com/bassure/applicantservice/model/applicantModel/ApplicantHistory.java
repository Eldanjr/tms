package com.bassure.applicantservice.model.applicantModel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
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
@Table(name = "applicant_history")
public class ApplicantHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int applicantHistoryId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "applicant_details_id")
    private ApplicantDetails applicantDetailsId;

    @Column(name = "description")
    private String description;
    
    @Column(name="date")
    private Timestamp date;
    
    @Column(name="created_by")
    private int createdBy;

}
