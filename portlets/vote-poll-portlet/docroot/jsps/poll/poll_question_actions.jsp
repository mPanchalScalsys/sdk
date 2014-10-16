<%@ include file="init.jsp" %>
<%
ResultRow row= (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
Question question = (Question) row.getObject();
String popup = "javascript:editPollQuestion('"+ question.getQuestionId()+"');";
String deleteQueUrl = "javascript:" + renderResponse.getNamespace() + "deletequestionofpoll('"+question.getQuestionId()+"');";
%>
<portlet:resourceURL var="addPollQuestionURL">
	<portlet:param name="ajax" value="editQuestion"/>
	<portlet:param name="quePollId" value="<%=String.valueOf(question.getPollId())%>"/>
</portlet:resourceURL>



<portlet:actionURL var="deletePollQuestionURL" name="deletePollQuestionURL">
	<portlet:param name="deletePollQuestionId" value="<%=String.valueOf(question.getQuestionId())%>"/>
</portlet:actionURL>

<input type="hidden" id="<%=question.getQuestionId()%>_delta" value="<%=deletePollQuestionURL.toString()%>"/>

<liferay-ui:icon image="edit" label="edit-poll" message="edit-poll" url="<%=popup%>" />
<liferay-ui:icon image="delete" label="delete-poll" message="delete-poll" url="<%=deleteQueUrl%>"/>


<script type="text/javascript" charset="utf-8">

editPollQuestion = function(questionId){
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
<portlet:namespace/>deletequestionofpoll = function(id){
	var cresult=confirm("<liferay-ui:message key='delete-confirmation' />");
		if(cresult){
			var url = document.getElementById(id+"_delta").value;
			submitForm(document.hrefFm, url);		
		}
	}
  </script>
  <div id="dialogPollQuestions" class="dialogPollQue"></div>