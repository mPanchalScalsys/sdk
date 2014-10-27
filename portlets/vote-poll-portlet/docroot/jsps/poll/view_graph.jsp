<%@ include file="init.jsp" %>
<%
List<Object> pollUserVote = (List)request.getAttribute("questionWithVote");

%>

<portlet:renderURL var="renderBackURL"></portlet:renderURL>
<aui:button value="back" onClick="<%=renderBackURL%>"></aui:button>
<h4>Display PollID : ${pollId}</h4>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<div id="piechartByPoll" style="width: 300; height: 500px;"></div>
<script type="text/javascript">
<!--
	google.load("visualization", "1", {packages:["corechart"]});
	google.setOnLoadCallback(drawChart);
	function drawChart() {
	
	  var data = google.visualization.arrayToDataTable([
		['Title', 'Number of Questions', 'QuestionId'],
		<%
		for(int i=0; i< pollUserVote.size();i++){
			Object [] o1 = (Object [])pollUserVote.get(i);
			if(i==pollUserVote.size()-1) {
		%>
		['<%=o1[1]%>', <%=o1[2]%>,<%=o1[0]%>]
		<%		
			} else {
		%>		
		['<%=o1[1]%>', <%=o1[2]%>,<%=o1[0]%>],
		<%
			}
		}
		%>
	    ]);
	
	  var options = {
	    title: 'Status of Vote on Poll'
	  };
	  var chart = new google.visualization.PieChart(document.getElementById('piechartByPoll'));
	  
	  google.visualization.events.addListener(chart, 'select', selectHandler);

	  function selectHandler() {
		  var selectedItem = chart.getSelection()[0];
          if (selectedItem) {
            var topping = data.getValue(selectedItem.row, 2);
			/* dialogBox(topping); */
			//$("#dialog").dialog('open');
          }
	  }
	  chart.draw(data, options);
	 }
	</script>