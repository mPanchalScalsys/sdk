package com.application.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "choices")
public class Choices {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "choiceId")
	private long choiceId;
	
	@Column(name = "questionId")
	private long questionId;

	@Column(name = "description")
	private String description;

	public long getChoiceId() {
		return choiceId;
	}

	public void setChoiceId(long choiceId) {
		this.choiceId = choiceId;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
