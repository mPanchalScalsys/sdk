<%@ include file="init.jsp" %>
<%
ResultRow row= (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
Question question = (Question) row.getObject();
%>

<portlet:resourceURL var="addPollQuestionURL">
<portlet:param name="ajax" value="editQuestion"/>
<portlet:param name="quePollId" value="<%=String.valueOf(question.getPollId())%>"/>
</portlet:resourceURL>
<portlet:actionURL var="deleteQuestionURL" name="deleteQuestionURL">
	<portlet:param name="deleteQuestionId" value="<%=String.valueOf(question.getQuestionId())%>"/>
</portlet:actionURL>

<% 
	String deleteQuestionUrl = "javascript:" + renderResponse.getNamespace() + "deleteVoteQuestion('"+question.getQuestionId()+"')";
%>
<input type="hidden" id="<%=question.getQuestionId()%>_delete" value="<%=deleteQuestionURL.toString()%>"/>

<input type="button" onclick="javascript:<portlet:namespace/>editPollQuestion('<%=question.getQuestionId()%>');" value="<liferay-ui:message key='edit'/>" />
<%-- <liferay-ui:icon image="edit" label="edit-poll" message="edit-poll" onClick="javascript:<portlet:namespace/>editPollQuestion('<%=question.getPollId()%>');"/> --%>

<liferay-ui:icon image="delete" label="delete-poll" message="delete-poll" url="<%=deleteQuestionUrl%>"/>


<script type="text/javascript" charset="utf-8">

<portlet:namespace/>editPollQuestion = function(questionId){
	alert('click edit question');
	var url = "<%=addPollQuestionURL.toString()%>&questionId="+questionId;
	var mytitle = "Poll Question Information";
	$("#dialogPollQuestions").load(url,null,function() {
		$("#dialogPollQuestions").dialog({
			title: mytitle,
			modal: true,
			width: 700,
			height: 500,
			close: function(event, ui) {
			$("#dialogPollQuestions").empty(); // remove the content
			} //END CLOSE
		});//END DIALOG
	});//END DIALOG
  }
  
<portlet:namespace/>deleteVoteQuestion = function(id){
	var result=confirm("<liferay-ui:message key='question-delete-confirmation' />");
		if(result){
			var url = document.getElementById(id+"_delete").value;
			submitForm(document.hrefFm, url);		
		}
	};
  
  
  </script>
  <div id="dialogPollQuestions"></div>