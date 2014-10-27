<%@ include file="init.jsp" %>

<%
int i=1;
Question question = (Question)request.getAttribute("question");
List<Choices> choices = (List<Choices>) request.getAttribute("choices");
UserVote userVote = (UserVote)request.getAttribute("userVote");
%>
<portlet:renderURL var="cancelVoteURL" />
<portlet:actionURL var="selectedVoteURL" name="selectedVoteURL">
</portlet:actionURL>

<aui:form action="<%=selectedVoteURL%>" method="POST" name="selectionForm">
	<aui:input name="questionId" type="hidden" value="<%=question.getQuestionId()%>" />
	<aui:input name="choiceId" type="hidden" value="<%=userVote.getChoiceId()%>" />
	<h3><%=question.getTitle()%></h3>
	<table>
		<% for(Choices choice : choices){%> 
			<tr><td><h3></h3></td><td><aui:input inlineLabel="right" name="selectedChoice" type="radio" value="<%=choice.getChoiceId()%>" label="<%=choice.getDescription()%>" checked="<%= (choice.getChoiceId() == userVote.getChoiceId()) %>"/></td></tr>
		<% i++; } %>
	</table>
	<aui:button-row>
		<aui:button type="submit" />
		<aui:button href="<%=cancelVoteURL%>" type="cancel" />
	</aui:button-row>
</aui:form>