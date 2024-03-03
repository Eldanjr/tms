package com.bassure.applicantservice.response;

import com.bassure.applicantservice.model.JobPostingStatus;
import com.bassure.applicantservice.model.JobPostingType;
import com.bassure.applicantservice.model.Skill;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomJobPostingResponse {

    private int id;

    private String title;

    private String description;

    private String experience;

    private int noOfVaccancies;

    private String requirements;

    private JobPostingType type;

    private JobPostingStatus status;

    private String location;

    private Date openDate;

    private Date closeDate;

    private int modifiedBy;

    private Timestamp modifiedAt;

    private List<Skill> skills;

    private List<CustomJobRecuriterResponse> jobRecruiters;

    private Object jobRecruiterDetails;

}
