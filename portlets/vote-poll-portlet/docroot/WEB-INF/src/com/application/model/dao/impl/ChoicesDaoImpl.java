package com.application.model.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.model.Choices;
import com.application.model.Question;
import com.application.model.dao.ChoicesDao;

@Repository
@Transactional
public class ChoicesDaoImpl implements ChoicesDao{

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Choices> getAllChoices() {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from Choices").list();
	}

	@Override
	public void addChoices(Choices choices) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(choices);
	}

	@Override
	public List<Choices> findChoicesByQuestionId(long questionId) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from Choices where questionId="+questionId).list();
	}

	@Override
	public void deleteChoices(Choices choices) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		session.delete(choices);
	}

	@Override
	public Choices findChoiceByChoiceId(long choiceId) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		return (Choices) session.get(Choices.class, choiceId);
	}

}
