package com.mcds.app.android.estar.provider;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import com.mcds.app.android.estar.bean.BaseReturnResult;
import com.mcds.app.android.estar.bean.GetContactsGroupChildsInfo;
import com.mcds.app.android.estar.bean.GetContactsGroupInfo;
import com.mcds.app.android.estar.bean.GoodWeibo;
import com.mcds.app.android.estar.bean.AllContactList;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.WeiboCommit;
import com.mcds.app.android.estar.bean.WeiboGetMatters;
import com.mcds.app.android.estar.bean.WeiboSave;
import com.mcds.app.android.estar.bean.WeiboShare;
import com.mcds.app.android.estar.bean.WeiboWrite;
import com.mcds.app.android.estar.util.ConnectUtility;
import com.mcds.app.android.estar.util.JsonUtility;

public class ConnectProviderGC {
	
	
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
	
	public static ReturnResult<WeiboGetMatters> getWeiboItems(String uid, String weibo_sort) {
		ReturnResult<WeiboGetMatters> rr = new ReturnResult<WeiboGetMatters>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		
		params.put("uid", uid);
		params.put("weibo_sort", weibo_sort);
		
		try {
			String returnJson = ConnectUtility.connect("getWeiboItems", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			
			if (rr.status.equals("0")) {
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null) {
					for (int i = 0; i < ja.length(); i++) {
						WeiboGetMatters item = new WeiboGetMatters();
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
	
	public static ReturnResult<GoodWeibo> goodWeibo(String uid, String weibo_id) {
		ReturnResult<GoodWeibo> rr = new ReturnResult<GoodWeibo>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		
		params.put("uid", uid);
		params.put("weibo_id", weibo_id);
		
		try {
			String returnJson = ConnectUtility.connect("goodWeibo", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			
			if (rr.status.equals("0")) {
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null) {
					for (int i = 0; i < ja.length(); i++) {
						GoodWeibo item = new GoodWeibo();
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
	
	public static ReturnResult<WeiboShare> shareWeibo(String uid, String weibo_id, String content_text, String original_weibo_id) {
		ReturnResult<WeiboShare> rr = new ReturnResult<WeiboShare>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		
		params.put("uid", uid);
		params.put("weibo_id", weibo_id);
		params.put("content_text", content_text);
		params.put("original_weibo_id", original_weibo_id);
		
		try {
			String returnJson = ConnectUtility.connect("shareWeibo", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			
			if (rr.status.equals("0")) {
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null) {
					for (int i = 0; i < ja.length(); i++) {
						WeiboShare item = new WeiboShare();
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
	
	public static ReturnResult<WeiboCommit> commitWeibo(String uid, String weibo_id, String content) {
		ReturnResult<WeiboCommit> rr = new ReturnResult<WeiboCommit>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		
		params.put("uid", uid);
		params.put("weibo_id", weibo_id);
		params.put("content", content);
		
		try {
			String returnJson = ConnectUtility.connect("commitWeibo", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			
			if (rr.status.equals("0")) {
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null) {
					for (int i = 0; i < ja.length(); i++) {
						WeiboCommit item = new WeiboCommit();
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
	
	public static ReturnResult<WeiboSave> SaveWeibo(String uid, String weibo_id) {
		ReturnResult<WeiboSave> rr = new ReturnResult<WeiboSave>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		
		params.put("uid", uid);
		params.put("weibo_id", weibo_id);
		
		try {
			String returnJson = ConnectUtility.connect("SaveWeibo", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			
			if (rr.status.equals("0")) {
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null) {
					for (int i = 0; i < ja.length(); i++) {
						WeiboSave item = new WeiboSave();
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
	
	public static ReturnResult<WeiboWrite> writeWeibo(String uid, String content_text, String weibo_sort, List<String> content_img,String model_id) {
		ReturnResult<WeiboWrite> rr = new ReturnResult<WeiboWrite>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		
		params.put("uid", uid);
		params.put("content_text", content_text);
		params.put("weibo_sort", weibo_sort);
		params.put("bt_id", model_id);
		
		JSONArray imgJa = new JSONArray();
		for (int i = 0; i < content_img.size(); i++) {
			imgJa.put(content_img.get(i));
		}
		params.put("content_img", imgJa);
		
		try {
			String returnJson = ConnectUtility.connect("writeWeibo", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			
			if (rr.status.equals("0")) {
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null) {
					for (int i = 0; i < ja.length(); i++) {
						WeiboWrite item = new WeiboWrite();
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
	
	public static ReturnResult<GetContactsGroupInfo> getMyGroupList(String uid) {
		ReturnResult<GetContactsGroupInfo> rr = new ReturnResult<GetContactsGroupInfo>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		
		params.put("uid", uid);
		
		try {
			String returnJson = ConnectUtility.connect("getMyGroupList", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			
			if (rr.status.equals("0")) {
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null) {
					for (int i = 0; i < ja.length(); i++) {
						GetContactsGroupInfo item = new GetContactsGroupInfo();
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
	
	public static ReturnResult<AllContactList> getAllWeiboersList(String uid, String page_num, String page_size) {
		ReturnResult<AllContactList> rr = new ReturnResult<AllContactList>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		
		params.put("uid", uid);
		params.put("page_num", page_num);
		params.put("page_size", page_size);
		
		try {
			String returnJson = ConnectUtility.connect("getMyAttentionList", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			
			if (rr.status.equals("0")) {
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null) {
					for (int i = 0; i < ja.length(); i++) {
						AllContactList item = new AllContactList();
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
	
	public static ReturnResult<GetContactsGroupChildsInfo> getGroupMembers(String uid, String group_id) {
		ReturnResult<GetContactsGroupChildsInfo> rr = new ReturnResult<GetContactsGroupChildsInfo>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		
		params.put("uid", uid);
		params.put("group_id", group_id);
		
		try {
			String returnJson = ConnectUtility.connect("getGroupMembers", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			
			if (rr.status.equals("0")) {
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null) {
					for (int i = 0; i < ja.length(); i++) {
						GetContactsGroupChildsInfo item = new GetContactsGroupChildsInfo();
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
}
