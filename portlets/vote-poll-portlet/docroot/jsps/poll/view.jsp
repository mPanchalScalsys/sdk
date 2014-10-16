<%@include file="init.jsp" %>
<portlet:renderURL var="addPollURL">
<portlet:param name="operation" value="addPoll"/>
</portlet:renderURL>
<%
	SearchContainer<Poll> searchContainer = (SearchContainer<Poll>) renderRequest.getAttribute("poll-list");
%>
<c:choose>
	<c:when test="<%= themeDisplay.isSignedIn() %>">
		<c:if test='<%= themeDisplay.getPermissionChecker().hasPermission(themeDisplay.getScopeGroupId(), "com.application.model.Question", 0, "ADD")%>'>
			<aui:button value="add-new-poll" name="addPoll"	onClick="<%=addPollURL%>" />
		</c:if>
		<div style="margin-top: 10px;">
			<liferay-ui:search-iterator searchContainer="<%=searchContainer %>" type="article" />
		</div>
	</c:when>
	<c:otherwise>
		<a href="<%=themeDisplay.getURLSignIn()%>"><liferay-ui:message key="please-sign-in-for-vote-into-poll" /></a>
	</c:otherwise>
</c:choose>