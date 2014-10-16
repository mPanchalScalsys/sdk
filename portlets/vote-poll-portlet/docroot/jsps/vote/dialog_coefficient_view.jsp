<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@ include file="init.jsp" %>
<div class="container">
    <div class="heading">
         <div class="col">username</div>
		 <div class="col">coefficentvalue</div>
    </div>
	<c:forEach var="vote" items="${userVoteByCoefficent}">  
	<% 
	 Object[] o = (Object[])pageContext.getAttribute("vote");
	%>
	<div class="table-row">
		    <div class="col"><%=o[1].toString()%></div>
			<div class="col"><%=o[2].toString()%></div>
	</div>
	</c:forEach>
</div>
