package com.bassure.applicantservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity(name = "job_template")
@ToString
public class JobTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "job_template_skill", joinColumns = {
        @JoinColumn(name = "job_template_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "skill_id")})
    private List<Skill> skills;
    @Column(name = "branch_id")

    private int branchId;
    @Column(name = "requirement")
    private String requirement;

    @Column(name = "created_by")
    private int createdBy;

    @Column(name = "modified_by")
    private int modifiedBy;

}
