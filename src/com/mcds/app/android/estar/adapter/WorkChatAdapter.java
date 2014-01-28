package com.mcds.app.android.estar.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.WorkChatReceive;
import com.mcds.app.android.estar.bean.WorkChatSend;

/**
 * 工作聊天窗口适配器
 */
public class WorkChatAdapter extends BaseAdapter
{
	private final int VIEW_TYPE = 2;
	private final int VIEW_TYPE0 = 0;
	private final int VIEW_TYPE1 = 1;
	/**
	 * 上下文
	 */
	private Context context;
	/**
	 * 数据
	 */
	private ArrayList<Object> dataList;
	/**
	 * 滚动状态
	 */
	public int scrollState = OnScrollListener.SCROLL_STATE_IDLE;
	
	/**
	 * 设置滚动状态
	 * 
	 * @param scrollState
	 */
	public void setScrollState(int scrollState)
	{
		this.scrollState = scrollState;
	}
	public WorkChatAdapter(Context context, ArrayList<Object> dataList)
	{
		this.dataList = dataList;
		this.context = context;
	}
	@Override
	public int getCount()
	{
		if (dataList != null)
		{
			return dataList.size();
		}
		return 0;
	}
	@Override
	public int getViewTypeCount()
	{
		return VIEW_TYPE;
	}
	@Override
	public Object getItem(int position)
	{
		if (dataList != null)
		{
			return dataList.get(position);
		}
		return null;
	}
	@Override
	public long getItemId(int position)
	{
		return 0;
	}
	@Override
	public int getItemViewType(int position)
	{
		Object object = dataList.get(position);
		if (dataList != null)
		{
			if (object instanceof WorkChatSend)
			{
				return VIEW_TYPE0;
			}
			else if (object instanceof WorkChatReceive)
			{
				return VIEW_TYPE1;
			}
		}
		return super.getItemViewType(position);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		int VIEW_TYPE = getItemViewType(position);
		Object object = dataList.get(position);
		final ViewHolder holder;
		if (convertView == null)
		{
			holder = new ViewHolder();
			switch (VIEW_TYPE)
			{
				case VIEW_TYPE0:// 发送
					convertView = LayoutInflater.from(context).inflate(R.layout.work_chat_adapter_send, null);
					holder.sendImageView = (ImageView) convertView.findViewById(R.id.work_chat_adapter_send_image);
					holder.sendTextView = (TextView) convertView.findViewById(R.id.work_chat_adapter_send_text_view);
					break;
				case VIEW_TYPE1:// 接收
					convertView = LayoutInflater.from(context).inflate(R.layout.work_chat_adapter_receive, null);
					holder.receiveImageView = (ImageView) convertView.findViewById(R.id.work_chat_adapter_receive_image);
					holder.receiveTextView = (TextView) convertView.findViewById(R.id.work_chat_adapter_receive_text_view);
					break;
			}
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		if (holder != null)
		{
			switch (VIEW_TYPE)
			{
				case VIEW_TYPE0:
					WorkChatSend chatSend = (WorkChatSend) object;
					holder.sendTextView.setText(chatSend.content);
					break;
				case VIEW_TYPE1:
					WorkChatReceive chatReceive = (WorkChatReceive) object;
					holder.receiveTextView.setText(chatReceive.content);
					break;
			}
		}
		return convertView;
	}
	
	private class ViewHolder
	{
		ImageView receiveImageView;
		ImageView sendImageView;
		TextView receiveTextView;
		TextView sendTextView;
	}
}
