package com.application.model.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.application.model.UserVote;

@Service
public interface UserVoteDao {
public void addUserVote(UserVote userVote);
public void deleteUserVote(UserVote userVote);
public List<UserVote> findChoiceIdByQuestionId(long questionId);
public UserVote checkUserVotedOrNot(long userId);
public UserVote voteOrNoteByQuestionId(long userId,long questionId);
public List countByChoice(long questionId);
public List<UserVote> findUserIdChoiceId(long choiceId);
public List countByCoEfficiency(long quetionId);
/*public List findUserCoefficiencyChoiceId(long choiceId);*/
public List findUserCoefficiencyChoiceId(long choiceId,long propertyId);
public Long findVotedUserOrNot(long propertyId, long pollId);
}
