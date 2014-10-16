package com.application.model.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.application.model.Vote;

@Service
public interface VoteDao {
public List<Vote> getAllVote();
}
