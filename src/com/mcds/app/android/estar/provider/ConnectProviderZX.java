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

import com.mcds.app.android.estar.bean.Action;
import com.mcds.app.android.estar.bean.ActionDetail;
import com.mcds.app.android.estar.bean.ActionQuestionairParam;
import com.mcds.app.android.estar.bean.ActionQuestionairResult;
import com.mcds.app.android.estar.bean.ActivitCommitList;
import com.mcds.app.android.estar.bean.ActivityHelpReply;
import com.mcds.app.android.estar.bean.ActivityTest;
import com.mcds.app.android.estar.bean.BaseReturnResult;
import com.mcds.app.android.estar.bean.BirthdayList;
import com.mcds.app.android.estar.bean.HeartHomeList;
import com.mcds.app.android.estar.bean.HonorsList;
import com.mcds.app.android.estar.bean.NewsList;
import com.mcds.app.android.estar.bean.NewsListDetail;
import com.mcds.app.android.estar.bean.NewsTestContent;
import com.mcds.app.android.estar.bean.NewsTestResult;
import com.mcds.app.android.estar.bean.PhotoList;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.TestList;
import com.mcds.app.android.estar.bean.WeiboWrite;
import com.mcds.app.android.estar.util.ConnectUtility;
import com.mcds.app.android.estar.util.JsonUtility;

public class ConnectProviderZX {

	
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
	
	/**
	 * 获取心灵家园
	 * 
	 * @param uid
	 * @param type
	 *            活动类型
	 * @param activity_sort
	 *            活动类别
	 * @param activity_status
	 *            活动状态
	 * @param activity_award
	 *            活动奖励
	 * @return
	 */
	public static ReturnResult<HeartHomeList> getHeartHomeList(String sort)
	{
		ReturnResult<HeartHomeList> rr = new ReturnResult<HeartHomeList>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("sort", sort);

		try
		{
			String returnJson = ConnectUtility.connect("getHeartHomeList", params);
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
						HeartHomeList item = new HeartHomeList();
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
	 * 获取活动列表
	 * 
	 * @param uid
	 * @param type
	 *            活动类型
	 * @param activity_sort
	 *            活动类别
	 * @param activity_status
	 *            活动状态
	 * @param activity_award
	 *            活动奖励
	 * @return
	 */
	public static ReturnResult<Action> getActivityList(String uid, String type, String activity_sort, String activity_status,
			String activity_award, String page_size, String page_num)
	{
		ReturnResult<Action> rr = new ReturnResult<Action>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("type", type);
		params.put("activity_sort", activity_sort);
		params.put("activity_status", activity_status);
		params.put("activity_award", activity_award);
		params.put("page_num", page_num);
		params.put("page_size", page_size);

		try
		{
			String returnJson = ConnectUtility.connect("getActivityList", params);
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
						Action item = new Action();
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
	 * 获取活动详细
	 * 
	 * @param uid
	 *            活动ID
	 * @return -2，活动不存在
	 */
	public static ReturnResult<ActionDetail> getActivityDetail(String activity_id)
	{
		ReturnResult<ActionDetail> rr = new ReturnResult<ActionDetail>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("activity_id", activity_id);

		try
		{
			String returnJson = ConnectUtility.connect("getActivityDetail", params);
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
						ActionDetail item = new ActionDetail();
						if (item.parseJson(ja.getJSONObject(i)))
						{
							rr.addData(item);
						}
					}
				}
			}
		} catch (Exception je)
		{
			setException(je, rr);
		}

		return rr;
	}
	
	/**
	 * 获取照片列表
	 * 
	 * @param uid
	 *            活动ID
	 * @return -2，活动不存在
	 */
	public static ReturnResult<PhotoList> getActivityPhotos(String activity_id)
	{
		ReturnResult<PhotoList> rr = new ReturnResult<PhotoList>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("activity_id", activity_id);
//		8fadf1c5-83dc-11e3-b77f-5254001763be
//		params.put("activity_id", "8fadf1c5-83dc-11e3-b77f-5254001763be");

		try
		{
			String returnJson = ConnectUtility.connect("getActivityPhotos", params);
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
						PhotoList item = new PhotoList();
						if (item.parseJson(ja.getJSONObject(i)))
						{
							rr.addData(item);
						}
					}
				}
			}
		} catch (Exception je)
		{
			setException(je, rr);
		}

		return rr;
	}

