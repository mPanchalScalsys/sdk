<%@ include file="init.jsp" %>
<%
long userId = themeDisplay.getUserId();
long property_Id = themeDisplay.getScopeGroupId();
ResultRow row= (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
Poll poll = (Poll)row.getObject();


UserVoteDao userVoteDao = ApplicationContextHolder.getContext().getBean(UserVoteDao.class);
QuestionDao questionDao = ApplicationContextHolder.getContext().getBean(QuestionDao.class);

UserVote userVote = null;
boolean isVoted = false;
//Question question = null;
List<Question> questionsByPollId = null;
// QuestionDao questionDao -
if(poll != null){
	questionsByPollId = questionDao.getQuestionByPropertyIdAndPollId(property_Id, poll.getPollId());
	if(questionsByPollId != null){
		for(Question question : questionsByPollId){
			userVote = userVoteDao.voteOrNoteByQuestionId(userId, question.getQuestionId());
				if(userVote != null){ isVoted=true;break;}
		}
	}
}


Long currentTime = System.currentTimeMillis();

Long queEndTime = poll.getEnd_date() != null ? poll.getEnd_date().getTime() : null;
Long votingTime = poll.getStart_date() != null ? poll.getStart_date().getTime() : null;

boolean allowVoting = currentTime < queEndTime;
boolean startVoting = currentTime > votingTime;

String giveVoteSrc = themeDisplay.getPathThemeImages() + "/ratings/thumbs_up_icon.png";
String editVoteSrc = themeDisplay.getPathThemeImages() + "/ratings/thumbs_up_icon_hover.png";
String graphVoteSrc = themeDisplay.getPathThemeImages() + "/api/property.png";
%>
<portlet:renderURL var="editPollURL" windowState="normal">
	<portlet:param name="operation" value="editPoll"/>
	<portlet:param name="pollId" value="<%=String.valueOf(poll.getPollId())%>"/>
	<portlet:param name="voteStatus" value="<%=String.valueOf(isVoted)%>" />
</portlet:renderURL>
<portlet:actionURL var="deletePollURL" name="deleteVoteQuestionURL">
	<portlet:param name="deletePollId" value="<%=String.valueOf(poll.getPollId())%>"/>
</portlet:actionURL>
<portlet:renderURL var="addQuestionsToPollURL" windowState="normal">
	<portlet:param name="operation" value="addQuestion"/>
	<portlet:param name="pollIdForAddQuestion" value="<%=String.valueOf(poll.getPollId())%>"/>
</portlet:renderURL>
<portlet:renderURL var="applyVoteURL">
	<portlet:param name="operation" value="giveVoteOnPoll"/>
	<portlet:param name="pollIdForGiveVote" value="<%=String.valueOf(poll.getPollId())%>"/>
	<portlet:param name="voteStatus" value="<%=String.valueOf(isVoted)%>" />
</portlet:renderURL>
<portlet:renderURL var="editVoteURL">
	<portlet:param name="operation" value="editGivenVoteOnPoll"/>
	<portlet:param name="pollIdForEditVote" value="<%=String.valueOf(poll.getPollId())%>"/>
</portlet:renderURL>
<portlet:renderURL var="displayGraphURL" windowState="normal">
	<portlet:param name="operation" value="displayVote"/>
	<portlet:param name="graphPollId" value="<%=String.valueOf(poll.getPollId())%>"/>
</portlet:renderURL>
<% 
	String deletePollById = "javascript:" + renderResponse.getNamespace() + "deleteVoteQuestion('"+poll.getPollId()+"')";
%>
<input type="hidden" id="<%=poll.getPollId()%>_delete" value="<%=deletePollURL%>"/>

<c:choose>
	<c:when test="<%= themeDisplay.getPermissionChecker().hasPermission(themeDisplay.getScopeGroupId(), \"com.application.model.Poll\", poll.getPollId(), \"UPDATE\") %>">
		<liferay-ui:icon image="edit" label="edit-poll" message="edit-poll" url="<%=editPollURL.toString()%>"/>
		<liferay-ui:icon image="delete" label="delete-poll" message="delete-poll" url="<%=deletePollById%>"/>
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="<%=startVoting%>">
					<c:choose>
						<c:when test="<%=allowVoting%>">
							<c:choose>
								<c:when test="<%=isVoted%>">
									<liferay-ui:icon src="<%=editVoteSrc%>" message="edit-vote" label="give-vote" url="<%=applyVoteURL.toString()%>"/>
								</c:when>
								<c:otherwise>
									<liferay-ui:icon src="<%=giveVoteSrc%>" message="give-vote" label="give-vote" url="<%=applyVoteURL.toString()%>"/>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<liferay-ui:icon src="<%=graphVoteSrc%>" message="display-graph" label="show-graph" url="<%=displayGraphURL%>"/>
						</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<liferay-ui:icon image="assign" label="upcomming-poll" message="upcomming-poll" />
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>
<script type="text/javascript">
<portlet:namespace/>deleteVoteQuestion = function(id){
	var result=confirm("<liferay-ui:message key='delete-confirmation'/>");
		if(result){
			var url = document.getElementById(id+"_delete").value;
			submitForm(document.hrefFm, url);		
		}
	};
</script>
