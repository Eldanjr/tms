package com.bassure.applicantservice.repo;

import com.bassure.applicantservice.model.feedbackform.FeedBackQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedBackQuestionsRepository extends JpaRepository<FeedBackQuestion, Integer>{
    
}