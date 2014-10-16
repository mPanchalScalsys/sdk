package com.application.model.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.application.model.Poll;

@Service
public interface PollDao {
	/**
	 * 
	 * @param propertyId
	 * @return
	 */
	public List<Poll> getPollByPropertyId(long propertyId);
	/**
	 * @param propertyId
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Poll> getPollByPropertyId(long propertyId, int start, int end);
	/**
	 * @param pollId
	 * @return
	 */
	public Poll getPollByPollId(long pollId);
	/**
	 * @param poll
	 * @return
	 */
	public Poll addPoll(Poll poll);
	/**
	 * @param poll
	 */
	public void deletePoll(Poll poll);
}
