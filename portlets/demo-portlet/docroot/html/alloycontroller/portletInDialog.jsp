<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/security" prefix="liferay-security" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@page import="com.liferay.portal.kernel.portlet.LiferayWindowState"%>
<portlet:defineObjects />
<liferay-theme:defineObjects />
<a href="<portlet:renderURL />">&laquo;Home</a>
<div class="separator"></div>
<div>
<h4>Iframe Dialog Please click on button  and see </h4><br/>
<aui:button name="open-portlet-dialog-iframe-example"
id="open-portlet-dialog-iframe-example" 
value="Click Here See Ifame Allou UI Dialog">
</aui:button>
</div>
<aui:script>
AUI().use('aui-base',
'aui-io-plugin-deprecated',
'liferay-util-window',
'liferay-portlet-url',
'aui-dialog-iframe-deprecated',
function(A) {
A.one('#<portlet:namespace />open-portlet-dialog-iframe-example').on('click',
function(event){
var url =Liferay.PortletURL.createRenderURL();
url.setPortletId("58")  //58 is sign in portlet ID
url.setWindowState('pop_up');
var popUpWindow=Liferay.Util.Window.getWindow(
{
dialog: {
centered: true,
constrain2view: true,
//cssClass: 'yourCSSclassName',
modal: true,
resizable: false,
width: 500
}
}
).plug(
A.Plugin.DialogIframe,
{
autoLoad: false,
iframeCssClass: 'dialog-iframe',
uri:url.toString()
}).render();
popUpWindow.show();
popUpWindow.titleNode.html("Liferay 6.2 Open Portlet in Iframe Dialog Window");
popUpWindow.io.start();

});
});
</aui:script>