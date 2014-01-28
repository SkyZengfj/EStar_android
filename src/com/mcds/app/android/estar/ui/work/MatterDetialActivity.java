package com.mcds.app.android.estar.ui.work;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.MatterDetail;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.ui.BaseActivity;

/**
 * 事项详情
 * 
 * @author TangChao
 */
public class MatterDetialActivity extends BaseActivity implements OnClickListener
{
	/**
	 * 上一个activity传过来的themeId
	 */
	private String theme_id;
	private ReturnResult<MatterDetail> rrMatterDetail;
	private MatterDetail matterDetail = null;
	/**
	 * 事项标题
	 */
	private TextView name;
	/**
	 * 事项内容
	 */
	private TextView content;
	/**
	 * 创建事项的人
	 */
	private TextView writer;
	/**
	 * 创建事项的时间
	 */
	private TextView time_start;
	/**
	 * 事项截止时间
	 */
	private TextView time_end;
	/**
	 * 事项负责人
	 */
	private TextView leader;
	/**
	 * 参加该事项的成员数
	 */
	private TextView member_num;
	/**
	 * 讨论组人数
	 */
	private TextView discuss_num;
	/**
	 * 文档数
	 */
	private TextView document_num;
	/**
	 * 任务数
	 */
	private TextView missions_num;
	/**
	 * 任务状态
	 */
	private ImageView task_status;
	/**
	 * 成员
	 */
	private TextView member;
	/**
	 * 讨论
	 */
	private TextView discuss;
	/**
	 * 文档
	 */
	private TextView document;
	/**
	 * 任务
	 */
	private TextView mission;
	private TextView task_leader0, task_leader1, task_end_time0, task_end_time1, task_number;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.work_home_matter_detail);
		initWidget();
		Intent intent = getIntent();
		theme_id = intent.getStringExtra("theme_id");
		Log.i("A-------->theme_id", theme_id);
		getData(theme_id);
		/**
		 * 事项编辑按钮
		 */
		findViewById(R.id.work_home_matter_detail_edit).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (!rrMatterDetail.status.equals("0"))
					if (matterDetail == null)
					{
						Toast.makeText(getApplicationContext(), "无事项详情", Toast.LENGTH_SHORT).show();
						return;
					}
				Intent intent = new Intent(MatterDetialActivity.this, MyMatterDialog.class);
				intent.putExtra("theme_id", theme_id);
				intent.putExtra("MATTER_DETAIL_OBJECT", matterDetail);
				startActivity(intent);
			}
		});
		/**
		 * 取消按钮
		 */
		findViewById(R.id.work_home_matter_detail_add_matter_cancel).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}
	/**
	 * 初始化控件
	 */
	private void initWidget()
	{
		name = (TextView) findViewById(R.id.workHome_matter_details_title);
		content = (TextView) findViewById(R.id.work_home_detail_content);
		writer = (TextView) findViewById(R.id.writer);
		time_start = (TextView) findViewById(R.id.startDate);
		time_end = (TextView) findViewById(R.id.endDate);
		leader = (TextView) findViewById(R.id.leader);
		member_num = (TextView) findViewById(R.id.member_number);
		discuss_num = (TextView) findViewById(R.id.argument_number);
		document_num = (TextView) findViewById(R.id.document_number);
		missions_num = (TextView) findViewById(R.id.discuss_num);
		task_status = (ImageView) findViewById(R.id.task_status);
		task_leader0 = (TextView) findViewById(R.id.task_leader0);
		task_leader1 = (TextView) findViewById(R.id.task_leader1);
		task_end_time0 = (TextView) findViewById(R.id.task_end_time0);
		task_end_time1 = (TextView) findViewById(R.id.task_end_time1);
		task_number = (TextView) findViewById(R.id.task_number);
		member = (TextView) findViewById(R.id.work_home_matter_detail_member);
		discuss = (TextView) findViewById(R.id.work_home_matter_detail_discuss);
		document = (TextView) findViewById(R.id.work_home_matter_detail_document);
		mission = (TextView) findViewById(R.id.work_home_matter_detail_mission);
		member.setOnClickListener(this);
		discuss.setOnClickListener(this);
		document.setOnClickListener(this);
		mission.setOnClickListener(this);
	}
	/**
	 * 请求事项详情
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
					rrMatterDetail = ConnectProvider.getThemeDetail(UserManager.uid, theme_id);
					doUI();
				}
				hideWaitingDialog();
			}
		}).start();
	}
	/**
	 * 刷新数据
	 */
	private void doUI()
	{
		this.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				if (rrMatterDetail.status.equals("0"))
				{
					if (rrMatterDetail.getDatas().size() == 1)
					{
						matterDetail = rrMatterDetail.getDatas().get(0);
						name.setText(matterDetail.name);
						content.setText(matterDetail.content);
						writer.setText(matterDetail.writer);
						leader.setText(matterDetail.leader);
						/*----------------------------------------------*/
						String returnTime = matterDetail.time_end;
						time_start.setText("创建于 " + returnTime);
						time_end.setText("还有" + matterDetail.time_end + "天截止");
						member_num.setText(matterDetail.member_num);
						document_num.setText(matterDetail.document_num);
						discuss_num.setText(matterDetail.discuss_num);
						missions_num.setText("(" + matterDetail.missions_num + ")");
						task_leader0.setText(matterDetail.leader + " 负责");
						task_leader1.setText(matterDetail.leader + " 负责");
						task_end_time0.setText(matterDetail.time_start + "截止");
						task_end_time1.setText("未设置截止时间");
						task_number.setText(matterDetail.missions_num);
						/**
						 * 判断任务当前状态并显示
						 */
						if (matterDetail.status == null)
						{
							task_status.setImageDrawable(getResources().getDrawable(R.drawable.work_home_task_status));
							return;
						}
						else
						{
							if (matterDetail.status.equals("0"))
							{
								task_status.setImageDrawable(getResources().getDrawable(R.drawable.work_home_task_status));
							}
							if (matterDetail.status.equals("1"))
							{
								task_status.setImageDrawable(getResources().getDrawable(R.drawable.work_home_task_status));
							}
							if (matterDetail.status.equals("2"))
							{
								task_status.setImageDrawable(getResources().getDrawable(R.drawable.work_home_task_status));
							}
							if (matterDetail.status.equals("3"))
							{
								task_status.setImageDrawable(getResources().getDrawable(R.drawable.work_home_task_status));
							}
							if (matterDetail.status.equals("4"))
							{
								task_status.setImageDrawable(getResources().getDrawable(R.drawable.work_home_task_status));
							}
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
			case R.id.work_home_matter_detail_member:// 成员
				Intent intentMember = new Intent(this, MemberActivity.class);
				intentMember.putExtra("theme_id", theme_id);
				startActivity(intentMember);
				break;
			case R.id.work_home_matter_detail_discuss:// 讨论
				Intent intent = new Intent(MatterDetialActivity.this,WorkChatActivity.class);
				intent.putExtra("theme_id", theme_id);
				startActivity(intent);
				break;
			case R.id.work_home_matter_detail_document:// 文档
				Intent intentDocument = new Intent(this, DocumentListActivity.class);
				intentDocument.putExtra("theme_id", theme_id);
				startActivity(intentDocument);
				break;
			case R.id.work_home_matter_detail_mission:// 任务
				Intent intentMission = new Intent(this, MissionListActivity.class);
				intentMission.putExtra("theme_id", theme_id);
				startActivity(intentMission);
				break;
			default:
				break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("requestCode", requestCode + "");
		Log.i("resultCode", resultCode + "");
		if (requestCode == 100)
		{
			if (resultCode == 101 || resultCode == 103)
			{
				getData(theme_id);
			}
			else if (resultCode == 102 || resultCode == 104)
			{
				return;
			}
		}
	}
}
