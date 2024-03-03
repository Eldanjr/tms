package com.bassure.applicantservice.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantAssociate {

    private int applicantId;
    private int jobId;
    private int applicantDetailsId;
    
}
