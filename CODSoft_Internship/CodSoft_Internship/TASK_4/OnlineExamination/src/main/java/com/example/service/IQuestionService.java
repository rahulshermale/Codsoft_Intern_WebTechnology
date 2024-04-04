package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.example.model.Question;

import jakarta.validation.Valid;

public interface IQuestionService {

	//Question createQuestion(Question question);
	Question createQuestion(@Valid Question question);
	
	List<Question> getAllQuestions();
	
	Optional<Question> getQuestionById(Long id);
	
	List<String> getAllSubjects();
	
	Question updateQuestion(Long id, Question question) throws NotFoundException;
	
	void deleteQuestion(Long id);
	
	List<Question> getQuestionForUser(Integer numOfQuestions, String subject);

	
	
}
