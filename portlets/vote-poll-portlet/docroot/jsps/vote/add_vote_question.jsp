<%@ include file="init.jsp" %>

<%
	Question question = (Question)request.getAttribute("question");
	List<Choices> choices = (List<Choices>)request.getAttribute("choices");
	String checkNew = (String)request.getAttribute("new");
	boolean isNew = "true".equals(checkNew);
	boolean alreadyVoted = (Boolean)request.getAttribute("voteGaved");
	
	int choicesCount = choices.size();
	choicesCount = choicesCount == 0 ? 2 : choicesCount;
	System.out.println("\n \n choiceCount " +choicesCount);
	System.out.println("\n \n VOTED OR NOT: " + alreadyVoted );
%>
	

<fmt:formatDate var="startDate" pattern="MM/dd/yyyy" value="${question.start_date}" />
<fmt:formatDate var="endDate" pattern="MM/dd/yyyy" value="${question.end_date}" />

<portlet:actionURL var="editQuestionURL" name="editQuestionURL"></portlet:actionURL>
<portlet:renderURL var="redirect" />
<portlet:resourceURL var="removeChoiceAjaxURLS">
	<portlet:param name="ajax" value="deleteChoice"/>
</portlet:resourceURL>



<form method="post" name="<portlet:namespace/>choiceForm" id='<portlet:namespace/>choiceForm'>

<input type="hidden" id="<portlet:namespace/>contactUId" name="<portlet:namespace/>contactUId" value='' />
<input type="hidden" id='<portlet:namespace/>questionId' name='<portlet:namespace/>questionId' value="<%=question.getQuestionId()%>" />
<input type="hidden" id='<portlet:namespace/>isNew' name='<portlet:namespace/>isNew' value="<%=isNew%>" />
<input type="hidden" id='<portlet:namespace/>choiceContentList' name='<portlet:namespace/>choiceContentList' value="" />
<%-- <input type="hidden" id='<portlet:namespace/>' name='<portlet:namespace/>' value="" />
<input type="hidden" id='<portlet:namespace/>' name='<portlet:namespace/>' value="" /> --%>

<c:if test='${voteGaved}'>
 <div class="portlet-msg-error"><liferay-ui:message key="for-this-question-vote-has-been-given-already"/></div>
</c:if>


<liferay-ui:header 
		backURL='<%=redirect.toString()%>'
		title='<%= isNew ? "new-question" : "edit-question" %>'
	/>
	<div class="control-group">
		<label class="control-label"><liferay-ui:message key='question-title'/></label> 
		<input type="text" id="<portlet:namespace/>txtQuestionTitle" name='<portlet:namespace/>txtQuestionTitle' value="<%=question.getTitle() %>" required/>
	</div>
	<div class="control-group">
		<label class="control-label"><liferay-ui:message key='description'/></label> 
		<textarea id="<portlet:namespace/>txtQuestionDesc" name='<portlet:namespace/>txtQuestionDesc' value="<%=question.getDescription() %>" ></textarea>
	</div>

<div id="addNewContacts">
		<input type="button" value="<liferay-ui:message key='add-choice'/>" onclick="<portlet:namespace/>addNewChoices();"  ${voteGaved ? "disabled" : "" } class="btn" />
</div>
<div id="choiceDynamicDiv">
	<c:choose>
		<c:when test="<%=!isNew%>">
			<c:forEach items="<%=choices%>" var="choice" varStatus="i">
				<div id="choiceList${i.index+1}"  class="choiceList">
					<div id="choiceField${i.index+1}" style="width:50%">
						<div id="choiceIncludedItem${i.index+1}">
							<span id="citem"></span>
							<span><input type="hidden" name='<portlet:namespace/>choiceRegisterId${i.index+1}' id='<portlet:namespace/>choiceRegisterId${i.index+1}' value='${choice.choiceId}' />
							<table>
								<tr>
									<td>
										<div class="control-group">
												<label class="control-label"><liferay-ui:message key='choice'/></label> 
												<input type="text" name='<portlet:namespace/>includedChoice${i.index+1}' id='<portlet:namespace/>includedChoice${i.index+1}' value='${choice.description}' />
										</div>
									</td>
									<td>
											<c:choose>
																<c:when test="${i.index+1 > 1}">
																	<c:choose>
																		<c:when test="<%= question.getCreated_date() != null %>">
																			<input type="button" name='<portlet:namespace/>removeChoicebtn${i.index+1}' style="width: 22px; margin-top:18px;" id='<portlet:namespace/>removeChoicebtn${i.index+1}' value="-" onclick="<portlet:namespace/>ajaxRemoveChoiceDetail('${i.index+1}');" ${voteGaved ? "disabled" : "" } />
																		</c:when>
																		<c:otherwise>
																			<input type="button" name='<portlet:namespace/>removeChoicebtn${i.index+1}' style="width: 22px; margin-top:18px;" id='<portlet:namespace/>removeChoicebtn${i.index+1}' value="-" onclick="<portlet:namespace/>removeChoicebtn('${i.index+1}');"/>
																		</c:otherwise>
																	</c:choose>
																</c:when>
																<c:otherwise>
																	<input type="button" name='<portlet:namespace/>removeChoicebtn${i.index+1}' style="display:none;width: 22px; margin-top:18px;" id='<portlet:namespace/>removeChoicebtn${i.index+1}' value="-" onclick="<portlet:namespace/>removeChoicebtn('${i.index+1}');"/>
																</c:otherwise>
											</c:choose>
										<div style="clear:both;"></div>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</c:forEach>
		</c:when>
		<c:otherwise>
				<div id="choiceList1" class="choiceList">
					<div id="choiceField1" style="width:50%">
						<div id="choiceIncludedItem1">
							<span id="citem1"></span>
							<input type="hidden" name='<portlet:namespace/>choiceRegisterId1' id='<portlet:namespace/>choiceRegisterId1' />
								<table>
									<tr>
										<td>
										<div class="control-group">
												<label class="control-label"><liferay-ui:message key='choice'/></label> 
												<input type="text" name='<portlet:namespace/>includedChoice1' id='<portlet:namespace/>includedChoice1' required/>
										</div>
										</td>
										<td>
												<input type="button" name='<portlet:namespace/>removeChoicebtn1' style="display:none;width: 22px; margin-top:18px;" id='<portlet:namespace/>removeChoicebtn1' value="-" onclick="<portlet:namespace/>removeChoicebtn('1');" />
												<div style="clear:both;"></div>
										</td>
									</tr>
								</table>
						</div>
					</div>
				</div>
		</c:otherwise>
	</c:choose>
