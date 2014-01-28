package com.mcds.app.android.estar.provider;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import com.mcds.app.android.estar.bean.BaseReturnResult;
import com.mcds.app.android.estar.bean.MyFans;
import com.mcds.app.android.estar.bean.My_FindFriends;
import com.mcds.app.android.estar.bean.My_IntegrationScoreList;
import com.mcds.app.android.estar.bean.My_SetHeadPic;
import com.mcds.app.android.estar.bean.My_SetIntro;
import com.mcds.app.android.estar.bean.My_SystemMessage;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.util.ConnectUtility;
import com.mcds.app.android.estar.util.JsonUtility;

public class ConnectProviderCL {
	
	/**
	 * 设置心情
	 */
	public static ReturnResult<My_SetIntro> setIntro(String uid, String intro)
	{
		ReturnResult<My_SetIntro> rr = new ReturnResult<My_SetIntro>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("intro", intro);
		
		try
		{
			String returnJson = ConnectUtility.connect("setIntro", params);

			JSONObject jo = new JSONObject(returnJson);

			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");

			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");

				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						My_SetIntro item = new My_SetIntro();
						if (item.parseJson(ja.getJSONObject(i)))
						{
							rr.addData(item);
						}
					}
				}
			}
		} catch (Exception e)
		{
			setException(e, rr);
		}

		return rr;
	}
	
	/**
	 * 上传图片
	 */
	public static ReturnResult<My_SetHeadPic> setUserImage(String uid, String user_img)
	{
		ReturnResult<My_SetHeadPic> rr = new ReturnResult<My_SetHeadPic>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("user_img", user_img);
		
		try
		{
			String returnJson = ConnectUtility.connect("setUserImage", params);

			JSONObject jo = new JSONObject(returnJson);

			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");

			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");

				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						My_SetHeadPic item = new My_SetHeadPic();
						if (item.parseJson(ja.getJSONObject(i)))
						{
							rr.addData(item);
						}
					}
				}
			}
		} catch (Exception e)
		{
			setException(e, rr);
		}

		return rr;
	}
	/**
	 * 找朋友
	 * @param uid
	 * @param tpye
	 * @param page_num
	 * @param page_size
	 * @return
	 */
	public static ReturnResult<My_FindFriends> searchFriendBySystem(String uid, String type,String page_num,String page_size)
	{
		ReturnResult<My_FindFriends> rr = new ReturnResult<My_FindFriends>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("type", type);
		params.put("page_num", page_num);
		params.put("page_size", page_size);
		
		try
		{
			String returnJson = ConnectUtility.connect("searchFriendBySystem", params);

			JSONObject jo = new JSONObject(returnJson);

			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");

			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");

				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						My_FindFriends item = new My_FindFriends();
						if (item.parseJson(ja.getJSONObject(i)))
						{
							rr.addData(item);
						}
					}
				}
			}
		} catch (Exception e)
		{
			setException(e, rr);
		}

		return rr;
	}

	/**
	 * 获取积分列表
	 * 
	 */
	public static ReturnResult<My_IntegrationScoreList> getScoreList(String uid, String page_num,String page_size)
	{
		ReturnResult<My_IntegrationScoreList> rr = new ReturnResult<My_IntegrationScoreList>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("page_num", page_num);
		params.put("page_size", page_size);

		try
		{
			String returnJson = ConnectUtility.connect("getScoreList", params);

			JSONObject jo = new JSONObject(returnJson);

			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");

			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if(ja!=null&&ja.length()>0){
					
				JSONObject dataJo = ja.getJSONObject(0);
				if (dataJo != null){
				My_IntegrationScoreList.my_score_totle = JsonUtility.getString(dataJo, "totle", "");
				My_IntegrationScoreList.my_score_access = JsonUtility.getString(dataJo, "access", "");
				My_IntegrationScoreList.my_score_expend = JsonUtility.getString(dataJo, "expend", "");
					JSONArray jArray= JsonUtility.getJSONArray(dataJo, "items");
					for (int i = 0; i < jArray.length(); i++)
					{
						My_IntegrationScoreList item = new My_IntegrationScoreList();
						if (item.parseJson(jArray.getJSONObject(i)))
						{
							rr.addData(item);
						}
					}
				}
			}
			}
		} catch (Exception e)
		{
			setException(e, rr);
		}
		return rr;
	}
	
	/**
	 * 获取系统消息
	 * 
	 */
	public static ReturnResult<My_SystemMessage> getSystemMessageList(String uid, String type, String page_num,String page_size) {
		ReturnResult<My_SystemMessage> rr = new ReturnResult<My_SystemMessage>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("type", type);
		params.put("page_num", page_num);
		params.put("page_size", page_size);
		try {
			String returnJson = ConnectUtility.connect("getSystemMessageList", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2"); 
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0")) {
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null) {
					for (int i = 0; i < ja.length(); i++) {
						My_SystemMessage item = new My_SystemMessage();
						if (item.parseJson(ja.getJSONObject(i))) {
							rr.addData(item);
							
						}
					}
				}
			}
		} catch (Exception e) {
			setException(e, rr);
		}
		return rr;
	}
	
	private static void setException(Exception exception, BaseReturnResult rr) {
		try {
			throw exception;
		} catch (JSONException jsonE) {
			rr.status = "-2";
			rr.info = jsonE.getMessage();
		} catch (SocketTimeoutException socketTimeoutE) {
			rr.status = "-3";
			rr.info = "连接超时.";
		} catch (HttpResponseException httpResponseE) {
			rr.status = "-4";
			rr.info = "服务器无反应.";
		} catch (UnknownHostException unknownHostE) {
			rr.status = "-5";
			rr.info = "未知服务器.";
		} catch (Exception e) {
			rr.status = "-1";
			rr.info = e.getMessage();
		}
	}
}
