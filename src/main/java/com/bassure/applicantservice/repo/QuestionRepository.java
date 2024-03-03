package com.bassure.applicantservice.repo;

import com.bassure.applicantservice.model.feedbackform.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    public Question findByQuestions(String name);
}
