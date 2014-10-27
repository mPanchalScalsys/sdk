package com.application.model;

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
@Table(name="polls")
public class Poll {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "pollId")	
	private long pollId;
	
	@Column(name = "pollTitle")
	private String pollTitle;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_date")
	private Date start_date;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_date")
	private Date end_date;

	@Column(name = "property_id")
	private long propertyId;
	
	public long getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(long propertyId) {
		this.propertyId = propertyId;
	}

	public long getPollId() {
		return pollId;
	}

	public void setPollId(long pollId) {
		this.pollId = pollId;
	}

	public String getPollTitle() {
		return pollTitle;
	}

	public void setPollTitle(String pollTitle) {
		this.pollTitle = pollTitle;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	
}