</div>
<div class="control-group">
		<label class="control-label"><liferay-ui:message key='startdate'/></label> 
		<input type="text" id="<portlet:namespace/>startdate" name='<portlet:namespace/>startdate' value="${startDate}"  required/>
</div>
<div class="control-group">
		<label class="control-label"><liferay-ui:message key='enddate'/></label> 
		<input type="text" id="<portlet:namespace/>enddate" name='<portlet:namespace/>enddate' value="${endDate}" required/>
</div>
<aui:button-row>
		<aui:button type="submit" onClick="submitForm();" value="submit"/>
		<aui:button type="cancel" value="cancel" href="<%=redirect%>" />
</aui:button-row>
</form>

<script type="text/javascript" charset="utf-8">

<portlet:namespace/>addNewChoices = function(){
	var clonnObject = $("#choiceList1").clone(); 
	var nCounter = $(".choiceList").length+1;
	
	$(clonnObject).attr("id","choiceList"+nCounter);
	$(clonnObject).find("#choiceField1").attr("id","choiceField"+nCounter);
	$(clonnObject).find("#choiceIncludedItem1").attr("id","choiceIncludedItem"+nCounter);
	$(clonnObject).find("#citem1").attr("id","citem"+nCounter);
	
	$(clonnObject).find("#<portlet:namespace/>choiceRegisterId1").attr("id","<portlet:namespace/>choiceRegisterId"+nCounter);
	$(clonnObject).find("#<portlet:namespace/>choiceRegisterId"+nCounter).attr("name","<portlet:namespace/>choiceRegisterId"+nCounter);
	$(clonnObject).find("#<portlet:namespace/>choiceRegisterId"+nCounter).val('');
	
	$(clonnObject).find("#<portlet:namespace/>includedChoice1").attr("id","<portlet:namespace/>includedChoice"+nCounter);
	$(clonnObject).find("#<portlet:namespace/>includedChoice"+nCounter).attr("name","<portlet:namespace/>includedChoice"+nCounter);
	$(clonnObject).find("#<portlet:namespace/>includedChoice"+nCounter).val('');
	
	$(clonnObject).find("#<portlet:namespace/>removeChoicebtn1").attr("id","<portlet:namespace/>removeChoicebtn"+nCounter);
	$(clonnObject).find("#<portlet:namespace/>removeChoicebtn"+nCounter).attr("name","<portlet:namespace/>removeChoicebtn"+nCounter);
	$(clonnObject).find("#<portlet:namespace/>removeChoicebtn"+nCounter).attr("onclick","<portlet:namespace/>removeChoicebtn('"+nCounter+"');");
	
	$(clonnObject).find("#<portlet:namespace/>removeChoicebtn"+nCounter).show();
	
	$("#choiceDynamicDiv").append(clonnObject);
	
}

	submitForm = function(){
		$("#<portlet:namespace/>choiceContentList").val($(".choiceList").length);
	  	document.<portlet:namespace/>choiceForm.action ="<%=editQuestionURL.toString()%>";
	  	if($("#<portlet:namespace/>choiceForm").valid()){
	  		$("#<portlet:namespace/>choiceForm").submit(); 	
	  	} 
	};

<portlet:namespace/>removeChoicebtn = function(index, confirmDelete){
	if(!confirmDelete){
		confirmDelete = confirm("<liferay-ui:message key='confirm-delete-choice' />");
		if(!confirmDelete){
			return;
		}
	}
	$('#choiceList'+index).remove();
};

<portlet:namespace/>ajaxRemoveChoiceDetail = function(index){
	var choiceRegisterId = $('#choiceList'+index).find('#<portlet:namespace/>choiceRegisterId'+index).val();
	var confirmDelete = confirm("<liferay-ui:message key='confirm-delete-of-add-choice'/>");
	if(!confirmDelete){
		return;
	}
	jQuery.ajax({
		url:"<%=removeChoiceAjaxURLS%>",
		type:'POST',
		dataType:"JSON",
		data:{
			<portlet:namespace/>cRegisterId:choiceRegisterId,
		},
		success:function(result){
			if(result.status == "success"){
					<portlet:namespace/>removeChoicebtn(index,confirmDelete);
			}
	  	}
	});
	
}

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

</script>
