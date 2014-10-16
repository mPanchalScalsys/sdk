package com.application.model;

import java.io.Serializable;
import java.util.Date;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
@Table(name = "user_votes")
public class Vote implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "voteId")
	private Long id;

	@Column(name = "questionId")
	private Long questionId;

	@Column(name = "choiceId")
	private Long choiceId;

	@Column(name = "user_open_vote_desc")
	private String user_vot_desc;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "voted_date")
	private Date voted_date;

	@Column(name = "userId")
	private String userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Long getChoiceId() {
		return choiceId;
	}

	public void setChoiceId(Long choiceId) {
		this.choiceId = choiceId;
	}

	public String getUser_vot_desc() {
		return user_vot_desc;
	}

	public void setUser_vot_desc(String user_vot_desc) {
		this.user_vot_desc = user_vot_desc;
	}

	public Date getVoted_date() {
		return voted_date;
	}

	public void setVoted_date(Date voted_date) {
		this.voted_date = voted_date;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
