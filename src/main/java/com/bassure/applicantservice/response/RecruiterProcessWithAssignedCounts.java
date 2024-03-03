package com.bassure.applicantservice.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RecruiterProcessWithAssignedCounts {

    private int offered;
    
    private String jobTitle;
    
    private String status;
    
    private int newApplicants;
    
    private int recruiterId;

    private int assignedCounts;

    private int positionFilled;

    private int rejected;

    private int inProcess;

}
