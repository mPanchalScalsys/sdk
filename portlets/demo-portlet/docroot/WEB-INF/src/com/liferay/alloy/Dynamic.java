package com.liferay.alloy;

import java.io.IOException;
import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portlet.polls.model.PollsChoice;
import com.liferay.portlet.polls.model.PollsQuestion;
import com.liferay.portlet.polls.service.PollsChoiceLocalServiceUtil;
import com.liferay.portlet.polls.service.PollsQuestionLocalServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class Dynamic
 */
public class Dynamic extends MVCPortlet {
	public void doView(RenderRequest renderRequest,RenderResponse renderResponse) throws IOException, PortletException{
		String renderPage = ParamUtil.getString(renderRequest, "renderPage",StringPool.BLANK);
		if("addChoice".equals(renderPage)){
			String questionId = ParamUtil.getString(renderRequest, "txtQuestionId",StringPool.BLANK);
			renderRequest.setAttribute("questionId", questionId);
			include("/html/dynamic/add_choice.jsp", renderRequest, renderResponse);
		}else{
			include("/html/dynamic/view.jsp", renderRequest, renderResponse);
		}
	}
	public void getAutoFieldsData(ActionRequest actionRequest,ActionResponse actionResponse) throws Exception {
		System.out.println("methoid");
		log.info("method started");
		
		PollsQuestion question;
		PollsChoice choices;
		
		question = PollsQuestionLocalServiceUtil.createPollsQuestion(CounterLocalServiceUtil.increment(PollsQuestion.class.getName()));
		
		
		String questionTitle = ParamUtil.getString(actionRequest, "txtQuestionTitle",StringPool.BLANK);
		String questionDescription = ParamUtil.getString(actionRequest, "txtQuestionDescription",StringPool.BLANK);
		
		String phonesIndexesString = actionRequest.getParameter("phonesIndexes");
		log.info("phonesIndexesString : "+phonesIndexesString );
		int[] phonesIndexes = StringUtil.split(phonesIndexesString, 0);
		
		question.setTitle(questionTitle);
		question.setDescription(questionDescription);
		question.setCreateDate(new Date());
		
		question = PollsQuestionLocalServiceUtil.updatePollsQuestion(question);
		
		  for (int phonesIndex : phonesIndexes) {
			  choices = PollsChoiceLocalServiceUtil.createPollsChoice(CounterLocalServiceUtil.increment(PollsChoice.class.getName()));
			  String choice = ParamUtil.getString(actionRequest, "txtChoice" + phonesIndex);
			  choices.setName(choice);
			  choices.setCreateDate(new Date());
			  choices.setQuestionId(question.getQuestionId());
		      log.info("choice : "+ choice );
		      choices = PollsChoiceLocalServiceUtil.updatePollsChoice(choices);
		  }
		  
		  
		  
	}
	private static Log log = LogFactoryUtil.getLog(Dynamic.class);
}
