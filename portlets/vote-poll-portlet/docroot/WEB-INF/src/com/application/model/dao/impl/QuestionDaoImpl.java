package com.application.model.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.model.Question;
import com.application.model.dao.QuestionDao;

@Repository
@Transactional
public class QuestionDaoImpl implements QuestionDao{

	@Autowired
	private SessionFactory sessionFactory;
	/**
	 * 
	 */
	@Override
	public List<Question> getAllQuestion() {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from Question ORDER BY created_date DESC").list();
	}
	/**
	 * 
	 */
	@Override
	public List<Question> getQuestionByProperty(long propertyId) {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from Question WHERE propertyId="+ propertyId + " and pollId=0 ORDER BY created_date DESC").list();
	}
	/**
	 * 
	 */
	@Override
	public List<Question> getQuestionByProperty(long propertyId, int start, int end) {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from Question WHERE propertyId="+ propertyId + " ORDER BY created_date DESC").setFirstResult(start).setMaxResults(end).list();
	}
	@Override
	public List<Question> getQuestionByPropertyId(long propertyId) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from Question WHERE propertyId="+ propertyId + " ORDER BY created_date DESC").list();
	}
	@Override
	public Question addQuestion(Question question) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		return (Question) session.merge(question);
	}

	@Override
	public void deleteQuestion(Question question) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		session.delete(question);
	}

	@Override
	public Question findbyQuestionId(long questionId) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		return (Question) session.get(Question.class, questionId);
	}
	@Override
	public List<Question> getQuestionByPropertyIdAndPollId(long propertyId,long pollId) {
		Session session = sessionFactory.getCurrentSession();
		if(pollId == 0){
			return session.createSQLQuery("select *,CURDATE()>end_date AS flag from questions WHERE property_Id="+ propertyId +" and pollId IS NULL ORDER BY flag,created_date DESC").addEntity(Question.class).list();
		}else{
			return session.createSQLQuery("select *,CURDATE()>end_date AS flag from questions WHERE property_Id="+ propertyId +" and pollId=" + pollId + " ORDER BY flag,created_date DESC").addEntity(Question.class).list();
		}
	}
	@Override
	public List<Question> getQuestionByPropertyIdAndPollId(long propertyId,long pollId, int start, int end) {
		Session session = sessionFactory.getCurrentSession();
		if(pollId == 0){
			return session.createSQLQuery("select *,CURDATE()>end_date AS flag from questions WHERE property_Id="+ propertyId +" and pollId IS NULL ORDER BY flag,created_date DESC").addEntity(Question.class).setFirstResult(start).setMaxResults(end).list();
		}else{
			return session.createSQLQuery("select *,CURDATE()>end_date AS flag from questions WHERE property_Id="+ propertyId +" and pollId=" + pollId + " ORDER BY flag,created_date DESC").addEntity(Question.class).setFirstResult(start).setMaxResults(end).list();
		}
	}
	@Override
	public List getQuestionsByPollId(long pollId) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		return session.createSQLQuery("SELECT q.questionId,q.title,COUNT(uv.voteId) AS VoteCount FROM questions q INNER JOIN user_votes uv ON q.questionId = uv.questionId WHERE pollid =" + pollId + " AND uv.choiceId IS NOT NULL GROUP BY uv.questionId").list();
	}
	
	
	
	
}
