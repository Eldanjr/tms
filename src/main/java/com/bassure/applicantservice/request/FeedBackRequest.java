package com.bassure.applicantservice.request;

import com.bassure.applicantservice.model.feedbackform.OfferedAnswer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedBackRequest {

    private String desc;
    private Map<String, OfferedAnswer> question;


//    private Map<Integer, OfferedAnswer> offeredAnswers;
}
