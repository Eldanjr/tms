package com.bassure.applicantservice.response;

import com.bassure.applicantservice.model.feedbackform.OfferedAnswer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class FeedBackViewResponse {

    private int id;
    private String questions;
    private OfferedAnswer offeredAnswer;
    private String desc;

}
