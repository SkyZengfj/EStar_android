package com.mcds.app.android.estar.ui.work;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.MissionDetail;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProviderTC;
import com.mcds.app.android.estar.ui.BaseActivity;

/**
 * 任务详情
 * 
 * @author TangChao
 */
public class MissionDetailActivity extends BaseActivity implements OnClickListener
{
	private String theme_id;
	private String mission_id;
	private ReturnResult<MissionDetail> rrMissionDetail;
	/**
	 * 标题任务名
	 */
	private TextView titleName;
	/**
	 * 任务名
	 */
	private TextView missionName;
	/**
	 * 负责人
	 */
	private TextView missionLeadr;
	/**
	 * 任务开始时间
	 */
	private TextView startTime;
	/**
	 * 结束时间
	 */
	private TextView endTime;
	/**
	 * 任务状态
	 */
	private TextView missionStatus;
	/**
	 * 任务状态判断变量
	 */
	private String status;
	/**
	 * 任务执行成员
	 */
	private TextView missionMember;
	/**
	 * 任务内容
	 */
	private TextView missionContent;
	/**
	 * 序列化对象
	 */
	private MissionDetail missionDetail = null;
	/**
	 * 任务进度
	 */
	private String schedule;
	/**
	 * 进度按钮
	 */
	private TextView progressBtn0, progressBtn1, progressBtn2, progressBtn3, progressBtn4;
	
//	//负责人ID
//	private String leader_Id;
//	//成员id
//	private String member_id;
//	//选中人员id集合
//	private ArrayList<String> addMatterMemberIdArrayList;
//	//成员Name集合
//	private ArrayList<String> addMatterMemberuserNameArrayList;
//	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.work_home_mission_detail);
		initWidget();
		/*-----------------------------------------------------*/
		theme_id = getIntent().getStringExtra("theme_id");
		mission_id = getIntent().getStringExtra("mission_id");
		schedule = getIntent().getStringExtra("schedule");
		Log.i("theme_id", theme_id);
		Log.i("mission_id", mission_id);
		/*----------------------------------------------------*/
		getData(mission_id);
		int progress = 0;
		if (schedule != null)
		{
			progress = Integer.parseInt(schedule);
		}
		if (progress < 20 || progress > 100)
		{
			progressBtn0.setBackgroundResource(0);
			progressBtn1.setBackgroundResource(0);
			progressBtn2.setBackgroundResource(0);
			progressBtn3.setBackgroundResource(0);
			progressBtn4.setBackgroundResource(0);
		}
		else if (progress >= 20 && progress < 40)
		{
			progressBtn0.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item0);
			progressBtn1.setBackgroundResource(0);
			progressBtn2.setBackgroundResource(0);
			progressBtn3.setBackgroundResource(0);
			progressBtn4.setBackgroundResource(0);
		}
		else if (progress >= 40 && progress < 60)
		{
			progressBtn0.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item0);
			progressBtn1.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
			progressBtn2.setBackgroundResource(0);
			progressBtn3.setBackgroundResource(0);
			progressBtn4.setBackgroundResource(0);
		}
		else if (progress >= 60 && progress < 80)
		{
			progressBtn0.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item0);
			progressBtn1.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
			progressBtn2.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
			progressBtn3.setBackgroundResource(0);
			progressBtn4.setBackgroundResource(0);
		}
		else if (progress >= 80 && progress < 100)
		{
			progressBtn0.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item0);
			progressBtn1.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
			progressBtn2.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
			progressBtn3.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
			progressBtn4.setBackgroundResource(0);
		}
		else if (progress == 100)
		{
			progressBtn0.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item0);
			progressBtn1.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
			progressBtn2.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
			progressBtn3.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
			progressBtn4.setBackgroundResource(R.drawable.work_home_member_list_title_progress_item1);
		}
	}
	private void initWidget()
	{
		titleName = (TextView) findViewById(R.id.work_home_mission_detail_title_name);
		missionName = (TextView) findViewById(R.id.work_home_mission_detail_name);
		missionLeadr = (TextView) findViewById(R.id.work_home_mission_detail_leader);
		startTime = (TextView) findViewById(R.id.work_home_mission_detail_start_time);
		endTime = (TextView) findViewById(R.id.work_home_mission_detail_end_time);
		missionStatus = (TextView) findViewById(R.id.work_home_mission_detail_mission_status);
		missionMember = (TextView) findViewById(R.id.work_home_mission_detail_operate_members);
		missionContent = (TextView) findViewById(R.id.work_home_mission_detail_content);
		progressBtn0 = (TextView) findViewById(R.id.work_home_mission_detail_progress_btn0);
		progressBtn1 = (TextView) findViewById(R.id.work_home_mission_detail_progress_btn1);
		progressBtn2 = (TextView) findViewById(R.id.work_home_mission_detail_progress_btn2);
		progressBtn3 = (TextView) findViewById(R.id.work_home_mission_detail_progress_btn3);
		progressBtn4 = (TextView) findViewById(R.id.work_home_mission_detail_progress_btn4);
		missionName.setOnClickListener(this);
		startTime.setOnClickListener(this);
		endTime.setOnClickListener(this);
		missionStatus.setOnClickListener(this);
		missionMember.setOnClickListener(this);
		missionContent.setOnClickListener(this);
		/**
		 * 添加任务执行成员
		 */
		findViewById(R.id.work_home_mission_detail_linear_layout_bottom).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MissionDetailActivity.this, MemberActivity.class);
				startActivity(intent);
			}
		});
		/**
		 * 取消
		 */
		findViewById(R.id.work_home_mission_detail_cancel).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		/**
		 * 编辑任务
		 */
		findViewById(R.id.work_home_mission_detail_edit).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (missionDetail == null)
				{
					Log.i("mission_dialog", "return");
					return;
				}
				Intent intent = new Intent(MissionDetailActivity.this, MyMissionDialog.class);
				intent.putExtra("theme_id", theme_id);
				intent.putExtra("mission_id", mission_id);
				intent.putExtra("MISSION_DETAIL_OBJECT", missionDetail);
				intent.putExtra("schedule", schedule);
				startActivity(intent);
			}
		});
	}
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data)
//	{
//		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == 100)// 指定负责人
//		{
//			if (resultCode == 101)// 成功
//			{
//				addMatterMemberIdArrayList = data.getStringArrayListExtra("atUserID");
//				addMatterMemberuserNameArrayList = data.getStringArrayListExtra("userName");
//				System.out.println(addMatterMemberIdArrayList.toString()+"==============="+addMatterMemberuserNameArrayList);
//				leader_Id = addMatterMemberIdArrayList.get(0);
//				missionLeadr.setText(addMatterMemberuserNameArrayList.get(0));
//			}
//			else if (resultCode == 102)// 失败
//			{
//				Toast.makeText(MissionDetailActivity.this, "指定失败,请重试", Toast.LENGTH_SHORT).show();
//			}
//		}
//		else if (requestCode == 200)// 指定成员
//		{
//			if (resultCode == 201)// 成功
//			{
//				addMatterMemberIdArrayList = data.getStringArrayListExtra("atUserID");
//				String str = addMatterMemberIdArrayList.toString();
//				member_id = str.substring(1, str.length()-1);
//				addMatterMemberuserNameArrayList = data.getStringArrayListExtra("userName");
//				String ss = addMatterMemberuserNameArrayList.toString();
//				/**
//				 * 显示到按钮上
//				 */
//				missionMember.setText(ss.substring(1, ss.length()-1));
//			}
//			else if (resultCode == 202)// 失败
//			{
//				Toast.makeText(MissionDetailActivity.this, "添加失败,请重试", Toast.LENGTH_SHORT).show();
//			}
//		}
//
//	}
	
	
	
	/**
	 * 请求任务详情
	 */
	private void getData(final String mission_id)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				if (!UserManager.uid.equals(""))
				{
					showWaitingDialog();
					rrMissionDetail = ConnectProviderTC.getMissionDetail(UserManager.uid, mission_id);
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
				if (rrMissionDetail.status.equals("0"))
				{
					if (rrMissionDetail.getDatas().size() == 1)
					{
						missionDetail = rrMissionDetail.getDatas().get(0);
						titleName.setText(missionDetail.name);
						missionName.setText(missionDetail.name);
						missionLeadr.setText("负责人:" + missionDetail.leader);
						startTime.setText(missionDetail.start_time + "开始");
						endTime.setText(missionDetail.end_time + "结束");
						missionMember.setText("成员:" + missionDetail.members);
						missionContent.setText("任务简介:" + missionDetail.content);
						status = missionDetail.status;
						if (status.equals("0"))
						{
							missionStatus.setText("未完成");
						}
						else if (status.equals("1"))
						{
							missionStatus.setText("完成");
						}
						else if (status.equals("2"))
						{
							missionStatus.setText("逾期");
						}
					}
				}
			}
		});
	}
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.work_home_mission_detail_name:// 任务名
				break;
			case R.id.work_home_mission_detail_start_time:// 任务开始时间
				break;
			case R.id.work_home_mission_detail_end_time:// 任务截止时间
				break;
			case R.id.work_home_mission_detail_mission_status:// 任务状态
				break;
			case R.id.work_home_mission_detail_content:// 任务内容
				break;
			default:
				break;
		}
	}
}
