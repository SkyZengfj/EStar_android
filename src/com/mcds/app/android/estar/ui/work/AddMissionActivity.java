package com.mcds.app.android.estar.ui.work;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.Layout;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.MissionDetail;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProviderTC;
import com.mcds.app.android.estar.ui.BaseActivity;

/**
 * 新建任务
 * 
 * @author TangChao
 */
public class AddMissionActivity extends BaseActivity implements OnClickListener
{
	/**
	 * Tag
	 */
	private static String Tag;
	/**
	 * theme_id
	 */
	private String theme_id;
	/**
	 * mission_id
	 */
	private String mission_id;
	/**
	 * 取消按钮
	 */
	private ImageButton addMissionCancelImageBtn;
	/**
	 * 保存按钮
	 */
	private ImageButton addMissionSaveImageBtn;
	/**
	 * 任务名称输入框
	 */
	private EditText add_mission_name_edit;
	/**
	 * 任务内容输入框
	 */
	private EditText add_mission_content_edit;
	/**
	 * 任务名称
	 */
	private String addMissionName;
	/**
	 * 任务内容
	 */
	private String addMissionContent;
	/**
	 * 负责人编辑按钮
	 */
	private TextView add_mission_leader_btn;
	/**
	 * 成员编辑按钮
	 */
	private TextView add_mission_member_btn;
	/**
	 * 截止日期编辑按钮
	 */
	private TextView add_mission_end_time_btn;
	/**
	 * 截止日期
	 */
	private String addMissionEndTime;
	/**
	 * 请求返回的对象
	 */
	private ReturnResult<String> rrMission;
	/**
	 * 序列化对象
	 */
	private MissionDetail missionDetail;
	//负责人ID
	private String leader_Id;
	//成员id
	private String member_id;
	//选中人员id集合
	private ArrayList<String> addMatterMemberIdArrayList;
	//成员Name集合
	private ArrayList<String> addMatterMemberuserNameArrayList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.work_home_add_mission);
		Intent intent = getIntent();
		theme_id = intent.getStringExtra("theme_id");
		Log.i("theme_id", theme_id);
		Tag = intent.getStringExtra("TAG");
		Log.i("TAG", Tag);
		initWidget();
		// 编辑页面
		if (Tag.equals("MY_MISSION_DIALOG"))
		{
			((TextView)findViewById(R.id.work_home_add_mission_title_name)).setText("编辑任务");
//			((LinearLayout)findViewById(R.id.work_home_add_mission_leader_layout)).setVisibility(View.GONE);
//			((LinearLayout)findViewById(R.id.work_home_add_mission_member_layout)).setVisibility(View.GONE);
			missionDetail = (MissionDetail) intent.getSerializableExtra("SerializableObject");
			mission_id = intent.getStringExtra("mission_id");
			leader_Id = missionDetail.leader;
			member_id = missionDetail.members;
			addData();
		}
	}
	/**
	 * 初始化控件
	 */
	private void initWidget()
	{
		addMissionCancelImageBtn = (ImageButton) findViewById(R.id.work_home_add_mission_cancel);
		addMissionSaveImageBtn = (ImageButton) findViewById(R.id.work_home_add_mission_save);
		add_mission_name_edit = (EditText) findViewById(R.id.mission_input_editText0);
		add_mission_content_edit = (EditText) findViewById(R.id.mission_input_editText1);
		add_mission_leader_btn = (TextView) findViewById(R.id.work_home_add_mission_leader_btn);
		add_mission_member_btn = (TextView) findViewById(R.id.work_home_add_mission_member_btn);
		add_mission_end_time_btn = (TextView) findViewById(R.id.work_home_add_mission_end_time_btn);
		addMissionCancelImageBtn.setOnClickListener(this);
		addMissionSaveImageBtn.setOnClickListener(this);
		add_mission_leader_btn.setOnClickListener(this);
		add_mission_member_btn.setOnClickListener(this);
		add_mission_end_time_btn.setOnClickListener(this);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100)// 指定负责人
		{
			if (resultCode == 101)// 成功
			{
				addMatterMemberIdArrayList = data.getStringArrayListExtra("atUserID");
				addMatterMemberuserNameArrayList = data.getStringArrayListExtra("userName");
				System.out.println(addMatterMemberIdArrayList.toString()+"==============="+addMatterMemberuserNameArrayList);
				leader_Id = addMatterMemberIdArrayList.get(0);
				add_mission_leader_btn.setText(addMatterMemberuserNameArrayList.get(0));
			}
			else if (resultCode == 102)// 失败
			{
				Toast.makeText(AddMissionActivity.this, "指定失败,请重试", Toast.LENGTH_SHORT).show();
			}
		}
		else if (requestCode == 200)// 指定成员
		{
			if (resultCode == 201)// 成功
			{
				addMatterMemberIdArrayList = data.getStringArrayListExtra("atUserID");
				String str = addMatterMemberIdArrayList.toString();
				member_id = str.substring(1, str.length()-1);
				addMatterMemberuserNameArrayList = data.getStringArrayListExtra("userName");
				String ss = addMatterMemberuserNameArrayList.toString();
				/**
				 * 显示到按钮上
				 */
				add_mission_member_btn.setText(ss.substring(1, ss.length()-1));
			}
			else if (resultCode == 202)// 失败
			{
				Toast.makeText(AddMissionActivity.this, "添加失败,请重试", Toast.LENGTH_SHORT).show();
			}
		}

	}
	
	/**
	 * 添加数据
	 */
	private void addData()
	{
		add_mission_name_edit.setText(missionDetail.name);
		add_mission_content_edit.setText(missionDetail.content);
		add_mission_leader_btn.setText(missionDetail.leader);
		add_mission_member_btn.setText(missionDetail.members);
		add_mission_end_time_btn.setText(missionDetail.end_time);
	}
	/**
	 * 检测输入框
	 */
	private void checkEditText()
	{
		addMissionName = add_mission_name_edit.getText().toString();
//		addMissionContent = add_mission_content_edit.getText().toString();
		addMissionEndTime = add_mission_end_time_btn.getText().toString();
//		leader_Id = add_mission_leader_btn.getText().toString();
//		member_id = add_mission_member_btn.getText().toString();
		if ("".equals(addMissionName))
		{
			Toast.makeText(getApplicationContext(), "任务名称不能为空", Toast.LENGTH_LONG).show();
			return;
		}
//		if ("".equals(addMissionContent))
//		{
//			Toast.makeText(getApplicationContext(), "任务内容不能为空", Toast.LENGTH_LONG).show();
//			return;
//		}
		if ("未指定".equals(leader_Id)||"".equals(leader_Id))
		{
			Toast.makeText(getApplicationContext(), "负责人不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		if ("未指定".equals(member_id)||"".equals(member_id))
		{
			Toast.makeText(getApplicationContext(), "成员不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		if (addMissionEndTime.equals("未指定")||"".equals(addMissionEndTime))
		{
			Toast.makeText(getApplicationContext(), "截止时间不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		setData(theme_id, mission_id, addMissionName, addMissionContent, addMissionEndTime, leader_Id, member_id);
	}
	/**
	 * 请求接口 提交数据
	 * 
	 * @param theme_id
	 * @param name
	 * @param content
	 * @param end_time
	 * @param leader
	 * @param members_id
	 */
	private void setData(final String theme_id, final String mission_id, final String name, final String content,
			final String end_time, final String leader, final String members_id)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				showWaitingDialog();
				if (!"".equals(UserManager.uid))
				{
					if (Tag.equals("MISSION_LIST"))// 新建
					{
						Log.i("style------>", "NEW");
						rrMission = ConnectProviderTC.addMission(UserManager.uid, theme_id, name, content, end_time, leader,
								members_id);
					}
					else if (Tag.equals("MY_MISSION_DIALOG"))// 编辑
					{
						Log.i("style------->", "EDIT");
						rrMission = ConnectProviderTC.editMission(UserManager.uid, mission_id, name, content, end_time, leader,
								members_id);
					}
					Log.i("UID", UserManager.uid);
					Log.i("NAME", name);
					Log.i("CONTENT", content);
					Log.i("END_TIME", end_time);
					Log.i("LEADER", leader);
					Log.i("MEMBERS_ID", members_id);
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
				Log.i("status", rrMission.status);
				if (rrMission.status.equals("0"))
				{
					Toast.makeText(AddMissionActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(AddMissionActivity.this, MissionListActivity.class);
					intent.putExtra("theme_id", theme_id);
					startActivity(intent);
				}
			}
		});
	}
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.work_home_add_mission_leader_btn:// 指定负责人
				Intent intentLeader = new Intent(this, WorkMemberListActivity.class);
				intentLeader.putExtra("usertype", 0);
				startActivityForResult(intentLeader, 100);
				break;
			case R.id.work_home_add_mission_member_btn:// 指定成员
//				Intent intent = new Intent(AddMissionActivity.this, MemberActivity.class);
//				startActivity(intent);
				Intent intentMember = new Intent(this, WorkMemberListActivity.class);
				intentMember.putExtra("usertype", 1);
				startActivityForResult(intentMember, 200);
				break;
			case R.id.work_home_add_mission_end_time_btn:// 指定截止日期
				final Time time = new Time();
				time.setToNow();
				DatePickerDialog dpd = new DatePickerDialog(this, new OnDateSetListener()
				{
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
					{
						Date date = new Date(year - 1900, monthOfYear, dayOfMonth,time.hour,time.minute,time.second);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
						add_mission_end_time_btn.setText(sdf.format(date));
						addMissionEndTime = sdf.format(date);
					}
				}, time.year, time.month, time.monthDay);
				dpd.show();
				break;
			case R.id.work_home_add_mission_cancel:// 返回
				finish();
				break;
			case R.id.work_home_add_mission_save:// 保存并请求提交数据
				checkEditText();
				break;
			default:
				break;
		}
	}
}
