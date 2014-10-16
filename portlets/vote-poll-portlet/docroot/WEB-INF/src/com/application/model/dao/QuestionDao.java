package com.application.model.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.application.model.Question;

@Service
public interface QuestionDao {
	/**
	 * To Get all the questions from Table - Regardless of property Id
	 * @return
	 */
	public List<Question> getAllQuestion();
	/**
	 * 
	 * @param propertyId
	 * @return
	 */
	public List<Question> getQuestionByProperty(long propertyId);
	/**
	 * 
	 * @param propertyId
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Question> getQuestionByProperty(long propertyId, int start, int end);
	/**
	 * @param propertyId
	 * @return
	 */
	public List<Question> getQuestionByPropertyId(long propertyId);
	/**
	 * 
	 * @param question
	 * @return
	 */
	public Question addQuestion(Question question);
	/**
	 * 
	 * @param questionId
	 * @return
	 */
	public Question findbyQuestionId(long questionId);
	/**
	 * 
	 * @param question
	 */
	public void deleteQuestion(Question question);
	/**
	 * @param propertyId
	 * @param pollId
	 * @return
	 */
	public List<Question> getQuestionByPropertyIdAndPollId(long propertyId,long pollId);
	/**
	 * @param propertyId
	 * @param pollId
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Question> getQuestionByPropertyIdAndPollId(long propertyId,long pollId, int start, int end);
	/**
	 * @param pollId
	 * 
	 */
	public List getQuestionsByPollId(long pollId,long groupId);
}

