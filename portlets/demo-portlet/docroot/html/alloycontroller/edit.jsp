<%@page import="javax.portlet.WindowState"%>
<%@ include file="init.jsp" %>

<%@page import="javax.portlet.ActionRequest"%>

<portlet:actionURL var="editArticleActionURL">
</portlet:actionURL>
<h5>Autofield Example</h5>
<aui:form action="<%=editArticleActionURL%>" method="post" name="LiferayAutoFieldForm">
  <div id="phone-fields">
    <div class="lfr-form-row lfr-form-row-inline">
      <div class="row-fields">
        <aui:input fieldParam='phoneNumber0' id='phoneNumber0' name="phoneNumber0" label="Phone Number" />
        <aui:select id="phoneTypeId0" name="phoneTypeId0" label="Type">
          <aui:option value="11006" label="Business"></aui:option>
          <aui:option value="11007" label="Business Fax"></aui:option>
          <aui:option value="11008" label="Mobile Phone"></aui:option>
          <aui:option value="11009" label="Other"></aui:option>
          <aui:option value="11011" label="Personal"></aui:option>
        </aui:select>
      </div>
    </div>
  </div>
  <aui:layout>
    <aui:column>
      <aui:button type="submit" value="Save Phone Numbers" name="SavePhoneNumbers"
        onClick="saveData();"></aui:button>
    </aui:column>
  </aui:layout>
  <aui:script use="liferay-auto-fields">
    new Liferay.AutoFields(
    {
    contentBox: '#phone-fields',
    fieldIndexes: '<portlet:namespace />phonesIndexes'
    }
    ).render();
  </aui:script>
</aui:form>