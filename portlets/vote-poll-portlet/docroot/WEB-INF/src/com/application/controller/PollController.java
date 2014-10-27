package com.application.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.application.config.ApplicationContextHolder;
import com.application.model.Choices;
import com.application.model.Poll;
import com.application.model.Question;
import com.application.model.UserVote;
import com.application.model.dao.ChoicesDao;
import com.application.model.dao.PollDao;
import com.application.model.dao.QuestionDao;
import com.application.model.dao.UserVoteDao;
import com.application.util.RenderHelper;
import com.liferay.portal.kernel.dao.search.ResultRow;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.dao.search.SearchEntry;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class PollController extends MVCPortlet{
	public void doView(RenderRequest renderRequest,RenderResponse renderResponse) throws IOException, PortletException{
		String operation = ParamUtil.getString(renderRequest, "operation");
		/** Case for Add New Poll Entry **/   
		if("addPoll".equals(operation)){
			Poll poll=new Poll();
			poll.setPollTitle(StringPool.BLANK);
			poll.setEnd_date(null);
			poll.setStart_date(null);
			renderRequest.setAttribute("poll", poll);
			renderRequest.setAttribute("isNew", true);
			include("/jsps/poll/add_poll.jsp", renderRequest, renderResponse);
		}else if ("addQuestion".equals(operation)) {
			/** Case for Add New Question In Particular Poll **/
			include("/jsps/poll/add_poll_question.jsp", renderRequest, renderResponse);
		}else if ("giveVoteOnPoll".equals(operation)) {
			/** Case for Select Poll by user for Vote **/
			long pollId = ParamUtil.getLong(renderRequest, "pollIdForGiveVote");
			//voteStatus
			boolean voteStatus = ParamUtil.getBoolean(renderRequest, "voteStatus");
			log.info(" Vote Status ------: " + voteStatus );
			try {
				long scopeGroupId = PortalUtil.getScopeGroupId(renderRequest);
				QuestionDao questionDao = ApplicationContextHolder.getContext().getBean(QuestionDao.class);
				/** Retrieve the Questions by GroupId and PollId **/
				List<Question> questionByPollId = questionDao.getQuestionByPropertyIdAndPollId(scopeGroupId, pollId);
				renderRequest.setAttribute("pollId", pollId);
				renderRequest.setAttribute("questionCount", questionByPollId.size());
				renderRequest.setAttribute("questionsByPollId", questionByPollId);
				renderRequest.setAttribute("isVoted", voteStatus);
			}catch (PortalException e) {
				log.error("error in generating question list list :: ", e);
				renderRequest.setAttribute("error", "Something went wrong. Please try after some time.");
			} catch (SystemException e) {
				log.error("error in generating question list list :: ", e);
				renderRequest.setAttribute("error", "Something went wrong. Please try after some time.");
			}
			include("/jsps/poll/give_vote_on_poll.jsp", renderRequest, renderResponse);
		} else if("displayVote".equals(operation)){
			/** Case for Show Graph after End date of Poll **/
			long pollId = ParamUtil.getLong(renderRequest, "graphPollId");
			
			QuestionDao questionDao = ApplicationContextHolder.getContext().getBean(QuestionDao.class);
			
			long scopeGroupId;
			try {
				scopeGroupId = PortalUtil.getScopeGroupId(renderRequest);
				//List<Question> questionList = questionDao.getQuestionByPropertyIdAndPollId(scopeGroupId, pollId);
				List questionWithVote = questionDao.getQuestionsByPollId(pollId);
				
				renderRequest.setAttribute("questionWithVote", questionWithVote);
				//renderRequest.setAttribute("questionList", questionList);
			} catch (PortalException e) {
				log.error("error in generating vote list :: ", e);
			} catch (SystemException e) {
				log.error("error in generating vote list :: ", e);
			}
			
			renderRequest.setAttribute("pollId", pollId);
			include("/jsps/poll/view_graph.jsp", renderRequest, renderResponse);
		}
		else if ("editPoll".equals(operation)) {
			/** Case for Edit Poll **/
			long pollId = ParamUtil.getLong(renderRequest, "pollId");
			if(pollId == 0) { 
				String pollID = ParamUtil.getString(renderRequest, "newQuestionPollId");
				// long pollID =  (Long)renderRequest.getAttribute("newQuestionPollId");
				log.info("-- " + pollID );
				pollId = Long.parseLong(pollID); 
				}
				/** Get The pollId of Edit Poll **/
				PollDao pollDao = ApplicationContextHolder.getContext().getBean(PollDao.class);
				/** Retrieve Poll object by pollId **/
		
			Poll poll = pollDao.getPollByPollId(pollId);
			
			try {
				long scopeGroupId = PortalUtil.getScopeGroupId(renderRequest);
				PortletURL portletURL = renderResponse.createRenderURL();
				QuestionDao questionDao = ApplicationContextHolder.getContext().getBean(QuestionDao.class);
				boolean dispMessage = false;
				/** Set Table Headers **/
				List<String> header = new ArrayList<String>();
				header.add("question-title");
				header.add("question-type");
				header.add(StringPool.BLANK);
				/** Search Container Listing Code **/
				SearchContainer<Question> searchContainer = new SearchContainer<Question>(renderRequest,portletURL,header,"no-question-available-for-this-poll");
				/** List<Question> resultList = questionDao.getQuestionByProperty(scopeGroupId, searchContainer.getStart(), searchContainer.getEnd()); **/
				List<Question> countList = questionDao.getQuestionByPropertyIdAndPollId(scopeGroupId, pollId);
				List<Question> resultList = questionDao.getQuestionByPropertyIdAndPollId(scopeGroupId, pollId,searchContainer.getStart(),searchContainer.getEnd());
				searchContainer.setDelta(20);
				searchContainer.setTotal(countList.size());
				searchContainer.setResults(resultList);
				List<ResultRow> resultRows = searchContainer.getResultRows();
				int rowCoutner = 0;
				/** Looping through list to set rows in Search Container **/
				for (Question question : resultList) {
					ResultRow resultRow = new ResultRow(question, question.getQuestionId(), rowCoutner);
					resultRow.addText("center", "middle", question.getTitle());
					resultRow.addText("center", "middle", question.getType());
					HttpServletRequest request = PortalUtil.getHttpServletRequest(renderRequest);
					resultRow.addJSP("center",SearchEntry.DEFAULT_ALIGN, "/jsps/poll/poll_question_actions.jsp", request.getSession().getServletContext(),request, PortalUtil.getHttpServletResponse(renderResponse));
					resultRows.add(resultRow);
					rowCoutner++;
				}
				/** Retrive the Size of SearchContainer **/
				int limit = countList.size();
				
				/**** vote given on this poll or not ***/
				UserVoteDao userVoteDao = ApplicationContextHolder.getContext().getBean(UserVoteDao.class);
				Long userListVoted = userVoteDao.findVotedUserOrNot(scopeGroupId, pollId);
			    if(userListVoted != null && userListVoted != 0){
			    	dispMessage = true;
			    }
				/****end of the give vote ******/
				renderRequest.setAttribute("dispMessage", dispMessage);
				renderRequest.setAttribute("containerMax", limit);
				renderRequest.setAttribute("question-list", searchContainer);
			} catch (PortalException e) {
				log.error("error in generating vote list :: ", e);
				renderRequest.setAttribute("error", "Something went wrong. Please try after some time.");
			} catch (SystemException e) {
				log.error("error in generating vote list :: ", e);
				renderRequest.setAttribute("error", "Something went wrong. Please try after some time.");
			}
		
			renderRequest.setAttribute("poll", poll);
			renderRequest.setAttribute("isNew", false);
			
			include("/jsps/poll/add_poll.jsp", renderRequest, renderResponse);
		} else{
			try {
				 long scopeGroupId = PortalUtil.getScopeGroupId(renderRequest);
				PortletURL portletURL = renderResponse.createRenderURL();
				PollDao pollDao = ApplicationContextHolder.getContext().getBean(PollDao.class);
				/** Set Table Headers **/
				List<String> header = new ArrayList<String>();
				header.add("poll-title");
				header.add(StringPool.BLANK);
				/** Search Container Listing Code **/
				SearchContainer<Poll> searchContainer = new SearchContainer<Poll>(renderRequest,portletURL,header,"no-poll-available");
				List<Poll> countList = pollDao.getPollByPropertyId(scopeGroupId);
				List<Poll> resultList = pollDao.getPollByPropertyId(scopeGroupId, searchContainer.getStart(), searchContainer.getEnd());
				searchContainer.setDelta(20);
				searchContainer.setTotal(countList.size());
				searchContainer.setResults(resultList);
				List<ResultRow> resultRows = searchContainer.getResultRows();
				int rowCoutner = 0;
				/** Looping through list to set rows in Search Container **/
				for (Poll poll : resultList) {
					ResultRow resultRow = new ResultRow(poll, poll.getPollId(), rowCoutner);
					resultRow.addText("center", "middle", poll.getPollTitle());
					HttpServletRequest request = PortalUtil.getHttpServletRequest(renderRequest);
					resultRow.addJSP("center",SearchEntry.DEFAULT_ALIGN, "/jsps/poll/add_authorised_permission.jsp", request.getSession().getServletContext(),request, PortalUtil.getHttpServletResponse(renderResponse));
					resultRows.add(resultRow);
					rowCoutner++;
				}
				renderRequest.setAttribute("poll-list", searchContainer);
			} catch (PortalException e) {
				log.error("error in generating poll list :: ", e);
				renderRequest.setAttribute("error", "Something went wrong. Please try after some time.");
			} catch (SystemException e) {
				log.error("error in generating poll list :: ", e);
				renderRequest.setAttribute("error", "Something went wrong. Please try after some time.");
			}
			include("/jsps/poll/view.jsp", renderRequest, renderResponse);
		}
			
	}
	/***
	 * Method for ADD or UPDATE Poll
	 * @param actionRequest
	 * @param actionResponse
	 * @throws PortalException
	 * @throws SystemException
	 */
	public void editPollURL(ActionRequest actionRequest,ActionResponse actionResponse) throws PortalException, SystemException{

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		/** Set the Date Format **/
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		User user = themeDisplay.getUser();
		/** Retrive The Active User **/
		long pollId = ParamUtil.getLong(actionRequest, "pollId");
		/** Retrive The Value of Parameters **/
		String pollTitle = ParamUtil.getString(actionRequest, "txtPollTitle",StringPool.BLANK);
		String sDate = ParamUtil.getString(actionRequest, "startdate");
		sDate+=" 00:00:01";
		String eDate = ParamUtil.getString(actionRequest, "enddate");
		eDate+=" 23:23:59";
		Date pollStartDate=null,pollEndDate=null;
		try {
			pollStartDate = dateFormat.parse(sDate);
			pollEndDate = dateFormat.parse(eDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PollDao pollDao = ApplicationContextHolder.getContext().getBean(PollDao.class);
		Poll poll=null;
		/** Check the Poll Is New or Updated **/
		if(pollId>0){
			poll = pollDao.getPollByPollId(pollId);
		}else{
			poll = new Poll();
		}
		poll.setPollTitle(pollTitle);
		poll.setStart_date(pollStartDate);
		poll.setEnd_date(pollEndDate);
		poll.setPropertyId(PortalUtil.getScopeGroupId(actionRequest));
		pollDao.addPoll(poll);
		log.info("poll added successfully");
	}
	/***
	 * Method for ADD or UPDATE Poll Question
	 * @param actionRequest
	 * @param actionResponse
	 * @throws PortalException
	 * @throws SystemException
	 */
	public void editPollQuestionURL(ActionRequest actionRequest,ActionResponse actionResponse) throws PortalException, SystemException{
		log.info("question Add successfull");
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		User user = themeDisplay.getUser();
		/** Retrive The Active User **/
		long pollId = ParamUtil.getLong(actionRequest, "pollId"); 
		long questionId = ParamUtil.getLong(actionRequest, "questionId"); 
		int choiceCount = ParamUtil.getInteger(actionRequest, "choiceContentList");
		QuestionDao questionDao = ApplicationContextHolder.getContext().getBean(QuestionDao.class);
		String pQuestionTitle = ParamUtil.getString(actionRequest, "txtQuestionTitle",StringPool.BLANK);
		String questionType = ParamUtil.getString(actionRequest, "question_status",StringPool.BLANK);
		
		Question question;
		/** Check Question Id is for Update or Add  **/
		if(questionId > 0){
			question = questionDao.findbyQuestionId(questionId);
		} else {
			question = new Question();
		}
		question.setTitle(pQuestionTitle);
		question.setCreated_by(user.getFullName());
		if(questionId>0){question.setModified_date(new Date());}
		else{question.setCreated_date(new Date());}
		question.setPollId(pollId);
		question.setPropertyId(PortalUtil.getScopeGroupId(actionRequest));
		if(questionType.equals("selectQuestion")){
			question.setType(questionType);
			Question q = questionDao.addQuestion(question);
			/**  Retrive Choices based on Question For ADD or UPDATE **/
			for (int i = 1; i <= choiceCount; i++) {
				ChoicesDao choicesDao = ApplicationContextHolder.getContext().getBean(ChoicesDao.class);
				Choices c;
				/** Retrie the Choice Id from hidden Field  **/
				Long id = ParamUtil.getLong(actionRequest, "choiceRegisterId"+i);
				String choiceValue = ParamUtil.getString(actionRequest, "includedChoice"+i);
				/**  check Choice is new or old **/
				if(id > 0){
					c = choicesDao.findChoiceByChoiceId(id);
				}else{
					c = new Choices();
					c.setQuestionId(q.getQuestionId());	
				}
				c.setDescription(choiceValue);
				if(q != null) {
					choicesDao.addChoices(c);
				}
			}
		}else if (questionType.equals("openAnswerQuestion")) {
			question.setType(questionType);
			questionDao.addQuestion(question);
			/** Delete all the choices when Change the Select Question To Open Answer Question **/
			ChoicesDao choiceDao = ApplicationContextHolder.getContext().getBean(ChoicesDao.class);
			/** Retrieve Choices of Question Id For Delete  **/
			List<Choices> choices = choiceDao.findChoicesByQuestionId(questionId);
			for(Choices choice : choices){
				choiceDao.deleteChoices(choice);
				/** Delete choice One by One  **/
			}
			
		}
		
		log.info("pollID :" + pollId + "QUESTION TITLE : " + pQuestionTitle + "QTYPE : " + questionType);
		log.info("-done-");
		
		/** set renderParameter poll and searchContainer after Entering New Question **/
		PollDao pollDao = ApplicationContextHolder.getContext().getBean(PollDao.class);
		Poll poll = pollDao.getPollByPollId(pollId);
		/** end of render parameter **/
		
		/** Set Parameter for render page on as it is(Current Page)  **/
		/** set renderParameter for set the QuestionId **/
		actionResponse.setRenderParameter("newQuestionPollId", String.valueOf(pollId));
		/** Set renderparameter for render on which jsp page **/
		actionResponse.setRenderParameter("operation", "editPoll");
	}
	/***
	 * Method for delete Single Question Into the POLL
	 * @param actionRequest
	 * @param actionResponse
	 */
	public void deleteQuestionURL(ActionRequest actionRequest,ActionResponse actionResponse){
		/** Get The Question Id For Delete Question **/
		int questionId = ParamUtil.getInteger(actionRequest, "deleteQuestionId");
		QuestionDao questionDao = ApplicationContextHolder.getContext().getBean(QuestionDao.class);
		/** Retrive Question Based On Question Id **/
		Question question = questionDao.findbyQuestionId(questionId);
		/** Delete Question  **/
		questionDao.deleteQuestion(question);
		ChoicesDao choiceDao = ApplicationContextHolder.getContext().getBean(ChoicesDao.class);
		/** Retrieve Choices of Question Id For Delete  **/
		List<Choices> choices = choiceDao.findChoicesByQuestionId(questionId);
		for(Choices choice : choices){
			choiceDao.deleteChoices(choice);
			/** Delete choice One by One  **/
		}
		
		/** Set Parameter for render page on as it is(Current Page)  **/
		 /** set renderParameter for set the QuestionId **/
		actionResponse.setRenderParameter("newQuestionPollId", String.valueOf(question.getPollId()));
		/** Set renderparameter for render on which jsp page **/
		actionResponse.setRenderParameter("operation", "editPoll");
		
		
		log.info("Question Id delete success fully : " + questionId );
	}
	/***
	 * Method for ADD or UPDATE Question From POLL (DIALOG)
	 * @param resourceRequest
	 * @param resourceResponse
	 * @throws IOException
	 * @throws PortletException
	 */
	public void serveResource(ResourceRequest resourceRequest,ResourceResponse resourceResponse) throws IOException,PortletException {
		String operation = ParamUtil.getString(resourceRequest, "ajax",StringPool.BLANK);
		if("addQuestion".equals(operation)){
		HttpServletRequest httpRequest = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
		HttpServletResponse httpResponse = PortalUtil.getHttpServletResponse(resourceResponse);
		ServletContext servletContext = httpRequest.getSession().getServletContext();
		
		String pollId = ParamUtil.getString(httpRequest, "pollId", StringPool.BLANK);
		log.info("call from POLLID: " + pollId );
		
		PollDao pollDao = ApplicationContextHolder.getContext().getBean(PollDao.class);
		Poll poll = pollDao.getPollByPollId(Long.parseLong(pollId));
		
		Question question = new Question();
		question.setStart_date(null);
		question.setEnd_date(null);
		question.setTitle(StringPool.BLANK);
		question.setDescription(StringPool.BLANK);
		question.setType("openAnswerQuestion");
		
		httpRequest.setAttribute("choices", new ArrayList<Choices>());
		httpRequest.setAttribute("question", question);
		httpRequest.setAttribute("poll", poll);
		httpRequest.setAttribute("new", "true");
		
		StringBuilder result= new StringBuilder();
		try {
			result.append(RenderHelper.renderPage(servletContext, httpRequest, httpResponse, "/jsps/poll/add_poll_question.jsp"));  
		     } 
		catch (ServletException e) {
		       e.printStackTrace();
		    }
		PrintWriter out= resourceResponse.getWriter();
		out.println(result.toString());
		} else if ("editQuestion".equals(operation)) {
			
			// long pollId = ParamUtil.getLong(resourceRequest, "pollId");
			// log.info(" poll ID : " + pollId );
			HttpServletRequest httpRequest = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
			HttpServletResponse httpResponse = PortalUtil.getHttpServletResponse(resourceResponse);
			ServletContext servletContext = httpRequest.getSession().getServletContext();
			
			String questionId = ParamUtil.getString(httpRequest, "questionId", StringPool.BLANK);
			log.info(" QUESTION ID : " + questionId );
			
			QuestionDao questionDao = ApplicationContextHolder.getContext().getBean(QuestionDao.class);
			ChoicesDao choiceDao = ApplicationContextHolder.getContext().getBean(ChoicesDao.class);
			PollDao pollDao = ApplicationContextHolder.getContext().getBean(PollDao.class);
			
			Question question = questionDao.findbyQuestionId(Long.parseLong(questionId));
			
			List<Choices> choices = null;
			
			if(question.getType().equals("selectQuestion")){
				choices= choiceDao.findChoicesByQuestionId(Long.parseLong(questionId));
			}
			log.info(" poll ID : " + question.getPollId() );
			Poll poll = pollDao.getPollByPollId(question.getPollId());
			
			httpRequest.setAttribute("poll", poll);
			httpRequest.setAttribute("choices", choices);
			httpRequest.setAttribute("question", question);
			httpRequest.setAttribute("new", "false");
			
			StringBuilder result= new StringBuilder();
			try {
				result.append(RenderHelper.renderPage(servletContext, httpRequest, httpResponse, "/jsps/poll/add_poll_question.jsp"));  
			     } 
			catch (ServletException e) {
			       e.printStackTrace();
			    }
			PrintWriter out= resourceResponse.getWriter();
			out.println(result.toString());
			
		}else if ("deleteChoice".equals(operation)) {
			/** Retrive the choiceId of Question **/
			long choiceId = ParamUtil.getLong(resourceRequest, "cRegisterId");
			log.info("delete choice Id "+ choiceId);
			ChoicesDao choiceDao = ApplicationContextHolder.getContext().getBean(ChoicesDao.class);
			/** Retrive Choice object by ChoiceId **/
			Choices choice = choiceDao.findChoiceByChoiceId(choiceId);
			/** Delete Choice **/
			choiceDao.deleteChoices(choice);
			JSONObject resultObj = JSONFactoryUtil.createJSONObject();
			resultObj.put("status", "success");
			PrintWriter out= resourceResponse.getWriter();
			out.println(resultObj.toString());
			log.info("choice deted successfully ");
		}
		
	}
	/***
	 * Method for ADD or UPDATE Given Vote On POLL Question by User
	 * @param actionRequest
	 * @param actionResponse
	 * @throws PortalException
	 * @throws SystemException
	 */
	public void pollQuestionVoteURL(ActionRequest actionRequest,ActionResponse actionResponse){
		
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		User user = themeDisplay.getUser();
		/** Retrieve the Status for Poll(Add or Update) **/
		boolean voteStatus = ParamUtil.getBoolean(actionRequest, "pollStatus");
		
		long userId = user.getUserId();
		long pollId = ParamUtil.getLong(actionRequest, "pollId");
		
		QuestionDao questionDao = ApplicationContextHolder.getContext().getBean(QuestionDao.class);
		
		long scopeGroupId;
		try {
			scopeGroupId = PortalUtil.getScopeGroupId(actionRequest);
			List<Question> questionList = questionDao.getQuestionByPropertyIdAndPollId(scopeGroupId, pollId);
		
			for(Question question : questionList){

			UserVoteDao userVoteDao = ApplicationContextHolder.getContext().getBean(UserVoteDao.class);
			UserVote userVote = null;
			if(voteStatus){
			 userVote = userVoteDao.voteOrNoteByQuestionId(user.getUserId(),question.getQuestionId());
			}else{
			 userVote = new UserVote();
			}
			long questionId = ParamUtil.getLong(actionRequest, "questionId"+question.getQuestionId());
			
			String selectQuestion = ParamUtil.getString(actionRequest, "selectQuestion"+question.getQuestionId(),StringPool.BLANK);
			
			userVote.setQuestionId(questionId);
			userVote.setVoted_date(new Date());
			userVote.setUserId(userId);
			
			if(selectQuestion.equals("selectQuestion")){
				 long choiceId = ParamUtil.getLong(actionRequest, "choiceId"+question.getQuestionId());  
				//answer = ParamUtil.getString(actionRequest, "radioAnswers",StringPool.BLANK);
				 userVote.setChoiceId(choiceId);
			}else{
				String	answer = ParamUtil.getString(actionRequest, "textAnswers"+question.getQuestionId(),StringPool.BLANK);
				userVote.setUser_open_vote_desc(answer);
			}
			/** Check Update Or New UserVote **/
			
			userVoteDao.addUserVote(userVote);
			}
		} catch (PortalException e) {
			log.error("error in generating vote list :: ", e);
		} catch (SystemException e) {
			log.error("error in generating vote list :: ", e);
		}
		
	}
	/***
	 * Method for DELETE Questions And POLL
	 * @param actionRequest
	 * @param actionResponse
	 * @throws PortalException
	 * @throws SystemException
	 */
	public void deleteVoteQuestionURL(ActionRequest actionRequest,ActionResponse actionResponse){
		long deletePollId = ParamUtil.getLong(actionRequest, "deletePollId");
		QuestionDao questionDao = ApplicationContextHolder.getContext().getBean(QuestionDao.class);
		PollDao pollDao = ApplicationContextHolder.getContext().getBean(PollDao.class);
		ChoicesDao choiceDao = ApplicationContextHolder.getContext().getBean(ChoicesDao.class);
		Poll poll = pollDao.getPollByPollId(deletePollId);
		long scopeGroupId;
		try {
			scopeGroupId = PortalUtil.getScopeGroupId(actionRequest);
			List<Question> questionsByPollId = questionDao.getQuestionByPropertyIdAndPollId(scopeGroupId, deletePollId);
			
			for(Question question : questionsByPollId){
				/** Retrive choices of Particular Question **/
				List<Choices> choices = choiceDao.findChoicesByQuestionId(question.getQuestionId());
					for(Choices choice : choices){
						choiceDao.deleteChoices(choice);
						/** Delete choice One by One  **/
					}
				/** Remove Questions From Poll one by one **/
				questionDao.deleteQuestion(question);
			}
			pollDao.deletePoll(poll);
		} catch (PortalException e) {
			log.error("error in generating question list for delete :: ", e);
		} catch (SystemException e) {
			log.error("error in generating question list for delete :: ", e);
		}
		/** NOT REMOVE ANY ENTRY FROM USERVOTE **/
	}
	
	private static Log log = LogFactoryUtil.getLog(PollController.class);
}
