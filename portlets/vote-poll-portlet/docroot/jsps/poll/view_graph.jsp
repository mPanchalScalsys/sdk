<%@ include file="init.jsp" %>

<portlet:renderURL var="renderBackURL"></portlet:renderURL>
<aui:button value="back" onClick="<%=renderBackURL%>"></aui:button>
<portlet:resourceURL var="viewUserURL">
<portlet:param name="ajax" value="getUser"/>
</portlet:resourceURL>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
	
<div id="piechartByPoll" style="width: 300; height: 500px;"></div>
<script type="text/javascript">
	google.load('visualization', '1.0', {'packages':['corechart']});
	google.load('visualization', '1.0', {'packages':['table']});
	google.setOnLoadCallback(drawChart);
	function drawChart() {
		
		//console.log('${questionWithVote}');
		var listData = $.parseJSON('${questionWithVote}');
		$.each(listData, function(i, item) {
		   // alert('Question Type ' + item.qType);
		    var data = new google.visualization.DataTable(item.value);
		    
		    var options = {
		    	    'title': 'Status of Vote on Poll'
		    };
		    var titleDiv = "<div id='title"+i+"'><h4>Title: "+item.title+"</h4></div>";
		    $('#piechartByPoll').append(titleDiv);
		    var div = "<div id='graph"+i+"'/>";
		    $('#piechartByPoll').append(div);
		    
		    if(item.qType == 'choice'){
		    	
		    	var chart = new google.visualization.PieChart(document.getElementById('graph'+i));
		    }else{
		    	var chart = new google.visualization.Table(document.getElementById('graph'+i));
		    }
		    
		   /*  google.visualization.events.addListener(chart, 'select', selectHandler); */

			 /*  function selectHandler() {
				  var selectedItem = chart.getSelection()[0];
		          if (selectedItem) {
		            var topping = data.getValue(selectedItem.row, 2);
		            
					dialogBox(topping);
					//$("#dialog").dialog('open');
		          }
			  } */
		    
		    chart.draw(data, options);
		});
	 }
	
<%-- 	function dialogBox(toppingValue) {
		var url = "<%=viewUserURL.toString()%>&choiceId="+toppingValue; 
		var mytitle = "User Information";
		$("#dialog").load(url,null,function() {
			$("#dialog").dialog({
				title: mytitle,
				modal: true,
				width: 450,
				close: function(event, ui) {
				$("#dialog").empty(); // remove the content
				} //END CLOSE
			});//END DIALOG
		});//END DIALOG
	  } --%>
</script>

<div id="dialog"></div>
	