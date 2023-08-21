package com.example.feedback.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.feedback.models.Feedback;


public interface FeedbackRepository extends MongoRepository<Feedback, String>{
	
	@Query(value = "{'userid': ?0}")
	List<Feedback> findFeedbackByUserId(@Param("id") String id);
}
