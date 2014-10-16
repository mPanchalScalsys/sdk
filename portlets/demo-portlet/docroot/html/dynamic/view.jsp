<%@ include file="init.jsp" %>
<%@page import="javax.portlet.WindowState"%>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<portlet:defineObjects />
<portlet:renderURL var="renderPageURL">
	<portlet:param name="renderPage" value="addChoice"/>
</portlet:renderURL>
<aui:input name="txtQuestionId" id="txtQuestionId" label="questionId" />
<aui:button name="btnRenderPage" onClick="${renderPageURL}" label="edit-page" />

