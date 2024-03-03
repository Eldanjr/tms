package com.bassure.applicantservice.response;

import com.bassure.applicantservice.model.feedbackform.OfferedAnswer;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackTableResponse {

    private Map<String,OfferedAnswer> rating;
}
