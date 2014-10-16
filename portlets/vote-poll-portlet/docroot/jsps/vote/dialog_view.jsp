<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@ include file="init.jsp" %>
<div class="container">
    <div class="heading">
         <div class="col">UserID</div>
		 <div class="col">Username </div>
    </div>
	<c:forEach var="vote" items="${userVoteByList}">
	<% 
	 UserVote userVote = (UserVote)pageContext.getAttribute("vote");
	 User lfrUser = UserLocalServiceUtil.getUserById(userVote.getUserId());
	 pageContext.setAttribute("lfrUser", lfrUser);
	%>
	<div class="table-row">
		    <div class="col">${vote.userId}</div>
			<div class="col">${lfrUser.fullName}</div>
	</div>
	</c:forEach>
</div>
