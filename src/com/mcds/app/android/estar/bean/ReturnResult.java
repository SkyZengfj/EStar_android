package com.mcds.app.android.estar.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口返回的状态类
 * 
 * @author Sky
 * 
 * @param <T>
 */
public class ReturnResult<T> extends BaseReturnResult {

	private List<T> datas;

	public ReturnResult() {
		datas = new ArrayList<T>();
	}

	public void addData(T item) {
		this.datas.add(item);
	}

	public List<T> getDatas() {
		return this.datas;
	}
}
