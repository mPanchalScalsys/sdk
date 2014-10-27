<%@ include file="init.jsp" %>
<% boolean voteStatus = (Boolean)request.getAttribute("isVoted");  %>
<portlet:actionURL var="pollQuestionVoteURL" name="pollQuestionVoteURL">
</portlet:actionURL>
<portlet:renderURL var="renderBackURL"></portlet:renderURL>

<aui:form method="POST" action="<%=pollQuestionVoteURL%>" name="pollVoteForm">
<aui:input name="pollId" type="hidden" value="${pollId}" />  
<aui:input name="totalQuestion" type="hidden" value="${questionCount}"/>
<aui:input name="pollStatus" type="hidden" value="<%=voteStatus%>"/>
<c:forEach var="question" items="${questionsByPollId}" varStatus="i">
	<aui:input name="questionId${question.questionId}" type="hidden" value="${question.questionId}"/>
	<h3>${i.index+1}).${question.title}</h3>
		<c:choose>
			<c:when test="${question.type eq 'selectQuestion' }">
			<aui:input name="selectQuestion${question.questionId}" type="hidden" value="selectQuestion" />
				<%
					Question question = (Question)pageContext.getAttribute("question");
					//long pollId = (Long)pageContext.getAttribute("pollId");
					ChoicesDao choiceDao = ApplicationContextHolder.getContext().getBean(ChoicesDao.class);
					List<Choices> choices = choiceDao.findChoicesByQuestionId(question.getQuestionId());
					pageContext.setAttribute("choices", choices);
					if(voteStatus){
					UserVoteDao userVoteDao = ApplicationContextHolder.getContext().getBean(UserVoteDao.class);
					UserVote userVote = userVoteDao.voteOrNoteByQuestionId(user.getUserId(), question.getQuestionId());
					pageContext.setAttribute("userVote", userVote);
					}
				%>
				<table>
					<c:forEach var="choice" items="${choices}" varStatus="j">
						<tr><td>${j.index+1}).</td><td><aui:input inlineLabel="right" name="choiceId${question.questionId}" type="radio" value="${choice.choiceId}" label="${choice.description}" checked='${choice.choiceId == userVote.choiceId}'><aui:validator name="required"/></aui:input></td></tr>
					</c:forEach>
				</table>
			</c:when>
			<c:otherwise>
			  <%
				  if(voteStatus){
					 Question question = (Question)pageContext.getAttribute("question");
					 UserVoteDao userVoteDao = ApplicationContextHolder.getContext().getBean(UserVoteDao.class);
					 UserVote userVote = userVoteDao.voteOrNoteByQuestionId(user.getUserId(), question.getQuestionId());
					 pageContext.setAttribute("userVote", userVote); 
					 }
			 %>  
					<aui:input name="textAnswers${question.questionId}" type="textarea" label="" value="${userVote.user_open_vote_desc}" ><aui:validator name="required"/></aui:input> 
			</c:otherwise>
		</c:choose>	
</c:forEach>

<aui:button-row>
		<aui:button type="submit" />
		<aui:button href="<%=renderBackURL%>" type="cancel" />
	</aui:button-row>

</aui:form>
