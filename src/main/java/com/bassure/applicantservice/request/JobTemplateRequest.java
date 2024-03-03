package com.bassure.applicantservice.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobTemplateRequest {

    @NotNull(message = "title is mandatory")
    private String title;

    @NotNull(message = "description is mandatory")
    private String description;


    @NotNull(message = "skill is mandatory")
    private int[] skillIds;

    @NotNull(message = "branch Id is mandatory")
    private int branchId;

    @NotNull(message = "requirement is mandatory")
    private String requirement;

    @NotNull(message = "created by is mandatory ")
    private int createdBy;

    @NotNull(message = "modified by is mandatory")
    private int modifiedBy;
}
