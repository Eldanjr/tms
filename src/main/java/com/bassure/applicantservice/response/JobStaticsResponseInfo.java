package com.bassure.applicantservice.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobStaticsResponseInfo {

    private int rescheduled;
    private int cancelled;
    private int scheduled;
    private int completed;
    private int inprogress;
    private String rescheduledPercent;
    private String cancelledPercent;
    private String scheduledPercent;
    private String completedPercent;
    private String inprogressPercent;

}
