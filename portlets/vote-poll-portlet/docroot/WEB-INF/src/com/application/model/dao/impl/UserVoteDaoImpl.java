package com.application.model.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.model.UserVote;
import com.application.model.dao.UserVoteDao;

@Repository
@Transactional
public class UserVoteDaoImpl implements UserVoteDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void addUserVote(UserVote userVote) {
		Session session = sessionFactory.getCurrentSession();
		session.merge(userVote);
	}
	
	@Override
	public void deleteUserVote(UserVote userVote) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(userVote);
	}

	@Override
	public List<UserVote> findChoiceIdByQuestionId(long questionId) {
		Session session = sessionFactory.getCurrentSession();
		return (List<UserVote>) session.createQuery("from UserVote where questionId="+questionId).list();
	}

	@Override
	public UserVote checkUserVotedOrNot(long userId) {
		Session session = sessionFactory.getCurrentSession();
		 return	(UserVote) session.createQuery("from UserVote where userId="+userId);
	}

	@Override
	public UserVote voteOrNoteByQuestionId(long userId,long questionId) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		return	(UserVote) session.createQuery("from UserVote where userId="+userId+" and questionId="+questionId).uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List countByChoice(long questionId){
		Session session = sessionFactory.getCurrentSession();
		return session.createSQLQuery("select count(*),c.description,u.choiceId from user_votes u inner join choices c on u.choiceId = c.choiceId " +
				 "where u.questionId="+questionId+" group by u.choiceId").list();
	}

	@Override
	public List<UserVote> findUserIdChoiceId(long choiceId) {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from UserVote where choiceId="+choiceId).list();
	}

	@Override
	public List countByCoEfficiency(long quetionId) {
		Session session = sessionFactory.getCurrentSession();
		return session.createSQLQuery("SELECT users.choiceId, cop.coefficient, users.userId,  choice.description FROM user_votes users "+
		"INNER JOIN choices choice ON choice.choiceId = users.choiceId "+
		"INNER JOIN copro_to_users cusers ON users.userId = cusers.user_Id "+
		"INNER JOIN coproperties cop ON cop.coproperties_id = cusers.coproperties_id "+
		"WHERE users.questionId ="+quetionId+
		" GROUP BY users.choiceId").list();
		
		
	}

	@Override
	public List findUserCoefficiencyChoiceId(long choiceId,long propertyId) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		return session.createSQLQuery("SELECT users.userId, cusers.user_name, cop.coefficient " +
				"FROM user_votes users " +
				"INNER JOIN copro_to_users cusers ON users.userId = cusers.user_Id " +
				"INNER JOIN coproperties cop ON cop.coproperties_id = cusers.coproperties_id " +
				"WHERE cop.property_id = " + propertyId  + " AND users.choiceId = "+choiceId).list();
	}
	@Override
	public Long findVotedUserOrNot(long propertyId, long pollId) {
		Session session = sessionFactory.getCurrentSession();
		Object ob = session.createSQLQuery("SELECT COUNT(*) AS voted FROM user_votes uv"+
	" INNER JOIN questions q ON q.questionId = uv.questionId"+ 
	" WHERE  q.pollId =" + pollId + " AND type='selectQuestion' AND q.property_id = " + propertyId).uniqueResult();
		BigInteger i = (BigInteger) ob;
		return i.longValue();
	}
}
