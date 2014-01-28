package com.mcds.app.android.estar.ui.work;

import android.R.integer;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.MissionList;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProviderTC;
import com.mcds.app.android.estar.ui.BaseActivity;

/**
 * 任务列表
 * 
 * @author TangChao
 */
public class MissionListActivity extends BaseActivity implements OnItemClickListener, OnItemLongClickListener
{
	private static final String Tag = "MISSION_LIST";
	private String theme_id;
	private ListView listView;
	private ReturnResult<MissionList> rrMissionList;
	private MissionListAdapter adapter0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.work_home_mission_list);
		listView = (ListView) findViewById(R.id.work_home_mission_list_view);
		adapter0 = new MissionListAdapter();
		listView.setAdapter(adapter0);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		theme_id = getIntent().getStringExtra("theme_id");
		Log.i("theme_id", theme_id);
		/**
		 * 取消按钮
		 */
		findViewById(R.id.work_home_mission_list_cancel).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		/**
		 * 新建任务
		 */
		findViewById(R.id.work_home_mission_detail_add_mission).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MissionListActivity.this, AddMissionActivity.class);
				intent.putExtra("TAG", Tag);
				intent.putExtra("theme_id", theme_id);
				startActivity(intent);
			}
		});
		getData(theme_id);
	}
	/**
	 * 請求任务列表
	 * 
	 * @param theme_id
	 */
	private void getData(final String theme_id)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				showWaitingDialog();
				if (!UserManager.uid.equals(""))
				{
					rrMissionList = ConnectProviderTC.getMissionList(UserManager.uid, theme_id);
					Log.i("UID", UserManager.uid);
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
				if (rrMissionList.getDatas().size() == 0)
				{
					Toast.makeText(getApplicationContext(), "暂无任务记录", Toast.LENGTH_SHORT).show();
					return;
				}
				if (rrMissionList.status.equals("0"))
				{
					adapter0.setItems(rrMissionList.getDatas());
					adapter0.notifyDataSetChanged();
				}
			}
		});
	}
	
	/**
	 * 适配器
	 */
	private class MissionListAdapter extends BaseListAdapter<MissionList>
	{
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder = null;
			if (convertView == null)
			{
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.work_home_mission_list_adapter, null);
				holder = new ViewHolder();
				holder.mission_status_iv = (ImageView) convertView.findViewById(R.id.work_home_mission_status_icon);
				holder.name = (TextView) convertView.findViewById(R.id.top_text0);
				holder.leader_name = (TextView) convertView.findViewById(R.id.leader_name);
				holder.end_time = (TextView) convertView.findViewById(R.id.end_time);
				holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progress_bar);
				holder.progress_num = (TextView) convertView.findViewById(R.id.progress_num);
				holder.content = (TextView) convertView.findViewById(R.id.work_home_mission_content);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			if (holder != null)
			{
				MissionList item = this.getItem(position);
				holder.name.setText(item.name + "-");
				holder.leader_name.setText(item.leader);
				holder.end_time.setText(item.end_time);
				holder.missionStatus = item.status;
				holder.progress_num.setText(item.schedule + "%");
				holder.content.setText(item.content);
				if (holder.missionStatus.equals("0"))
				{
					holder.mission_status_iv.setBackgroundResource(R.drawable.work_home_mission_list_adapter_item_icon0);
				}
				else if (holder.missionStatus.equals("1"))
				{
					holder.mission_status_iv.setBackgroundResource(R.drawable.work_home_mission_list_adapter_item_icon1);
				}
				else if (holder.missionStatus.equals("2"))
				{
					holder.mission_status_iv.setBackgroundResource(R.drawable.work_home_mission_list_adapter_item_icon2);
				}
				else if (holder.missionStatus.equals("3"))
				{
					holder.mission_status_iv.setBackgroundResource(R.drawable.work_home_mission_list_adapter_item_icon3);
				}
				holder.progressBar.setMax(100);
				int progress = Integer.parseInt(item.schedule);
				holder.progressBar.setSecondaryProgress(progress);
			}
			return convertView;
		}
	}
	
	private class ViewHolder
	{
		String missionStatus;
		ImageView mission_status_iv;
		TextView name;
		TextView leader_name;
		TextView end_time;
		ProgressBar progressBar;
		TextView progress_num;
		TextView content;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		MissionList missionList = (MissionList) arg0.getItemAtPosition(arg2);
		Intent intent = new Intent(MissionListActivity.this, MissionDetailActivity.class);
		intent.putExtra("mission_id", missionList.mission_id);
		intent.putExtra("theme_id", theme_id);
		intent.putExtra("schedule", missionList.schedule);
		startActivity(intent);
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		MissionList item = rrMissionList.getDatas().get(arg2);
		Intent intent = new Intent(this, MyDeleteMissionListDialogActivity.class);
		intent.putExtra("mission_id", item.mission_id);
		startActivity(intent);
		return true;
	}
}
