<%@ include file="init.jsp" %>

<portlet:renderURL var="renderToIframeDemo">
	<portlet:param name="operation" value="iframeDialog"/>
</portlet:renderURL>

<portlet:renderURL var="simpleDialogExample" windowState="<%=LiferayWindowState.EXCLUSIVE.toString()%>">
		<portlet:param name="mvcPath" value="/html/alloycontroller/edit.jsp"/>
		<portlet:param name="message" value="Hello welcome"/>
</portlet:renderURL>

<a href="<portlet:renderURL />">&laquo;Home</a>
<div class="separator"></div>
<div>
	<aui:button name="simple-dialog-example"  id="simple-dialog-example" value="open AUI Dialog" /> 
</div>
<div class="separator"></div>
	<a href="javascript:void(0)" onclick="<%=renderToIframeDemo%>">
  		<liferay-ui:icon image="edit" message="IFrame Dialog" /> go to iframe demo
	</a>
<div class="separator"></div>

<div class="separator"></div>
<aui:script>
AUI().use('aui-base','aui-io-plugin-deprecated','liferay-util-window',function(A) {
	A.one('#<portlet:namespace />simple-dialog-example').on('click', function(event){
		var popUpWindow=Liferay.Util.Window.getWindow(
		{
		dialog: {
			centered: true,
			constrain2view: true,
			//cssClass: 'yourCSSclassName',
			modal: true,
			resizable: false,
			width: 475
			}
		}
		).plug(
		A.Plugin.IO,
		{
		autoLoad: false
		}).render();
		popUpWindow.show();
		popUpWindow.titleNode.html("liferay-6.2-dialog-example");
		popUpWindow.io.set('uri','<%=simpleDialogExample%>');
		popUpWindow.io.start();
		});
		});
</aui:script>