<%@ include file="init.jsp" %>
<portlet:resourceURL var="viewUserURL">
<portlet:param name="ajax" value="getUser"/>
</portlet:resourceURL>
<portlet:resourceURL var="coEfficentURL">
<portlet:param name="ajax" value="coEfficient"/>
</portlet:resourceURL>

<%
UserVoteDao userVoteDao = ApplicationContextHolder.getContext().getBean(UserVoteDao.class);
List<Object> userVote = (List)request.getAttribute("userVote");
List<Object> userCoEfficiencyVote = (List)request.getAttribute("userCoEfficiencyVote");
%>
<portlet:renderURL var="renderBackURL"></portlet:renderURL>
<aui:button value="back" onClick="<%=renderBackURL%>"></aui:button>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<div style="width: 100%; clear:both;">
<div id="piechart" style="width: 49.5%; border-right:1px solid #d3d3d3; float:left; "></div>
<div id="piechart2" style="width: 49.5%; float:left;  "></div>
</div>
<div style="clear:both;"></div>
<script type="text/javascript">
<!--
	google.load("visualization", "1", {packages:["corechart"]});
	google.setOnLoadCallback(drawChart);
	function drawChart() {
	
	  var data = google.visualization.arrayToDataTable([
		['Options', 'Number of Votes', 'ChoiceID'],
		<%
		for(int i=0; i< userVote.size();i++){
			Object [] o1 = (Object [])userVote.get(i);
			if(i==userVote.size()-1) {
		%>
		['<%=o1[1]%>', <%=o1[0]%>,<%=o1[2]%>]
		<%		
			} else {
		%>		
		['<%=o1[1]%>', <%=o1[0]%>,<%=o1[2]%>],
		<%
			}
		}
		%>
	    ]);
	
	  var options = {
	    title: 'Vote By People'
	  };
	  var chart = new google.visualization.PieChart(document.getElementById('piechart'));
	  
	  google.visualization.events.addListener(chart, 'select', selectHandler);

	  function selectHandler() {
		  var selectedItem = chart.getSelection()[0];
          if (selectedItem) {
            var topping = data.getValue(selectedItem.row, 2);
            
			dialogBox(topping);
			//$("#dialog").dialog('open');
          }
	  }
	  chart.draw(data, options);
	  
	  //  CoEfficiency Graph
	  var data2 = google.visualization.arrayToDataTable([
		['Options', 'Number of Votes', 'ChoiceID'],
		<%
		for(int i=0; i< userCoEfficiencyVote.size();i++){
			Object [] o1 = (Object [])userCoEfficiencyVote.get(i);
			if(i==userCoEfficiencyVote.size()-1) {
		%>
		['<%=o1[3]%>', <%=o1[1]%>,<%=o1[0]%>]
		<%		
			} else {
		%>		
		['<%=o1[3]%>', <%=o1[1]%>,<%=o1[0]%>],
		<%
			}
		}
		%>
	    ]);
	  
	  var options2 = {
			    title: 'Vote By People with coefficency'
			  };
			 
	  var chart2 = new google.visualization.PieChart(document.getElementById('piechart2'));
			  
			  google.visualization.events.addListener(chart2, 'select', selectHandler2);

			  function selectHandler2() {
				  var selectedItem = chart2.getSelection()[0];
		          if (selectedItem) {
		            var topping = data2.getValue(selectedItem.row, 2);
		            
					dialogBox2(topping);
					//$("#dialog").dialog('open');
		          }
			  }
			  chart2.draw(data2, options2);

	}
	
	
	
	
	function dialogBox(toppingValue) {
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
	  }
	function dialogBox2(toppingValue) {
		var url = "<%=coEfficentURL.toString()%>&choiceId="+toppingValue;
		var mytitle = "User Information By CoEfficent";
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
	  }
//-->

</script>

<div id="dialog"></div>