package com.bassure.applicantservice.model.feedbackform;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "feedback_questions")
public class FeedBackQuestion {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "feedback_id")
    private FeedBack FeedbackId;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question questionId;

    @Column(name = "offered_answer")
    @Enumerated(EnumType.STRING)
    private OfferedAnswer offeredAnswer;

    public Map<Question, OfferedAnswer> getOfferedAnswers() {
        Map<Question, OfferedAnswer> offeredAnswersMap = new HashMap<>();
        offeredAnswersMap.put(questionId, offeredAnswer);
        return offeredAnswersMap;
    }
}
