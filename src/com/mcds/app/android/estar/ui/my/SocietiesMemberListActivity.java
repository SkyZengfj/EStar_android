package com.mcds.app.android.estar.ui.my;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.SocietiesMemberList;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProviderTC;
import com.mcds.app.android.estar.ui.BaseActivity;

/**
 * 社团成员列表
 * 
 * @author TangChao
 */
public class SocietiesMemberListActivity extends BaseActivity
{
	/**
	 * 社团ID
	 */
	private String club_id;
	private ReturnResult<SocietiesMemberList> rrSocietiesMemberList;
	/**
	 * 适配器
	 */
	private SocietiesMemberListAdapter adapter;
	/**
	 * 列表
	 */
	private ListView listView;
	/**
	 * 关注状态按钮
	 */
	private ImageButton statusImageBtn;
	/**
	 * 关注状态
	 */
	private String status;
	/**
	 * 性别图标
	 */
	private ImageView sexImage;
	/**
	 * 性别
	 */
	private String sex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.societies_member_list);
		statusImageBtn = (ImageButton) findViewById(R.id.soieties_member_list_adapter_status);
		sexImage = (ImageView) findViewById(R.id.soieties_member_list_adapter_sex);
		adapter = new SocietiesMemberListAdapter();
		listView = (ListView) findViewById(R.id.societies_member_list_view);
		listView.setAdapter(adapter);
		club_id = getIntent().getStringExtra("club_id");
		if (!club_id.equals(""))
		{
			getClubMembers(club_id, "0", "7");
		}
	}
	/**
	 * 请求社团成员
	 */
	private void getClubMembers(final String club_id, final String page_num, final String page_size)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				showWaitingDialog();
				if (!UserManager.uid.equals(""))
				{
					rrSocietiesMemberList = ConnectProviderTC.getClubMember(UserManager.uid, club_id, page_num, page_size);
					doUI();
				}
				hideWaitingDialog();
			}
		}).start();
	}
	/**
	 * 刷新界面
	 */
	private void doUI()
	{
		this.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				if (rrSocietiesMemberList.status.equals("0"))
				{
					adapter.setItems(rrSocietiesMemberList.getDatas());
					adapter.notifyDataSetChanged();
				}
			}
		});
	}
	
	/**
	 * 社团成员列表适配器
	 */
	private class SocietiesMemberListAdapter extends BaseListAdapter<SocietiesMemberList>
	{
		ViewHolder holder = null;
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.societies_member_list_adapter, null);
				holder = new ViewHolder();
				holder.memberName = (TextView) convertView.findViewById(R.id.soieties_member_list_adapter_name);
				holder.memberWork = (TextView) convertView.findViewById(R.id.soieties_member_list_adapter_work);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			if (holder != null)
			{
				SocietiesMemberList item = this.getItem(position);
				holder.memberName.setText(item.name);
				holder.memberWork.setText(item.work);
				status = item.attention_flag;
				sex = item.sex;
				// if (sex.equals("男"))
				// {
				// sexImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.societies_member_list_adapter_sex_male));
				// System.out.println("-------->" + "A");
				// }
				// else if (sex.equals("女"))
				// {
				// System.out.println("-------->" + "B");
				// sexImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_launcher));
				// }
				// if (status.equals("0"))// 已关注
				// {
				// statusImageBtn.setBackgroundDrawable(getResources().getDrawable(
				// R.drawable.societies_member_list_adapter_status0));
				// }
				// else if (status.equals("1"))
				// {
				// statusImageBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_launcher));
				// }
			}
			return convertView;
		}
	}
	
	private class ViewHolder
	{
		/**
		 * 姓名
		 */
		TextView memberName;
		/**
		 * 岗位
		 */
		TextView memberWork;
	}
}
