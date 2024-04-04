package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.model.Question;
import com.example.repository.QuestionRepository;

@Service
public class QuestionServiceImpl implements IQuestionService {

	
	private final QuestionRepository questionRepository;
	
	
//	public QuestionRepository getQuestionRepository() {
//		return questionRepository;
//	}
//
//	@Autowired
//	public void setQuestionRepository(QuestionRepository questionRepository) {
//		this.questionRepository = questionRepository;
//	}
	@Autowired
	public QuestionServiceImpl(QuestionRepository questionRepository) {
		super();
		this.questionRepository = questionRepository;
	}


	public Question createQuestion(Question question) {
		
		return questionRepository.save(question);
	}


	public List<Question> getAllQuestions() {
	return questionRepository.findAll();
	}


	public Optional<Question> getQuestionById(Long id) {
		return questionRepository.findById(id);
	}


	public List<String> getAllSubjects() {
		return questionRepository.findDistinctSubject();
	}


	public Question updateQuestion(Long id, Question question) throws NotFoundException {
		Optional<Question> theQuestion = this.getQuestionById(id);
		if(theQuestion.isPresent())
		{
			Question updatedQuestion = theQuestion.get();
			updatedQuestion.setQuestion(question.getQuestion());
			updatedQuestion.setChoices(question.getChoices());
			updatedQuestion.setCorrectAnswers(question.getCorrectAnswers());
			return questionRepository.save(updatedQuestion);	
		} else {
			throw new ChangeSetPersister.NotFoundException();
		}
		
	} 
 	
	public void deleteQuestion(Long id) {
		questionRepository.deleteById(id);
	}

	public List<Question> getQuestionForUser(Integer numOfQuestions, String subject) {
		Pageable pageable= PageRequest.of(0, numOfQuestions);
		return questionRepository.findBySubject(subject, pageable).getContent();
	}
	
	
	

}
