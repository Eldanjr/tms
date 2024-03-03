package com.bassure.applicantservice.request;

import com.bassure.applicantservice.model.JobPostingStatus;
import com.bassure.applicantservice.model.JobPostingType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPostingRequest {

    @NotNull(message = "title is mandatory")
    private String title;

    @NotNull(message = "description is mandatory")
    private String description;

    @NotNull(message = "experience is mandatory")
    private String experience;

    @NotNull(message = "no of vaccancies is mandatory")
    private int noOfVaccancies;

    @NotNull(message = "requirement is mandatory")
    private String requirement;

    @NotNull(message = "recruiter id's is mandatory")
    private List<Map<String, Integer>> recruitersIdWithAssignedCounts;

    @NotNull(message = "skill is mandatory")
    private int[] skillIds;

    @NotNull(message = "job type is mandatory")
    private JobPostingType type;

    @NotNull(message = "draft is mandatory")
    private boolean draft;

    @NotNull(message = "status is mandatory")
    @Enumerated(EnumType.STRING)
    private JobPostingStatus status;

    @NotNull(message = "location is mandatory")
    private String location;

    @NotNull(message = "open date is mandatory")
    private Date openDate;

    @NotNull(message = "close date is mandatory")
    private Date closeDate;

    @NotNull(message = "created by is mandatory")
    private int createdBy;

    @NotNull(message = "modified by is mandatory")
    private int modifiedBy;

    @NotNull(message = "barnchId is mandatory")
    private int branchId;
}
