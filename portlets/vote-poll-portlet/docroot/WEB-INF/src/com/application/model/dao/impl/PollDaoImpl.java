package com.application.model.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.model.Poll;
import com.application.model.Question;
import com.application.model.dao.PollDao;

@Repository
@Transactional
public class PollDaoImpl implements PollDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<Poll> getPollByPropertyId(long propertyId) {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from Poll WHERE propertyId="+ propertyId).list();
		/*return session.createSQLQuery("select *,CURDATE()>end_date AS flag from polls WHERE propertyId="+ propertyId + " ORDER BY flag ASC").list();*/
		
	}
	@Override
	public List<Poll> getPollByPropertyId(long propertyId, int start, int end) {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from Poll WHERE propertyId="+ propertyId).setFirstResult(start).setMaxResults(end).list();
		/*return session.createSQLQuery("select *,CURDATE()>end_date AS flag from polls WHERE propertyId="+ propertyId + " ORDER BY flag ASC").setFirstResult(start).setMaxResults(end).list();*/
	}
	@Override
	public Poll getPollByPollId(long pollId) {
		Session session = sessionFactory.getCurrentSession();
		return (Poll)session.createQuery("from Poll WHERE pollId="+pollId).uniqueResult();
	}
	@Override
	public Poll addPoll(Poll poll) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		return (Poll) session.merge(poll);
	}
	@Override
	public void deletePoll(Poll poll) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		session.delete(poll);
	}
	
}
