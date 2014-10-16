package com.application.model.dao;

import java.util.List;

import org.springframework.stereotype.Service;
import com.application.model.Choices;

@Service
public interface ChoicesDao {
public List<Choices> getAllChoices();
public void addChoices(Choices choices);
public void deleteChoices(Choices choices);
public Choices findChoiceByChoiceId(long choiceId);
public List<Choices> findChoicesByQuestionId(long questionId);
}