//	upActivityPhoto
	
	
	/**
	 * 获取活动评论
	 * 
	 * @param activity_id
	 * @param page_num
	 * @param page_size
	 * @return
	 */
	public static ReturnResult<ActivitCommitList> getActivitCommitList(String activity_id, String page_num, String page_size)
	{
		ReturnResult<ActivitCommitList> rr = new ReturnResult<ActivitCommitList>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("activity_id", activity_id);
		params.put("page_num", page_num);
		params.put("page_size", page_size);
//		params.put("activity_id", "d5c89590-824a-11e3-971f-5254001763be");

		try
		{
			String returnJson = ConnectUtility.connect("getActivitCommitList", params);

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
						ActivitCommitList item = new ActivitCommitList();
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
	 * 获取求助活动回复
	 * 
	 * @param activity_id
	 * @param page_num
	 * @param page_size
	 * @return
	 */
	public static ReturnResult<ActivityHelpReply> getActivityHelpReply(String activity_id, String page_num, String page_size)
	{
		ReturnResult<ActivityHelpReply> rr = new ReturnResult<ActivityHelpReply>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("activity_id", activity_id);
		params.put("page_num", page_num);
		params.put("page_size", page_size);

		try
		{
			String returnJson = ConnectUtility.connect("getActivityHelpReply", params);

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
						ActivityHelpReply item = new ActivityHelpReply();
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

//	intrestActivity
	public static ReturnResult<String> intrestActivity(String uid, String activity_id)
	{
		ReturnResult<String> rr = new ReturnResult<String>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("activity_id", activity_id);

		try
		{
			String returnJson = ConnectUtility.connect("intrestActivity", params);

			JSONObject jo = new JSONObject(returnJson);

			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");

		} catch (Exception e)
		{
			setException(e, rr);
		}

		return rr;
	}
	
	/**
	 * 上传图片
	 * @param uid
	 * @param activity_id
	 * @param photo_data
	 * @return
	 */
	public static ReturnResult<String> upActivityPhoto(String uid, String activity_id, String photo_data)
	{
		ReturnResult<String> rr = new ReturnResult<String>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("activity_id", activity_id);
		params.put("photo_data", photo_data);

		try
		{
			String returnJson = ConnectUtility.connect("upActivityPhoto", params);

			JSONObject jo = new JSONObject(returnJson);

			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");

		} catch (Exception e)
		{
			setException(e, rr);
		}

		return rr;
	}
	
	/**
	 * 参与活动
	 * 
	 * @param activity_id
	 * @param uid
	 * @return 是否参与成功直接从返回的rr的status判断
	 */
	public static ReturnResult<String> joinTheActivity(String activity_id, String uid)
	{
		ReturnResult<String> rr = new ReturnResult<String>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("activity_id", activity_id);
		params.put("uid", uid);

		try
		{
			String returnJson = ConnectUtility.connect("joinTheActivity", params);

			JSONObject jo = new JSONObject(returnJson);

			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");

		} catch (Exception e)
		{
			setException(e, rr);
		}

		return rr;
	}
	
	/**
	 * 获取测试结果
	 * @param uid
	 * @param activity_id
	 * @param question_result
	 * @return
	 */
	public static ReturnResult<ActionQuestionairResult> getActivityTestResult(String uid, String activity_id, List<ActionQuestionairParam> question_result) {
		ReturnResult<ActionQuestionairResult> rr = new ReturnResult<ActionQuestionairResult>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		
		params.put("uid", uid);
		params.put("activity_id", activity_id);
//		params.put("question_result", question_result);
		
		JSONArray quesJa = new JSONArray();
		for (int i = 0; i < question_result.size(); i++) {
			quesJa.put(question_result.get(i));
		}
		params.put("question_result", quesJa);
		
		try {
			String returnJson = ConnectUtility.connect("getActivityTestResult", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			
			if (rr.status.equals("0")) {
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null) {
					for (int i = 0; i < ja.length(); i++) {
						ActionQuestionairResult item = new ActionQuestionairResult();
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
	
	public static ReturnResult<String> commentActivity(String activity_id, String uid, String content)
	{
		ReturnResult<String> rr = new ReturnResult<String>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("activity_id", activity_id);
		params.put("uid", uid);
		params.put("content", content);

		try
		{
			String returnJson = ConnectUtility.connect("commentActivity", params);

			JSONObject jo = new JSONObject(returnJson);

			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");

		} catch (Exception e)
		{
			setException(e, rr);
		}

		return rr;
	}
	
	
	public static ReturnResult<String> goodNews(String uid, String news_id)
	{
		ReturnResult<String> rr = new ReturnResult<String>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
//		params.put("news_id", news_id);
		
		params.put("news_id", "9908bb17-8700-11e3-abab-5254001763be");

		try
		{
			String returnJson = ConnectUtility.connect("goodNews", params);

			JSONObject jo = new JSONObject(returnJson);

			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");

		} catch (Exception e)
		{
			setException(e, rr);
		}

		return rr;
	}

	/**
	 * 回复求助活动
	 * 
	 * @param activity_id
	 * @param uid
	 * @param content
	 * @return 是否回复成功直接从返回的rr的status判断
	 */
	public static ReturnResult<String> replyActivityHelp(String activity_id, String uid, String content)
	{
		ReturnResult<String> rr = new ReturnResult<String>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("activity_id", activity_id);
		params.put("uid", uid);
		params.put("content", content);

		try
		{
			String returnJson = ConnectUtility.connect("replyActivityHelp", params);

			JSONObject jo = new JSONObject(returnJson);

			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");

		} catch (Exception e)
		{
			setException(e, rr);
		}

		return rr;
	}

	/**
	 * 采纳该回复
	 * 
	 * @param activity_id
	 * @param uid
	 * @param reply_id
	 * @return 是否采纳成功直接从返回的rr的status判断
	 */
	public static ReturnResult<String> acceptTheActivityHelpReply(String activity_id, String uid, String reply_id)
	{
		ReturnResult<String> rr = new ReturnResult<String>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("activity_id", activity_id);
		params.put("uid", uid);
		params.put("reply_id", reply_id);

		try
		{
			String returnJson = ConnectUtility.connect("acceptTheActivityHelpReply", params);

			JSONObject jo = new JSONObject(returnJson);

			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");

		} catch (Exception e)
		{
			setException(e, rr);
		}

		return rr;
	}
	
	public static ReturnResult<ActivityTest> getActivityTest(String uid, String activity_id)
	{
		ReturnResult<ActivityTest> rr = new ReturnResult<ActivityTest>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
//		params.put("activity_id", activity_id);0d2d9bb6-8014-11e3-971f-5254001763be
//		params.put("activity_id", "0d2d9bb6-8014-11e3-971f-5254001763be");
		params.put("activity_id", activity_id);

		try
		{
			String returnJson = ConnectUtility.connect("getActivityTest", params);
			
			
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
						ActivityTest item = new ActivityTest();
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
	 * 心灵家园获取测试内容
	 * @param test_id
	 * @return
	 */
	public static ReturnResult<NewsTestContent> getTestContent(String test_id)
	{
		ReturnResult<NewsTestContent> rr = new ReturnResult<NewsTestContent>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("test_id", test_id);

		try
		{
			String returnJson = ConnectUtility.connect("getTestContent", params);
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
						NewsTestContent item = new NewsTestContent();
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
	 * 获取新闻资讯
	 * 
	 * @param uid
	 * @return
	 */
	public static ReturnResult<NewsList> getCompanyNewsList(String uid, String page_num, String page_size)
	{
		ReturnResult<NewsList> rr = new ReturnResult<NewsList>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("page_num", "0");
		params.put("page_size", "5");

		try
		{
			String returnJson = ConnectUtility.connect("getCompanyNewsList", params);
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
						NewsList item = new NewsList();
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
	 * 获取新闻资讯详细
	 * 
	 * @param uid
	 * @param news_id
	 * @return
	 */
	public static ReturnResult<NewsListDetail> getNewsDetailed(String uid, String news_id)
	{
		ReturnResult<NewsListDetail> rr = new ReturnResult<NewsListDetail>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("news_id", news_id);

		try
		{
			String returnJson = ConnectUtility.connect("getNewsDetailed", params);
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
						NewsListDetail item = new NewsListDetail();
						if (item.parseJson(ja.getJSONObject(i)))
						{
							rr.addData(item);
						}
					}
				}
			}
		} catch (Exception je)
		{
			setException(je, rr);
		}

		return rr;
	}

	public static ReturnResult<NewsTestResult> getTestResult(String uid, String test_id, String totle_score)
	{
		ReturnResult<NewsTestResult> rr = new ReturnResult<NewsTestResult>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("test_id", test_id);
		params.put("totle_score", totle_score);

		try
		{
			String returnJson = ConnectUtility.connect("getTestResult", params);

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
						NewsTestResult item = new NewsTestResult();
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
	 * 获取荣誉列表
	 * 
	 * @param uid
	 * @return
	 */
	public static ReturnResult<HonorsList> getHonorsList(String uid)
	{
		ReturnResult<HonorsList> rr = new ReturnResult<HonorsList>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
//		params.put("page_num", "0");
//		params.put("page_size", "5");

		try
		{
			String returnJson = ConnectUtility.connect("getHonorsList", params);

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
						HonorsList item = new HonorsList();
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
	 * 获取生日列表
	 * 
	 * @param uid
	 * @return
	 */
	public static ReturnResult<BirthdayList> getBirthdayList(String uid)
	{
		ReturnResult<BirthdayList> rr = new ReturnResult<BirthdayList>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
//		params.put("page_num", "0");
//		params.put("page_size", "5");

		try
		{
			String returnJson = ConnectUtility.connect("getBirthdayList", params);

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
						BirthdayList item = new BirthdayList();
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
	 * 获取测试列表
	 * 
	 * @param uid
	 * @param test_classification
	 * @return
	 */
	public static ReturnResult<TestList> getTestList(String uid, String test_classification)
	{
		ReturnResult<TestList> rr = new ReturnResult<TestList>();

		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("page_size", "5");
		params.put("test_classification", test_classification);
//		params.put("test_classification", "5");
		
		try
		{
			String returnJson = ConnectUtility.connect("getTestList", params);

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
						TestList item = new TestList();
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
}
