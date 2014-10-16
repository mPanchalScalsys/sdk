package com.application.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "user_votes")
public class UserVote {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "voteId")
	private long voteId;
	
	@Column(name = "questionId")
	private long questionId;
	
	@Column(name = "choiceId")
	private long choiceId;
	
	@Column(name = "user_open_vote_desc")
	private String user_open_vote_desc;
	
	@Column(name = "voted_date")
	private Date voted_date;
	
	@Column(name = "userId")
	private long userId;

	public long getVoteId() {
		return voteId;
	}

	public void setVoteId(long voteId) {
		this.voteId = voteId;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public long getChoiceId() {
		return choiceId;
	}

	public void setChoiceId(long choiceId) {
		this.choiceId = choiceId;
	}

	public String getUser_open_vote_desc() {
		return user_open_vote_desc;
	}

	public void setUser_open_vote_desc(String user_open_vote_desc) {
		this.user_open_vote_desc = user_open_vote_desc;
	}

	public Date getVoted_date() {
		return voted_date;
	}

	public void setVoted_date(Date voted_date) {
		this.voted_date = voted_date;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
