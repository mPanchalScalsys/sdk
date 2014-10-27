
<%@ include file="init.jsp" %>

<%
	long userId = themeDisplay.getUserId();
	ResultRow row= (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	Question question = (Question)row.getObject();
	
	UserVoteDao userVoteDao = ApplicationContextHolder.getContext().getBean(UserVoteDao.class);
	UserVote userVote = null;
	if(question != null){
		userVote = userVoteDao.voteOrNoteByQuestionId(userId, question.getQuestionId());
	}
	
	Long currentTime = System.currentTimeMillis();
	
	Long votingTime = question.getStart_date() != null ? question.getStart_date().getTime() : null;
	
	Long queEndTime = question.getEnd_date() != null ? question.getEnd_date().getTime() : null;
	
	boolean allowVoting = currentTime < queEndTime;
	
	boolean startVoting = currentTime > votingTime;
	
	String giveVoteSrc = themeDisplay.getPathThemeImages() + "/ratings/thumbs_up_icon.png";
	String editVoteSrc = themeDisplay.getPathThemeImages() + "/ratings/thumbs_up_icon_hover.png";
	String graphVoteSrc = themeDisplay.getPathThemeImages() + "/api/property.png";
%>
<portlet:actionURL var="deleteVoteQuestionURL" name="deleteVoteQuestionURL">
	<portlet:param name="deleteQuestionId" value="<%=String.valueOf(question.getQuestionId())%>"/>
</portlet:actionURL>
<portlet:renderURL var="displayGraphURL" windowState="normal">
	<portlet:param name="operation" value="displayVote"/>
	<portlet:param name="graphQuestionId" value="<%=String.valueOf(question.getQuestionId())%>"/>
</portlet:renderURL>
<portlet:renderURL var="editVoteQuestionURL" windowState="normal">
	<portlet:param name="operation" value="editVote"/>
	<portlet:param name="editQuestionId" value="<%=String.valueOf(question.getQuestionId())%>"/>
</portlet:renderURL>
<portlet:renderURL var="editGivenVoteURL" windowState="normal">
	<portlet:param name="operation" value="editChoice"/>
	<portlet:param name="questionId" value="<%=String.valueOf(question.getQuestionId())%>"/>
</portlet:renderURL>
<portlet:renderURL var="applyVoteURL">
	<portlet:param name="operation" value="giveVote"/>
	<portlet:param name="voteQuestionId" value="<%=String.valueOf(question.getQuestionId())%>"/>
</portlet:renderURL>
<% 
	String deleteQuestionURL = "javascript:" + renderResponse.getNamespace() + "deleteVoteQuestion('"+question.getQuestionId()+"')";
%>
<input type="hidden" id="<%=question.getQuestionId()%>_delete" value="<%=deleteVoteQuestionURL.toString()%>"/>
<c:choose>
 	<c:when test="<%= themeDisplay.getPermissionChecker().hasPermission(themeDisplay.getScopeGroupId(), \"com.application.model.Question\", question.getQuestionId(), \"UPDATE\") %>">
		<liferay-ui:icon image="edit" label="edit-vote" message="edit-vote" url="<%=editVoteQuestionURL%>"/>
		<liferay-ui:icon image="delete" label="delete-vote" message="delete-vote" url="<%=deleteQuestionURL%>"/>
 	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="<%=startVoting%>">
				<c:choose>
					<c:when test="<%=allowVoting%>">
						<c:choose>
							<c:when test="<%= (userVote!=null) %>">
								<liferay-ui:icon src="<%=editVoteSrc%>" message="edit-given-vote" label="edit-given-vote" url="<%=editGivenVoteURL%>"/>
							</c:when>
							<c:otherwise>
								<liferay-ui:icon src="<%=giveVoteSrc%>" message="give-vote" label="give-vote" url="<%=applyVoteURL%>"/>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<liferay-ui:icon src="<%=graphVoteSrc%>" message="display-graph" label="show-graph" url="<%=displayGraphURL%>"/>
					</c:otherwise>
				</c:choose>
			</c:when>
		<c:otherwise>
			<liferay-ui:icon image="assign" label="upcomming-vote" message="upcomming-vote" />
		</c:otherwise>
	 </c:choose>
 	</c:otherwise>
</c:choose>
<script type="text/javascript">
<portlet:namespace/>deleteVoteQuestion = function(id){
	var result=confirm("<liferay-ui:message key='delete-confirmation' />");
		if(result){
			var url = document.getElementById(id+"_delete").value;
			submitForm(document.hrefFm, url);		
		}
	};
</script>
