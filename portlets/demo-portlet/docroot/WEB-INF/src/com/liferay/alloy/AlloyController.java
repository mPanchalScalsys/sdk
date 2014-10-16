package com.liferay.alloy;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class AlloyController
 */
public class AlloyController extends MVCPortlet {
	public void doView(RenderRequest renderRequest,RenderResponse renderResponse) throws IOException, PortletException{
		String renderParameter = ParamUtil.getString(renderRequest, "operation",StringPool.BLANK);
		if("edit".equals(renderParameter)){
			include("/html/alloycontroller/edit.jsp", renderRequest, renderResponse);	
		}else if("iframeDialog".equals(renderParameter)){
			include("/html/alloycontroller/portletInDialog.jsp", renderRequest, renderResponse);
		}
		else{
			include("/html/alloycontroller/edit.jsp", renderRequest, renderResponse);
		}	
	}
	public void editArticleActionURL(ActionRequest actionRequest,ActionResponse actionResponse) throws Exception {
		String phonesIndexesString = actionRequest.getParameter("phonesIndexes");
        _log.info("phonesIndexesString=="+phonesIndexesString);
         int[] phonesIndexes = StringUtil.split(phonesIndexesString, 0);
         for (int phonesIndex : phonesIndexes) {
        	 String number = ParamUtil.getString(actionRequest, "phoneNumber" + phonesIndex);
        	 _log.info("phoneNumber: "+number);
             int typeId = ParamUtil.getInteger(actionRequest, "phoneTypeId" + phonesIndex);
             _log.info("typeId: "+typeId);
         }

	}
	
	private static Log _log = LogFactoryUtil.getLog(AlloyController.class); 
}
