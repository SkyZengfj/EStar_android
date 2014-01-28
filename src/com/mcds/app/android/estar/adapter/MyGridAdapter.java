package com.mcds.app.android.estar.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.OperateButton;
import com.mcds.app.android.estar.bean.WorkMemberListAdapterItem;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;

public class MyGridAdapter extends ArrayListAdapter
{
	/**
	 * 条目数
	 */
	private final static int VIEW_TYPE = 2;
	/**
	 * 成员
	 */
	private final static int VIEW_TYPE0 = 0;
	/**
	 * 操作按钮
	 */
	private final static int VIEW_TYPE1 = 1;
	/**
	 * 封装后的条目数据
	 */
	private ArrayList<WorkMemberListAdapterItem> arrayListItem;
	/**
	 * 封装后的元数据
	 */
	private ArrayList<Object> arrayList;
	/**
	 * 上下文
	 */
	private Context context;
	/**
	 * 布局转化对象
	 */
	private LayoutInflater layoutInflater;
	
	public MyGridAdapter(ArrayList<Object> arrayList, Context context)
	{
		super();
		this.arrayList = arrayList;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		Log.i("size--------->", arrayList.size() + "");
	}
	@Override
	public int getItemViewType(int position)
	{
		// TODO Auto-generated method stub
		Object object = arrayList.get(position);
		if (object instanceof WorkMemberListAdapterItem)
		{
			return VIEW_TYPE0;
		}
		else if (object instanceof OperateButton)
		{
			return VIEW_TYPE1;
		}
		return super.getItemViewType(position);
	}
	@Override
	public int getViewTypeCount()
	{
		// TODO Auto-generated method stub
		return VIEW_TYPE;
	}
	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		if (arrayList != null)
		{
			return arrayList.get(position);
		}
		return null;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		final ViewHolder holder;
		AsyncBitmapLoader bitMapLoader = new AsyncBitmapLoader();
		Object object = this.getItem(position);
		if (convertView == null)
		{
			holder = new ViewHolder();
			switch (VIEW_TYPE)
			{
				case VIEW_TYPE0:// 成员
					convertView = layoutInflater.inflate(R.layout.work_home_matter_member_adapter, null);
					holder.headImage = (ImageView) convertView.findViewById(R.id.adapter_item_image);
					holder.memberNameTextView = (TextView) convertView.findViewById(R.id.adapter_item_text);
					break;
				case VIEW_TYPE1:// 操作
					convertView = layoutInflater.inflate(R.layout.work_home_matter_member_adapter_operate, null);
					holder.addBtn = (ImageView) convertView.findViewById(R.id.work_home_member_adapter_add);
					holder.deleteBtn = (ImageView) convertView.findViewById(R.id.work_home_member_adapter_delete);
					break;
				default:
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
				case VIEW_TYPE0:// 成员
					WorkMemberListAdapterItem memberItem = (WorkMemberListAdapterItem) object;
					holder.memberNameTextView.setText(memberItem.memberName);
					// Bitmap bitMap = bitMapLoader.loadBitmap(holder.headImage,
					// memberItem.headImage,
					// Utility.dip2px(context,
					// (float)116L),Utility.dip2px(context, (float)116L,new
					// ImageV);
					break;
				case VIEW_TYPE1:// 操作
					OperateButton operateItem = (OperateButton) object;
					holder.addBtn.setImageBitmap(operateItem.addBtn);
					holder.deleteBtn.setImageBitmap(operateItem.deleteBtn);
					break;
				default:
					break;
			}
		}
		return convertView;
	}
	
	private class ViewHolder
	{
		/**
		 * 成员头像
		 */
		ImageView headImage;
		/**
		 * 成员姓名
		 */
		TextView memberNameTextView;
		/**
		 * 增加按钮
		 */
		ImageView addBtn;
		/**
		 * 减少按钮
		 */
		ImageView deleteBtn;
	}
}
