package com.mcds.app.android.estar.bean;

public class BaseReturnResult {
	/**
	 * 返回状态："0"成功；"1"逻辑错误；"-1"未知错误；"-2"解析json错误；"-3"网络超时
	 */
	public String status;

	public String info;
}
