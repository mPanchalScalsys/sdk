package com.application.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;

public class PollManager {

	public String convertAndGroupJSON(List questionWithVote){
		if(questionWithVote == null || questionWithVote.size()==0){
			return "";
		}
		JSONArray groupedJsonArray = JSONFactoryUtil.createJSONArray();
		JSONObject groupedJsonObject = JSONFactoryUtil.createJSONObject();
		
		
		JSONObject jsonObject;
		JSONObject finalJsonObject = JSONFactoryUtil.createJSONObject();
		JSONArray jsonArray2;// = JSONFactoryUtil.createJSONArray();
		
		String prevQuestionId = null;
		String prevChoiceId = null;
		String title = "";
		JSONArray jsonArray3 = JSONFactoryUtil.createJSONArray();
		if(questionWithVote != null){
			Iterator itr =questionWithVote.iterator();
			while(itr.hasNext()){
				Map map = (Map) itr.next();
				//JSONObject jsonObject2 = JSONFactoryUtil.createJSONObject();						
				String voteId = map.get("voteId").toString();
				String questionId = map.get("questionId").toString();
				String choiceId = map.get("choiceId").toString();
				//String userName = map.get("username").toString();
				String voteDesc = map.get("voteDesc").toString();
				Integer choiceCount = Integer.parseInt(map.get("choiceCount").toString());
				
				if(prevQuestionId == null){
					finalJsonObject = createJsonHeader(choiceId);
					
				}else if(!prevQuestionId.equals(questionId)){
					finalJsonObject.put("rows", jsonArray3);
					
					if(prevChoiceId != null && !"0".equals(prevChoiceId)){
						groupedJsonObject.put("qType", "choice");
					}else{
						groupedJsonObject.put("qType", "open");
					}
					groupedJsonObject.put("title", title);
					groupedJsonObject.put("value", finalJsonObject);
					groupedJsonArray.put(groupedJsonObject);
					
					groupedJsonObject = JSONFactoryUtil.createJSONObject();
					finalJsonObject = createJsonHeader(choiceId);
					jsonArray3 = JSONFactoryUtil.createJSONArray();
				}
				
				jsonObject = JSONFactoryUtil.createJSONObject();
				jsonArray2 = JSONFactoryUtil.createJSONArray();
				
				if("0".equals(choiceId)){
					jsonObject.put("v", voteDesc);
				}else{
					jsonObject.put("v", voteDesc);
				}
				
				jsonArray2.put(jsonObject);
				
				if(!"0".equals(choiceId)){
					jsonObject = JSONFactoryUtil.createJSONObject();
					jsonObject.put("v", choiceCount);
					jsonArray2.put(jsonObject);
					
					jsonObject = JSONFactoryUtil.createJSONObject();
					jsonObject.put("v", choiceId);
					jsonArray2.put(jsonObject);
				}
				
				jsonObject = JSONFactoryUtil.createJSONObject();
				jsonObject.put("c", jsonArray2);
				
				jsonArray3.put(jsonObject);
					
				
				prevQuestionId = questionId;
				prevChoiceId = choiceId;
				title = map.get("title").toString();
			}
			
			//jsonObject = JSONFactoryUtil.createJSONObject();
			finalJsonObject.put("rows", jsonArray3);
			
			if(prevChoiceId != null && !"0".equals(prevChoiceId)){
				groupedJsonObject.put("qType", "choice");
			}else{
				groupedJsonObject.put("qType", "open");
			}
			groupedJsonObject.put("title", title);
			groupedJsonObject.put("value", finalJsonObject);
			groupedJsonArray.put(groupedJsonObject);
			
		}
		System.out.println(groupedJsonArray.toString());
		return groupedJsonArray.toString();
	}
	
	private JSONObject createJsonHeader(String choiceId){
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
		
		JSONArray jsonArray2 = JSONFactoryUtil.createJSONArray();
		
		jsonObject.put("id", "");
		if("0".equals(choiceId)){
			jsonObject.put("label", "Answers");
		}else{
			jsonObject.put("label", "Votes");
		}
		jsonObject.put("type", "string");
		jsonArray2.put(jsonObject);
		if(!"0".equals(choiceId)){
			jsonObject = JSONFactoryUtil.createJSONObject();
			jsonObject.put("id", "");
		
			jsonObject.put("label", "No of votes");
		
			jsonObject.put("type", "number");
			jsonArray2.put(jsonObject);
			
			jsonObject = JSONFactoryUtil.createJSONObject();
			jsonObject.put("id", "choiceId");
		
			jsonObject.put("label", "ChoiceId");
		
			jsonObject.put("type", "number");
			jsonArray2.put(jsonObject);
		}
		JSONObject finalJsonObject = JSONFactoryUtil.createJSONObject();
		finalJsonObject.put("cols", jsonArray2);
		
		return finalJsonObject;
	}
	
}
