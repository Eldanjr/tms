package com.bassure.applicantservice.response;

import com.bassure.applicantservice.model.JobPostingStatus;
import com.bassure.applicantservice.model.JobPostingType;
import com.bassure.applicantservice.model.JobRecruiter;
import com.bassure.applicantservice.model.Skill;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetJobPostingResponse {

    private int id;

    private String title;

    private String description;

    private String requirement;

    private String experience;

    private int noOfVaccancies;

    private boolean draft;

    private List<JobRecruiter> jobRecruiters;

    private List<Skill> skills;

    private JobPostingType type;

    private JobPostingStatus status;

    private String location;

    private Date openDate;

    private Date closeDate;

    private int createdBy;

    private int modifiedBy;

    private Timestamp createdAt;

    private Timestamp modifiedAt;

    private int branchId;

    private int positionFilled;

}
