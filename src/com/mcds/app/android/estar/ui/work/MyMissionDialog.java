package com.mcds.app.android.estar.ui.work;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.MissionDetail;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProviderTC;
import com.mcds.app.android.estar.ui.BaseActivity;

public class MyMissionDialog extends BaseActivity implements OnClickListener
{
	private static final String Tag = "MY_MISSION_DIALOG";
	private MissionDetail missionDetail;
	private String theme_id;
	private String mission_id;
	private TextView mission_edit_btn;
	private TextView mission_complete_btn;
	private TextView mission_delete_btn;
	private TextView mission_cancel_btn;
	private ReturnResult<String> rrDeleteMission;
	private ReturnResult<String> rrSetMissionComplete;
	private TextView progressBtn0, progressBtn1, progressBtn2, progressBtn3, progressBtn4;
	private String schedule;
	private int progres = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.work_home_mission_detail_edit_dialog);
		mission_edit_btn = (TextView) findViewById(R.id.mission_edit_btn);
		mission_complete_btn = (TextView) findViewById(R.id.mission_complete_btn);
		mission_delete_btn = (TextView) findViewById(R.id.mission_delete_btn);
		mission_cancel_btn = (TextView) findViewById(R.id.mission_cancel_btn);
		/*----------------------------------------*/
		progressBtn0 = (TextView) findViewById(R.id.work_home_mission_edit_dialog_prigress_btn0);
		progressBtn1 = (TextView) findViewById(R.id.work_home_mission_edit_dialog_prigress_btn1);
		progressBtn2 = (TextView) findViewById(R.id.work_home_mission_edit_dialog_prigress_btn2);
		progressBtn3 = (TextView) findViewById(R.id.work_home_mission_edit_dialog_prigress_btn3);
		progressBtn4 = (TextView) findViewById(R.id.work_home_mission_edit_dialog_prigress_btn4);
		progressBtn0.setOnClickListener(this);
		progressBtn1.setOnClickListener(this);
		progressBtn2.setOnClickListener(this);
		progressBtn3.setOnClickListener(this);
		progressBtn4.setOnClickListener(this);
		/*---------------------------------------*/
		mission_edit_btn.setOnClickListener(this);
		mission_complete_btn.setOnClickListener(this);
		mission_delete_btn.setOnClickListener(this);
		mission_cancel_btn.setOnClickListener(this);
		Intent intent = getIntent();
		theme_id = intent.getStringExtra("theme_id");
		mission_id = intent.getStringExtra("mission_id");
		missionDetail = (MissionDetail) intent.getSerializableExtra("MISSION_DETAIL_OBJECT");
		/*----------------------------------------------*/
		schedule = intent.getStringExtra("schedule");
		progres = Integer.parseInt(schedule);
		Log.i("schedule---------->", schedule);
		Log.i("progress---------->", progres + "");
		if (progres >= 0 && progres < 20)
		{
			progressBtn0.setBackgroundResource(0);
			progressBtn1.setBackgroundResource(0);
			progressBtn2.setBackgroundResource(0);
			progressBtn3.setBackgroundResource(0);
			progressBtn4.setBackgroundResource(0);
		}
		else if (progres >= 20 && progres < 40)
		{
			progressBtn0.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item0);
			progressBtn1.setBackgroundResource(0);
			progressBtn2.setBackgroundResource(0);
			progressBtn3.setBackgroundResource(0);
			progressBtn4.setBackgroundResource(0);
		}
		else if (progres >= 40 && progres < 60)
		{
			progressBtn0.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item0);
			progressBtn1.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
			progressBtn2.setBackgroundResource(0);
			progressBtn3.setBackgroundResource(0);
			progressBtn4.setBackgroundResource(0);
		}
		else if (progres >= 60 && progres < 80)
		{
			progressBtn0.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item0);
			progressBtn1.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
			progressBtn2.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
			progressBtn3.setBackgroundResource(0);
			progressBtn4.setBackgroundResource(0);
		}
		else if (progres >= 80 && progres < 100)
		{
			progressBtn0.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item0);
			progressBtn1.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
			progressBtn2.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
			progressBtn3.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
			progressBtn4.setBackgroundResource(0);
		}
		else if (progres == 100)
		{
			progressBtn0.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item0);
			progressBtn1.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
			progressBtn2.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
			progressBtn3.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
			progressBtn4.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
		}
	}
	/**
	 * 删除任务
	 */
	private void deleteMission(final String mission_id)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				if (!UserManager.uid.equals(""))
				{
					rrDeleteMission = ConnectProviderTC.deleteMission(UserManager.uid, mission_id);
					doUIForDelete();
				}
			}
		}).start();
	}
	/**
	 * 标记完成
	 */
	private void setMissionComplete(final String mission_id)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				if (schedule.equals("100"))
				{
					Toast.makeText(getApplicationContext(), "该任务已完成", Toast.LENGTH_SHORT).show();
				}
				rrSetMissionComplete = ConnectProviderTC.setMissionComplete(mission_id);
				schedule = "100";
				doUIForSign();
			}
		}).start();
	}
	/**
	 * 请求成功后通知刷新界面
	 */
	private void doUIForDelete()
	{
		this.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				if (rrDeleteMission.status.equals("0"))
				{
					Toast.makeText(getApplicationContext(), "操作成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(MyMissionDialog.this, MissionListActivity.class);
					startActivity(intent);
				}
				else
				{
					Toast.makeText(getApplicationContext(), "操作成功", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		});
	}
	private void doUIForSign()
	{
		this.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				if (rrSetMissionComplete.status.equals("0"))
				{
					Toast.makeText(getApplicationContext(), "操作成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(MyMissionDialog.this, MissionDetailActivity.class);
					intent.putExtra("theme_id", theme_id);
					intent.putExtra("mission_id", mission_id);
					intent.putExtra("schedule", schedule);
					startActivity(intent);
				}
				else
				{
					Toast.makeText(getApplicationContext(), "操作成功", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		});
	}
	@Override
	protected void onPause()
	{
		super.onPause();
		finish();
	}
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.mission_edit_btn:// 编辑
				Intent intent = new Intent(this, AddMissionActivity.class);
				intent.putExtra("TAG", Tag);
				intent.putExtra("SerializableObject", missionDetail);
				intent.putExtra("theme_id", theme_id);
				intent.putExtra("mission_id", mission_id);
				startActivity(intent);
				break;
			case R.id.mission_complete_btn:// 完成
				setMissionComplete(mission_id);
				break;
			case R.id.mission_delete_btn:// 删除
				deleteMission(mission_id);
				break;
			case R.id.mission_cancel_btn:// 取消
				if (!this.isFinishing())
				{
					this.finish();
				}
				break;
			case R.id.work_home_mission_edit_dialog_prigress_btn0:// 一个点
				if (progres < 0)
				{
					return;
				}
				else if (progres >= 0 && progres < 20)
				{
					progressBtn0.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item0);
					progres = 20;
				}
				else if (progres >= 20)
				{
					Toast.makeText(MyMissionDialog.this, "任务进度不能减少", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.work_home_mission_edit_dialog_prigress_btn1:// 二个点
				if (progres < 0)
				{
					return;
				}
				else if (progres >= 0 && progres < 40)
				{
					progressBtn0.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item0);
					progressBtn1.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
					progres = 40;
				}
				else if (progres >= 40)
				{
					Toast.makeText(MyMissionDialog.this, "任务进度不能减少", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.work_home_mission_edit_dialog_prigress_btn2:// 三个点
				if (progres < 0)
				{
					return;
				}
				else if (progres >= 0 && progres < 60)
				{
					progressBtn0.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item0);
					progressBtn1.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
					progressBtn2.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
					progres = 60;
				}
				else if (progres >= 60)
				{
					Toast.makeText(MyMissionDialog.this, "任务进度不能减少", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.work_home_mission_edit_dialog_prigress_btn3:// 四个点
				if (progres < 0)
				{
					return;
				}
				else if (progres >= 0 && progres < 80)
				{
					progressBtn0.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item0);
					progressBtn1.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
					progressBtn2.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
					progressBtn3.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
					progres = 80;
				}
				else if (progres >= 80)
				{
					Toast.makeText(MyMissionDialog.this, "任务进度不能减少", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.work_home_mission_edit_dialog_prigress_btn4:// 五个点
				if (progres < 0)
				{
					return;
				}
				else if (progres >= 0 && progres < 100)
				{
					progressBtn0.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item0);
					progressBtn1.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
					progressBtn2.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
					progressBtn3.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
					progressBtn4.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
					progres = 100;
				}
				else if (progres >= 100)
				{
					Toast.makeText(MyMissionDialog.this, "任务进度不能减少", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
		}
	}
}
