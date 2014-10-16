<%@page import="javax.portlet.ActionRequest"%>
<%@ include file="init.jsp" %>
<portlet:renderURL var="addVoteURL">
	<portlet:param name="operation" value="addVote"/>
</portlet:renderURL>
<%
	SearchContainer<Question> searchContainer = (SearchContainer<Question>) renderRequest.getAttribute("question-list");
%>
<c:choose>
	<c:when test="<%= themeDisplay.isSignedIn() %>">
		<c:if test='<%= themeDisplay.getPermissionChecker().hasPermission(themeDisplay.getScopeGroupId(), "com.application.model.Question", 0, "ADD")%>'>
			<aui:button value="add-new-vote-question" name="addVote"	onClick="<%=addVoteURL %>" />
		</c:if>
		<div style="margin-top: 10px;">
			<liferay-ui:search-iterator searchContainer="<%=searchContainer %>" type="article" />
		</div>
	</c:when>
	<c:otherwise>
		<a href="<%=themeDisplay.getURLSignIn()%>"><liferay-ui:message key="please-sign-in-for-vote" /></a>
	</c:otherwise>
</c:choose>

