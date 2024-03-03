package com.bassure.applicantservice.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class RecruiterProcess {

    private int offered;

    private int newApplicants;

    private String jobTitle;

    private String status;

    private int recruiterId;

    private int assignedCounts;

    private int positionFilled;

    private int rejected;

    private int inProcess;

}
