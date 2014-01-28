package com.mcds.app.android.estar.ui.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.SocietiesList;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.provider.ConnectProviderTC;
import com.mcds.app.android.estar.ui.BaseActivity;
import com.mcds.app.android.estar.util.Utility;

/**
 * 社团
 * 
 * @author TangChao
 */
public class SocietiesActivity extends BaseActivity implements OnClickListener
{
	/**
	 * 社团列表
	 */
	private ListView listView;
	/**
	 * 社团列表适配器
	 */
	private MySocietiesListAdapter adapter;
	/**
	 * 取消按钮
	 */
	private ImageButton cancelBtn;
	/**
	 * 请求社团列表的响应
	 */
	private ReturnResult<SocietiesList> rrSocietiesList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.my_societies);
		cancelBtn = (ImageButton) findViewById(R.id.my_societies_cancel_btn);
		cancelBtn.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.my_societies_list_view);
		adapter = new MySocietiesListAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				SocietiesList item = (SocietiesList) arg0.getItemAtPosition(arg2);
				Intent intent = new Intent(SocietiesActivity.this, SocietiesChatActivity.class);
				intent.putExtra("club_id", item.club_id);
				startActivity(intent);
			}
		});
		// 请求数据
		getData("0", "5");
	}
	/**
	 * 请求社团列表
	 */
	private void getData(final String page_num, final String page_size)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				showWaitingDialog();
				if (!UserManager.uid.equals(""))
				{
					rrSocietiesList = ConnectProviderTC.getMyClubList(UserManager.uid, page_num, page_size);
					doUI();
				}
				hideWaitingDialog();
			}
		}).start();
	}
	/**
	 * 刷新界面
	 * 
	 * @author TangChao
	 */
	private void doUI()
	{
		this.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				adapter.setItems(rrSocietiesList.getDatas());
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	/**
	 * 社团列表适配器
	 * 
	 * @author TangChao
	 */
	private class MySocietiesListAdapter extends BaseListAdapter<SocietiesList>
	{
		private ViewHolder holder = null;
		private AsyncBitmapLoader bitMapLoader = new AsyncBitmapLoader();
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.societies_list_adapter, null);
				holder = new ViewHolder();
				holder.societiesImage = (ImageView) convertView.findViewById(R.id.societies_list_adapter_image);
				holder.societiesName = (TextView) convertView.findViewById(R.id.societies_list_adapter_name);
				holder.societiesMessageNum = (TextView) convertView.findViewById(R.id.societies_list_adapter_message_num);
				holder.deleteBtnImageView = (ImageView) convertView.findViewById(R.id.societies_list_adapter_delete_btn);
				convertView.setTag(holder);
				// convertView.setOnLongClickListener(new
				// View.OnLongClickListener()
				// {
				// @Override
				// public boolean onLongClick(View v)
				// {
				// ViewHolder holderTemp = (ViewHolder) v.getTag();
				// holderTemp.deleteBtnImageView.setVisibility(View.VISIBLE);
				// return true;
				// }
				// });
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			if (holder != null)
			{
				SocietiesList item = this.getItem(position);
				holder.societiesName.setText(item.name);
				holder.societiesMessageNum.setText(item.new_num);
				Bitmap bitmap = bitMapLoader.loadBitmap(holder.societiesImage, item.img,
						Utility.dip2px(SocietiesActivity.this, (float) 40.0),
						Utility.dip2px(SocietiesActivity.this, (float) 40.0), new ImageCallBack()
						{
							@Override
							public void imageLoad(ImageView imageView, Bitmap bitmap)
							{
								if (bitmap == null)
								{
									bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
								}
								imageView.setImageBitmap(bitmap);
							}
						});
				if (bitmap != null)
				{
					holder.societiesImage.setImageBitmap(bitmap);
				}
			}
			return convertView;
		}
	}
	
	/**
	 * 适配器列表缓存类
	 */
	private class ViewHolder
	{
		// 社团头像
		ImageView societiesImage;
		// 社团名字
		TextView societiesName;
		// 社团消息数
		TextView societiesMessageNum;
		/**
		 * 删除条目按钮
		 */
		ImageView deleteBtnImageView;
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.my_societies_cancel_btn:
				finish();
				break;
			case R.id.societies_list_adapter_delete_btn:
				/* 调用删除接口 */
				break;
			default:
				break;
		}
	}
}
