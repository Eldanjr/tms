package com.bassure.applicantservice.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecruiterProgress {
    private int postingId;
    private String jobTitle;
    private int hiredCount;
}
