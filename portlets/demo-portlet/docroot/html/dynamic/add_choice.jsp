<%@ include file="init.jsp" %>
<%@page import="javax.portlet.WindowState"%>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<portlet:defineObjects />
<%@page import="javax.portlet.ActionRequest"%>

<portlet:actionURL var="editArticleActionURL" windowState="<%= WindowState.NORMAL.toString()%>">
  <portlet:param name="<%=ActionRequest.ACTION_NAME%>" value="getAutoFieldsData" />
</portlet:actionURL>

<aui:form action="<%=editArticleActionURL%>" method="post" name="LiferayAautoFieldForm">
<div id="separator"></div>
<h5>Question Id : ${questionId}</h5>
<aui:input name="txtQuestionTitle" label="question-title" />
<aui:input name="txtQuestionDescription" label="question-description" type="textarea"  />
  
  <div id="phone-fields">
    <div class="lfr-form-row lfr-form-row-inline">
      	<div class="row-fields">
        	<aui:input fieldParam='txtChoice0' id='txtChoice0' name="txtChoice0" label="choice" />
        </div>
    </div>
  </div>
  <aui:layout>
    <aui:column>
      <aui:button type="submit" value="Save Phone Numbers" name="SavePhoneNumbers" onClick="saveData();"></aui:button>
    </aui:column>
  </aui:layout>
  <aui:script use="liferay-auto-fields">
    new Liferay.AutoFields(
    {
    contentBox: '#phone-fields',
    fieldIndexes: '<portlet:namespace />phonesIndexes'
    }
    ).render();
  </aui:script>
</aui:form>