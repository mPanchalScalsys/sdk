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
import javax.portlet.PortletRequest;
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
import com.application.model.Question;
import com.application.model.UserVote;
import com.application.model.dao.ChoicesDao;
import com.application.model.dao.QuestionDao;
import com.application.model.dao.UserVoteDao;
import com.application.model.dao.impl.UserVoteDaoImpl;
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
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.permission.PortletPermissionUtil;
import com.liferay.portal.theme.PortletDisplay;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class VoteController extends MVCPortlet{
	
	public void doView(RenderRequest renderRequest,RenderResponse renderResponse) throws IOException, PortletException{
		String operation = ParamUtil.getString(renderRequest, "operation");
		if("addVote".equals(operation)){
			//Case of Add Vote Button Clicked
			log.info("In AddVote Condition");
			Question question = new Question();
			question.setStart_date(null);
			question.setEnd_date(null);
			question.setTitle(StringPool.BLANK);
			question.setDescription(StringPool.BLANK);
			question.setType(StringPool.BLANK);
			renderRequest.setAttribute("question", question);
			renderRequest.setAttribute("choices", new ArrayList<Choices>());
			renderRequest.setAttribute("new", "true");
			renderRequest.setAttribute("voteGaved", false);
			include("/jsps/vote/add_vote_question.jsp", renderRequest, renderResponse);
		}else if ("displayVote".equals(operation)){
			/** Get Question Id for Display Graph **/
			long questionId = ParamUtil.getInteger(renderRequest, "graphQuestionId");
			log.info("SHOW GRAPH QUESTION ID : " + questionId);
			QuestionDao questionDao = ApplicationContextHolder.getContext().getBean(QuestionDao.class);
			Question question = questionDao.findbyQuestionId(questionId);
			ChoicesDao choiceDao = ApplicationContextHolder.getContext().getBean(ChoicesDao.class);
			List<Choices> choices = choiceDao.findChoicesByQuestionId(questionId);
			UserVoteDao userVoteDao = ApplicationContextHolder.getContext().getBean(UserVoteDao.class);
			//List<UserVote> userVote = userda
			List uservotes = userVoteDao.countByChoice(questionId);
			renderRequest.setAttribute("question",question);
			renderRequest.setAttribute("choices",choices );
			renderRequest.setAttribute("userVote",uservotes);
			List votes = userVoteDao.countByCoEfficiency(questionId);
			renderRequest.setAttribute("userCoEfficiencyVote",votes);
			include("/jsps/vote/view_graph.jsp", renderRequest, renderResponse);
		}
		else if ("giveVote".equals(operation)){
			//Case of Give Vote by User
			/** Get Question Id for Give Vote **/
			long questionId = ParamUtil.getInteger(renderRequest, "voteQuestionId");
			log.info("give Vote " + questionId);
			QuestionDao questionDao = ApplicationContextHolder.getContext().getBean(QuestionDao.class);
			/** Retrieve the Question Object based on Question Id **/ 
			Question question = questionDao.findbyQuestionId(questionId);
			ChoicesDao choiceDao = ApplicationContextHolder.getContext().getBean(ChoicesDao.class);
			/** Retrive the List of Choices based on Question Id **/ 
			List<Choices> choices = choiceDao.findChoicesByQuestionId(questionId);
			UserVote userVote = new UserVote();
			userVote.setUser_open_vote_desc(StringPool.BLANK);
			userVote.setVoted_date(new Date());
			renderRequest.setAttribute("question", question);
			renderRequest.setAttribute("choices", choices);
			renderRequest.setAttribute("userVote", userVote);
			
			/** Display Graph Pass Object For Display Graph  **/
			/*
			UserVoteDao userVoteDao = ApplicationContextHolder.getContext().getBean(UserVoteDao.class);
			List uservotes = userVoteDao.countByChoice(questionId);
			renderRequest.setAttribute("userVoteList",uservotes);
			List votes = userVoteDao.countByCoEfficiency(questionId);
			renderRequest.setAttribute("userCoEfficiencyVote",votes);
			*/
			/** End of Display Graph **/
			
			include("/jsps/vote/give_vote_question.jsp", renderRequest, renderResponse);
		}
		else if ("editChoice".equals(operation)){   
			// Case of Edit Given Vote by User 
			/** Retrive Current Login User **/ 
			User user = (User) renderRequest.getAttribute(WebKeys.USER);
			/** Get Question Id for Already Given Vote **/
			long questionId = ParamUtil.getInteger(renderRequest, "questionId");
			ChoicesDao choiceDao = ApplicationContextHolder.getContext().getBean(ChoicesDao.class);
			UserVoteDao userVoteDao = ApplicationContextHolder.getContext().getBean(UserVoteDao.class);
			/** Retrive the UserVote Object based on UserId and Question Id **/ 
			UserVote userVote = userVoteDao.voteOrNoteByQuestionId(user.getUserId(), questionId);
			/** Retrive the List of Choices based on Question Id **/ 
			List<Choices> choices = choiceDao.findChoicesByQuestionId(questionId);
			QuestionDao questionDao = ApplicationContextHolder.getContext().getBean(QuestionDao.class);
			/** Retrive the Question Object based on Question Id **/ 
			Question question = questionDao.findbyQuestionId(questionId);
			renderRequest.setAttribute("question", question);
			renderRequest.setAttribute("choices", choices);
			renderRequest.setAttribute("userVote", userVote);
			
			/** Display Graph Pass Object For Display Graph  **/
			/*
			List uservotes = userVoteDao.countByChoice(questionId);
			renderRequest.setAttribute("userVoteList",uservotes);
			List votes = userVoteDao.countByCoEfficiency(questionId);
			renderRequest.setAttribute("userCoEfficiencyVote",votes);
			*/
			/** End of Display Graph **/
			
			include("/jsps/vote/give_vote_question.jsp", renderRequest, renderResponse);
		}
		else if ("editVote".equals(operation)) {
			//Case of Edit Vote Link Clicked 
			/** Get Edit Question Id **/
			long editQuestionId = ParamUtil.getLong(renderRequest, "editQuestionId");
			QuestionDao questionDao = ApplicationContextHolder.getContext().getBean(QuestionDao.class);
			UserVoteDao userVoteDao = ApplicationContextHolder.getContext().getBean(UserVoteDao.class);
			/** Retrive the Question Object based on Question Id **/ 
			Question question = questionDao.findbyQuestionId(editQuestionId);
			/** Retrive the List of Choices based on Question Id **/ 
			ChoicesDao choicesDao = ApplicationContextHolder.getContext().getBean(ChoicesDao.class);
			List<Choices> choices = choicesDao.findChoicesByQuestionId(editQuestionId);
			boolean voteGaved = false;
			// UserVote giveVoteOrNot = userVoteDao.voteOrNoteByQuestionId(user.getUserId(), editQuestionId);
			List<UserVote> giveVoteOrNot = userVoteDao.findChoiceIdByQuestionId(editQuestionId);
			if(!giveVoteOrNot.isEmpty()){
				voteGaved=true; 
			};
			renderRequest.setAttribute("question", question);
			renderRequest.setAttribute("choices", choices);
			renderRequest.setAttribute("new", "false");
			renderRequest.setAttribute("voteGaved", voteGaved);
			include("/jsps/vote/add_vote_question.jsp", renderRequest, renderResponse);
		} else {
			try {
				long scopeGroupId = PortalUtil.getScopeGroupId(renderRequest);
				PortletURL portletURL = renderResponse.createRenderURL();
				QuestionDao questionDao = ApplicationContextHolder.getContext().getBean(QuestionDao.class);
				/** Set Table Headers **/
				List<String> header = new ArrayList<String>();
				header.add("question-title");
				header.add(StringPool.BLANK);
				/** Search Container Listing Code **/
				SearchContainer<Question> searchContainer = new SearchContainer<Question>(renderRequest,portletURL,header,"no-question-available");
				/** List<Question> resultList = questionDao.getQuestionByProperty(scopeGroupId, searchContainer.getStart(), searchContainer.getEnd()); **/
				/** List<Question> countList = questionDao.getQuestionByPropertyId(scopeGroupId);
				List<Question> resultList = questionDao.getQuestionByProperty(scopeGroupId, searchContainer.getStart(), searchContainer.getEnd()); **/
				List<Question> countList = questionDao.getQuestionByPropertyIdAndPollId(scopeGroupId, 0);
				List<Question> resultList = questionDao.getQuestionByPropertyIdAndPollId(scopeGroupId, 0,searchContainer.getStart(),searchContainer.getEnd());
				searchContainer.setDelta(20);
				searchContainer.setTotal(countList.size());
				searchContainer.setResults(resultList);
				List<ResultRow> resultRows = searchContainer.getResultRows();
				int rowCoutner = 0;
				/** Looping through list to set rows in Search Container **/
				for (Question question : resultList) {
					ResultRow resultRow = new ResultRow(question, question.getQuestionId(), rowCoutner);
					resultRow.addText("center", "middle", question.getTitle());
					HttpServletRequest request = PortalUtil.getHttpServletRequest(renderRequest);
					resultRow.addJSP("center",SearchEntry.DEFAULT_ALIGN, "/jsps/vote/add_authorised_permission.jsp", request.getSession().getServletContext(),request, PortalUtil.getHttpServletResponse(renderResponse));
					resultRows.add(resultRow);
					rowCoutner++;
				}
				renderRequest.setAttribute("question-list", searchContainer);
			} catch (PortalException e) {
				log.error("error in generating vote list :: ", e);
				renderRequest.setAttribute("error", "Something went wrong. Please try after some time.");
			} catch (SystemException e) {
				log.error("error in generating vote list :: ", e);
				renderRequest.setAttribute("error", "Something went wrong. Please try after some time.");
			}
			include("/jsps/vote/view.jsp", renderRequest, renderResponse);
		}
	}
	
	/***
	 * Method for ADD or UPDATE Choices / Question
	 * @param actionRequest
	 * @param actionResponse
	 * @throws PortalException
	 * @throws SystemException
	 */
	public void editQuestionURL(ActionRequest actionRequest,ActionResponse actionResponse) throws PortalException, SystemException{
		log.info("/** add edit choices  **/");
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		User user = themeDisplay.getUser();
		
		/** Count the Number of Choices of Questions **/
		int choiceCount = ParamUtil.getInteger(actionRequest, "choiceContentList");
		log.info("found choiceCountList " +choiceCount);
		//int choiceCount = ParamUtil.getInteger(actionRequest, "choicesCount");
		log.info("Number of Count : " + choiceCount );
		/** Get the Question Id for check Record is New or Not **/
		int questionId = ParamUtil.getInteger(actionRequest, "questionId");
		log.info("Edit Question Id " + questionId );
		/** check Question is New or Not **/
		String isNew = ParamUtil.getString(actionRequest, "isNew",StringPool.BLANK);
		/** Get the Value from the Forms  **/
		String questioinTitle = ParamUtil.getString(actionRequest, "txtQuestionTitle",StringPool.BLANK);
		String questionDesc = ParamUtil.getString(actionRequest, "txtQuestionDesc",StringPool.BLANK);
		
		String sDate = ParamUtil.getString(actionRequest, "startdate");
		sDate+=" 00:00:01";
		String eDate = ParamUtil.getString(actionRequest, "enddate");
		eDate+=" 23:23:59";
		Date questionStartDate=null,questionEndDate=null;
		try {
				questionStartDate = dateFormat.parse(sDate);
				questionEndDate = dateFormat.parse(eDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		QuestionDao questionDao = ApplicationContextHolder.getContext().getBean(QuestionDao.class);
		Question question;
		/** Check Question Id is for Update or Add  **/
		if(questionId > 0){
			question = questionDao.findbyQuestionId(questionId);
		} else {
			question = new Question();
		}
		/** set value in Question Object  **/
		question.setTitle(questioinTitle);
		question.setDescription(questionDesc);
		question.setType("selectQuestion");
		question.setCreated_by(user.getFirstName());
		question.setStart_date(questionStartDate);
		question.setEnd_date(questionEndDate);
		question.setPropertyId(PortalUtil.getScopeGroupId(actionRequest));
		if(questionId>0){question.setModified_date(new Date());}
		else{question.setCreated_date(new Date());}
		
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
	}
	/***
	 * 
	 * @param actionRequest
	 * @param actionResponse
	 */
	public void deleteVoteQuestionURL(ActionRequest actionRequest,ActionResponse actionResponse){
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
		/** NOT REMOVE ANY ENTRY FROM USERVOTE **/
		log.info("Question Id delete success fully : " + questionId );
	}
	
	/***
	 * 
	 * @param request
	 * @param action
	 * @return
	 * @throws PrincipalException
	 * @throws PortalException
	 * @throws SystemException
	 */
	public boolean checkAuthoriseUser(PortletRequest request,String action) throws PrincipalException, PortalException, SystemException {
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);
		//PortletDisplay portletDisplay = (PortletDisplay)request.getAttribute(WebKeys.PORTLET_ID);
		PortletDisplay portletDisplay= themeDisplay.getPortletDisplay();
		Layout layout = themeDisplay.getLayout();
		long plid = layout.getPlid();
		String portletId = portletDisplay.getId();
		PermissionChecker permissionChecker = PermissionThreadLocal.getPermissionChecker();
		
		try {
			PortletPermissionUtil.check(permissionChecker,portletId , action);
			return true;
		} catch (PrincipalException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("NO Permission of THIS USER");
		}
		return false;
	}
	
	/**
	 * Select vote url
	 * @param actionRequest
	 * @param actionResponse
	 *  
	 */
	public void selectedVoteURL(ActionRequest actionRequest,ActionResponse actionResponse){
		/** Get Current Login User **/
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		User user = themeDisplay.getUser();
		long userId = user.getUserId();
		/** Retrive the Values from Give Vote by User, Form **/
		long questionId = ParamUtil.getLong(actionRequest, "questionId");
		long choiceId = ParamUtil.getLong(actionRequest, "choiceId"); 
		long selectedChoiceId = ParamUtil.getLong(actionRequest, "selectedChoice");
		UserVoteDao userVoteDao = ApplicationContextHolder.getContext().getBean(UserVoteDao.class);
		UserVote userVote=null;
		/** Check Update Or New UserVote **/
		if(choiceId > 0){
			 userVote = userVoteDao.voteOrNoteByQuestionId(userId, questionId);
		}else{
			userVote = new UserVote();
		}
		userVote.setQuestionId(questionId);
		userVote.setChoiceId(selectedChoiceId);
		userVote.setVoted_date(new Date());
		userVote.setUserId(userId);
		userVoteDao.addUserVote(userVote);
	}
	/**
	 * Delete Choice url
	 * @param resourceRequest
	 * @param resourceResponse
	 * @throws IOException
	 * @throws PortletException
	 */
	public void serveResource(ResourceRequest resourceRequest,ResourceResponse resourceResponse) throws IOException,PortletException {
		String operation = ParamUtil.getString(resourceRequest, "ajax",StringPool.BLANK);
		
		if("deleteChoice".equals(operation)){
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
		} else if("getUser".equals(operation)){
			
			HttpServletRequest httpRequest = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
			HttpServletResponse httpResponse = PortalUtil.getHttpServletResponse(resourceResponse);
			ServletContext servletContext = httpRequest.getSession().getServletContext();
			
			String choiceId = ParamUtil.getString(httpRequest, "choiceId", StringPool.BLANK);
			log.info("call from USERID: "+choiceId);
			UserVoteDao userVoteDao = ApplicationContextHolder.getContext().getBean(UserVoteDao.class);
			List<UserVote> userVoteByList = userVoteDao.findUserIdChoiceId(Long.parseLong(choiceId));
			JSONObject resultObj = JSONFactoryUtil.createJSONObject();
			httpRequest.setAttribute("userVoteByList", userVoteByList);
			StringBuilder result= new StringBuilder();
			try {
				result.append(RenderHelper.renderPage(servletContext, httpRequest, httpResponse, "/jsps/vote/dialog_view.jsp"));  
			     } 
			catch (ServletException e) {
			       e.printStackTrace();
			    }
			
			PrintWriter out= resourceResponse.getWriter();
			out.println(result.toString());
		}else if("coEfficient".equals(operation)){
			
			HttpServletRequest httpRequest = PortalUtil.getOriginalServletRequest(PortalUtil.getHttpServletRequest(resourceRequest));
			HttpServletResponse httpResponse = PortalUtil.getHttpServletResponse(resourceResponse);
			ServletContext servletContext = httpRequest.getSession().getServletContext();
			
			String choiceId = ParamUtil.getString(httpRequest, "choiceId", StringPool.BLANK);
			log.info("COEfficient from USERID: "+choiceId);
			UserVoteDao userVoteDao = ApplicationContextHolder.getContext().getBean(UserVoteDao.class);
			
			long scopeGroupId;
			try {
				scopeGroupId = PortalUtil.getScopeGroupId(httpRequest);
				log.info("groupId : " + scopeGroupId );
				List userVoteByCoefficent = userVoteDao.findUserCoefficiencyChoiceId(Long.parseLong(choiceId),scopeGroupId);
				httpRequest.setAttribute("userVoteByCoefficent", userVoteByCoefficent);
			} catch (PortalException e1) {
				log.error("error in find user by Coefficent :: ", e1);
			} catch (SystemException e1) {
				log.error("error in find user by Coefficent :: ", e1);
			}
			
			StringBuilder result= new StringBuilder();
			try {
				result.append(RenderHelper.renderPage(servletContext, httpRequest, httpResponse, "/jsps/vote/dialog_coefficient_view.jsp"));  
			     } 
			catch (ServletException e) {
			       e.printStackTrace();
			    }
			PrintWriter out= resourceResponse.getWriter();
			out.println(result.toString());
		}
		
	}
	private static Log log = LogFactoryUtil.getLog(VoteController.class);
}
