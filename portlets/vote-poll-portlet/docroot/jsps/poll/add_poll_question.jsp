<%@include file="init.jsp" %>

<%

Poll poll = (Poll)request.getAttribute("poll");
Question question = (Question)request.getAttribute("question");
List<Choices> choices = (List<Choices>)request.getAttribute("choices");
String checkNew = (String)request.getAttribute("new");
String status = (String)request.getAttribute("btnSTATUS");
boolean notNew=true;
if("true".equals(checkNew)){
	notNew = false;
}
%>
<portlet:actionURL var="editPollQuestionURL" name="editPollQuestionURL">
</portlet:actionURL>

<portlet:resourceURL var="addPollQuestionURL">
<portlet:param name="ajax" value="deleteChoice"/>
</portlet:resourceURL>

<form method="post" name="<portlet:namespace/>choiceForm" id='<portlet:namespace/>choiceForm'>

<input type="hidden" id="<portlet:namespace/>pollId" name="<portlet:namespace/>pollId" value='<%=poll.getPollId()%>' />
<input type="hidden" id="<portlet:namespace/>choiceContentList" name="<portlet:namespace/>choiceContentList" value="" />
<input type="hidden" id='<portlet:namespace/>questionId' name='<portlet:namespace/>questionId' value="${question.questionId}" />
<input type="hidden" id='<portlet:namespace/>isNew' name='<portlet:namespace/>isNew' value="<%=notNew%>" />

<aui:field-wrapper name="question-status">
		<aui:input inlineLabel="right"  name="question_status" type="radio" value="openAnswerQuestion" label="open-answer-question" onClick="openAnswer();" checked='${question.type == "openAnswerQuestion" ? true : false }' />
		<aui:input  inlineLabel="right" name="question_status"  type="radio" value="selectQuestion" label="select-option-question" onClick="selectQuestion();" checked='${question.type == "selectQuestion" ? true : false }' />
</aui:field-wrapper>

<aui:input name="txtQuestionTitle" label="question-title" value="${question.title}"/>


 <div id="<portlet:namespace/>selectOptionDiv" style="display: none;">
	 <div id="addNewContacts">
			<input type="button" class="btn" value="<liferay-ui:message key='add-choice'/>" onclick="<portlet:namespace/>addQuestionChoices();" <%if(status.equals("disabled")){%> disabled="disabled" <%}%> />
	</div>
	 <div id="pollDynamicDiv">
	 <c:choose>
		<c:when test="<%=notNew%>">
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
																		<c:when test="<%= question.getCreated_date()!=null %>">
																			<input type="button" name='<portlet:namespace/>removeChoicebtn${i.index+1}' style="width: 22px; margin-top:18px;" id='<portlet:namespace/>removeChoicebtn${i.index+1}' value="-" onclick="<portlet:namespace/>ajaxRemoveChoiceDetail('${i.index+1}');" <%if(status.equals("disabled")){%>disabled="disabled" <%}%> />
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
			<c:if test="<%=(choices == null)%>">
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
																<input type="text" name='<portlet:namespace/>includedChoice1' id='<portlet:namespace/>includedChoice1' />
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
			</c:if>
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
															<input type="text" name='<portlet:namespace/>includedChoice1' id='<portlet:namespace/>includedChoice1' />
													</div>
													</td>
													<td>
															<input type="button" name='<portlet:namespace/>removeChoicebtn1' style="display:none; width: 22px; margin-top:18px;" id='<portlet:namespace/>removeChoicebtn1' value="-" onclick="<portlet:namespace/>removeChoicebtn('1');" />
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
</div> 

<aui:button-row>
		<aui:button type="submit" onClick="submitForm();" value="submit"/>
		<aui:button type="cancel" value="cancel" />
</aui:button-row>
</form>

<script type="text/javascript" charset="utf-8">
	
	$(function(){
		 var questionType = $('input[name="<portlet:namespace />question_status"]:checked').val();
		 if(questionType == "selectQuestion"){
			  document.getElementById('<portlet:namespace />selectOptionDiv').style.display = "block";
		  }
		 });
		
		selectQuestion = function(){
			 document.getElementById('<portlet:namespace/>selectOptionDiv').style.display = "block";
		}
	
		openAnswer = function(){
		 document.getElementById('<portlet:namespace/>selectOptionDiv').style.display = "none";
		}
	
		<portlet:namespace/>addQuestionChoices = function(){
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
			
			$("#pollDynamicDiv").append(clonnObject);
			
		}
		
		submitForm = function(){
			$("#<portlet:namespace/>choiceContentList").val($(".choiceList").length);
		  	document.<portlet:namespace/>choiceForm.action ="<%=editPollQuestionURL.toString()%>";
		  	$("#<portlet:namespace/>choiceForm").submit(); 	 
		};
		
		$(document).on('click','.btn-cancel', function(e) {
		    // find the dialog that contains this .closeButton
		    var $dialog = $(this).parents('.ui-dialog-content');
		    $dialog.dialog('close');
		});
		
		<portlet:namespace/>ajaxRemoveChoiceDetail = function(index){
			var choiceRegisterId = $('#choiceList'+index).find('#<portlet:namespace/>choiceRegisterId'+index).val();
			var confirmDelete = confirm("<liferay-ui:message key='confirm-delete-of-add-choice'/>");
			if(!confirmDelete){
				return;
			}
			jQuery.ajax({
				url:"<%=addPollQuestionURL%>",
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
		
		<portlet:namespace/>removeChoicebtn = function(index, confirmDelete){
			if(!confirmDelete){
				confirmDelete = confirm("<liferay-ui:message key='confirm-delete-choice' />");
				if(!confirmDelete){
					return;
				}
			}
			$('#choiceList'+index).remove();
		};
		
</script>
