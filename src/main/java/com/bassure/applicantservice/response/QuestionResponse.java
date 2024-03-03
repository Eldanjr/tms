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
public class QuestionResponse {
    private int id;
    private String question;
    private OfferedAnswer offeredAnswer;

    @Override
    public String toString() {
        return "FeedBackQuestionResponse{" + "id=" + id + ", question=" + question + ", offeredAnswer=" + offeredAnswer + '}';
    }
    
}
