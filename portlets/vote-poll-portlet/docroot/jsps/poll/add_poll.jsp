<%@include file="init.jsp" %>
<%
Poll poll = (Poll) request.getAttribute("poll");
SearchContainer<Question> searchContainer = (SearchContainer<Question>) renderRequest.getAttribute("question-list");
%>
<portlet:resourceURL var="addPollQuestionURL">
<portlet:param name="ajax" value="addQuestion"/>
</portlet:resourceURL>

<portlet:renderURL var="redirect" />
<portlet:actionURL var="editPollURL" name="editPollURL" >
</portlet:actionURL>
<fmt:formatDate var="startDate" pattern="MM/dd/yyyy" value="${poll.start_date}" />
<fmt:formatDate var="endDate" pattern="MM/dd/yyyy" value="${poll.end_date}" />


 <c:if test='${dispMessage}'>
 	<div class="portlet-msg-alert"><liferay-ui:message key="for-this-poll-vote-has-been-given-already"/></div>
</c:if>

<form method="post" name="<portlet:namespace/>pollForm" id='<portlet:namespace/>pollForm'>
<input type="hidden" id="<portlet:namespace/>pollId" name="<portlet:namespace/>pollId" value="<%=poll.getPollId()%>" />
 
<liferay-ui:header 
		backURL="<%=redirect%>" title='${isNew == true ? "add-poll" : "edit-poll"}' />
	
<aui:input name="txtPollTitle" label="poll-title" value="<%=poll.getPollTitle() %>" />

<div class="control-group">
		<label class="control-label"><liferay-ui:message key='startdate'/></label> 
		<input type="text" id="<portlet:namespace/>startdate" name='<portlet:namespace/>startdate' value="${startDate}"/>
</div>
<div class="control-group">
		<label class="control-label"><liferay-ui:message key='enddate'/></label> 
		<input type="text" id="<portlet:namespace/>enddate" name='<portlet:namespace/>enddate' value="${endDate}"/>
</div>
<aui:button-row>
		<aui:button type="submit" onClick="submitForm();" value="submit"/>
		<aui:button type="cancel" value="cancel" href="<%=redirect%>" />
</aui:button-row>

</form>
<c:choose>
	<c:when test="${containerMax >= 10}">
				<div class="portlet-msg-error"><liferay-ui:message key="maximum-ten-question-have-been-added-already"/></div>
				<div style="margin-top: 10px;">
					<liferay-ui:search-iterator searchContainer="<%=searchContainer%>" type="article" />
				</div>
	</c:when>
	<c:otherwise>
		<c:if test="${poll.pollId > 0}">
			<input type="button" onclick="javascript:<portlet:namespace/>addPollQuestion('<%=poll.getPollId()%>');" value="<liferay-ui:message key='add-question'/>" class="btn" />
			<div style="margin-top: 10px;">
				<liferay-ui:search-iterator searchContainer="<%=searchContainer%>" type="article" />
			</div>
		</c:if>
	</c:otherwise>
</c:choose>
<script type="text/javascript" charset="utf-8">

submitForm = function(){
	/* $("#<portlet:namespace/>choiceContentList").val($(".choiceList").length); */
  	document.<portlet:namespace/>pollForm.action ="<%=editPollURL%>";
  	$("#<portlet:namespace/>pollForm").submit(); 	 
};

$(function() {
	  $("#<portlet:namespace/>startdate").datepicker({
	         dateFormat: "mm/dd/yy",
	         minDate: 0,
	         onSelect: function () {
	             var dt2 = $('#<portlet:namespace/>enddate');
	             var startDate = $(this).datepicker('getDate');
	             //add 30 days to selected date
	             startDate.setDate(startDate.getDate() + 30);
	             var minDate = $(this).datepicker('getDate');
	             //minDate of dt2 datepicker = dt1 selected day
	             dt2.datepicker('setDate', minDate);
	             //sets dt2 maxDate to the last day of 30 days window
	             dt2.datepicker('option', 'maxDate', startDate);
	             //first day which can be selected in dt2 is selected date in dt1
	             dt2.datepicker('option', 'minDate', minDate);
	             //same for dt1
	             $(this).datepicker('option', 'minDate', minDate);
	         }
	     });
	     $('#<portlet:namespace/>enddate').datepicker({
	         dateFormat: "mm/dd/yy"
	     });
});

<portlet:namespace/>addPollQuestion = function(pollId){
	var url = "<%=addPollQuestionURL.toString()%>&pollId="+pollId; 
	var mytitle = "Poll Question Information";
	$("#dialogPollQuestion").load(url,null,function() {
		$("#dialogPollQuestion").dialog({
			title: mytitle,
			modal: true,
			width: 700,
			height: 500,
			close: function(event, ui) {
			$("#dialogPollQuestion").empty(); // remove the content
			} //END CLOSE
		});//END DIALOG
	});//END DIALOG
  }
  
</script>
<div id="dialogPollQuestion"  class="dialogPollQue"></div>
