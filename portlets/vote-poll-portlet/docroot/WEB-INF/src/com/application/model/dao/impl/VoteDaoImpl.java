package com.application.model.dao.impl;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.model.Vote;
import com.application.model.dao.VoteDao;

@Repository
@Transactional
public class VoteDaoImpl implements VoteDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<Vote> getAllVote() {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from Vote").list();
	}

}
