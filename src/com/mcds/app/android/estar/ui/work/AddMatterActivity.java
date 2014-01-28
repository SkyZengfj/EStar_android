package com.mcds.app.android.estar.ui.work;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.MatterDetail;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.WorkMemberListAdapterItem;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.ui.BaseActivity;

/**
 * 新建事项
 * 
 * @author TangChao
 */
public class AddMatterActivity extends BaseActivity implements OnClickListener
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
	 * 负责人ID
	 */
	private String addMatterLeaderId;
	/**
	 * 选中人员id集合
	 */
	private ArrayList<String> addMatterMemberIdArrayList;
	
	/**
	 * 成员Name集合
	 */
	private ArrayList<String> addMatterMemberuserNameArrayList;
	/**
	 * 取消按钮
	 */
	private ImageButton addMatterCancelImageBtn;
	/**
	 * 保存按钮
	 */
	private ImageButton addMatterSaveImageBtn;
	/**
	 * 事项名称输入框
	 */
	private EditText add_matter_name_Eedit;
	/**
	 * 事项说明输入框
	 */
	private EditText add_matter_explain_edit;
	/**
	 * 事项名称
	 */
	private String addMatterName;
	/**
	 * 事项说明
	 */
	private String addMatterExplain;
	/**
	 * 负责人编辑按钮
	 */
	private TextView add_matter_leader_btn;
	/**
	 * 成员编辑按钮
	 */
	private TextView add_matter_member_btn;
	/**
	 * 成员ID
	 */
	private String addMatterMemberIdChar;
	/**
	 * 截止日期编辑按钮
	 */
	private TextView add_matter_end_time_btn;
	/**
	 * 截止日期
	 */
	private String addMatterEndTime;
	/**
	 * 请求返回的对象
	 */
	private ReturnResult<String> rrWork;
	ReturnResult<String> rrResult;
	/**
	 * 上一个Activity传过来的可序列化对象
	 */
	private MatterDetail matterDetail = null;
	
	private Spinner work_home_add_matter_spinner;
	
	//标题
	private TextView work_home_mission_detail_title_name;
	
	private String [] spinnerData = {"暂停","进行中","完成"};
	private ArrayAdapter<String> adapter;
	private int spinnerStatus = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.work_home_add_matter);
		Intent intent = getIntent();
		Tag = intent.getStringExtra("TAG");
		initWidget();
		
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinnerData);
		work_home_add_matter_spinner.setAdapter(adapter);
		//添加事件Spinner事件监听 
		work_home_add_matter_spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
		
		
		// 编辑页面
		if (Tag.equals("MY_MATTER_DIALOG"))
		{
			work_home_mission_detail_title_name.setText("编辑事项");
			findViewById(R.id.work_home_add_matter_leaderuser).setVisibility(View.GONE);
			findViewById(R.id.work_home_add_matter_memberuser).setVisibility(View.GONE);
			findViewById(R.id.work_home_add_matter_spinnerlayout).setVisibility(View.VISIBLE);
			theme_id = intent.getStringExtra("theme_id");
			matterDetail = (MatterDetail) intent.getSerializableExtra("SerializableObject");
			work_home_add_matter_spinner.setSelection(Integer.parseInt(matterDetail.status),true);
			addMatterLeaderId = matterDetail.leader;
			addMatterMemberIdChar = matterDetail.member_num;
			addData();
		}
	}
	
	class SpinnerSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
//			Toast.makeText(AddMatterActivity.this, "你选择的是:"+arg2, Toast.LENGTH_SHORT).show();
			if (matterDetail != null&&!matterDetail.status.equals("2")) {
				spinnerStatus = arg2;
			}else {
				Toast.makeText(AddMatterActivity.this, "已完成事项不能修改状态", Toast.LENGTH_SHORT).show();
			}
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	
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
				addMatterLeaderId = addMatterMemberIdArrayList.get(0);
				add_matter_leader_btn.setText(addMatterMemberuserNameArrayList.get(0));
			}
			else if (resultCode == 102)// 失败
			{
				Toast.makeText(AddMatterActivity.this, "指定失败,请重试", Toast.LENGTH_SHORT).show();
			}
		}
		else if (requestCode == 200)// 指定成员
		{
			if (resultCode == 201)// 成功
			{
				addMatterMemberIdArrayList = data.getStringArrayListExtra("atUserID");
				String str = addMatterMemberIdArrayList.toString();
				addMatterMemberIdChar = str.substring(1, str.length()-1);
				addMatterMemberuserNameArrayList = data.getStringArrayListExtra("userName");
				String ss = addMatterMemberuserNameArrayList.toString();
				/**
				 * 显示到按钮上
				 */
				add_matter_member_btn.setText(ss.substring(1, ss.length()-1));
			}
			else if (resultCode == 202)// 失败
			{
				Toast.makeText(AddMatterActivity.this, "添加失败,请重试", Toast.LENGTH_SHORT).show();
			}
		}

	}
	@Override
	protected void onPause()
	{
		super.onPause();
//		finish();
	}
	/**
	 * 初始化控件
	 */
	private void initWidget()
	{
		work_home_add_matter_spinner = (Spinner)findViewById(R.id.work_home_add_matter_spinner);
		work_home_mission_detail_title_name = (TextView)findViewById(R.id.work_home_mission_detail_title_name);
		addMatterCancelImageBtn = (ImageButton) findViewById(R.id.work_home_add_matter_cancel);
		addMatterSaveImageBtn = (ImageButton) findViewById(R.id.work_home_add_matter_save);
		add_matter_name_Eedit = (EditText) findViewById(R.id.matter_input_editText0);
		add_matter_explain_edit = (EditText) findViewById(R.id.matter_input_editText1);
		add_matter_leader_btn = (TextView) findViewById(R.id.work_home_add_matter_leader_btn);
		add_matter_member_btn = (TextView) findViewById(R.id.work_home_add_matter_member_btn);
		add_matter_end_time_btn = (TextView) findViewById(R.id.work_home_add_matter_end_time_btn);
		addMatterCancelImageBtn.setOnClickListener(this);
		addMatterSaveImageBtn.setOnClickListener(this);
		add_matter_leader_btn.setOnClickListener(this);
		add_matter_member_btn.setOnClickListener(this);
		add_matter_end_time_btn.setOnClickListener(this);
	}
	/**
	 * 添加数据
	 */
	private void addData()
	{
		add_matter_name_Eedit.setText(matterDetail.name);
		add_matter_explain_edit.setText(matterDetail.content);
		add_matter_leader_btn.setText(matterDetail.leader);
		add_matter_member_btn.setText(matterDetail.member_num);
		add_matter_end_time_btn.setText(matterDetail.time_end);
	}
	/**
	 * 检测输入框
	 */
	private void checkEditText()
	{
		addMatterName = add_matter_name_Eedit.getText().toString();
		addMatterExplain = add_matter_explain_edit.getText().toString();
		addMatterEndTime = add_matter_end_time_btn.getText().toString();
//		addMatterLeaderId = add_matter_end_time_btn.getText().toString();//负责人ID
//		addMatterMemberIdChar = (String[]) addMatterMemberIdArrayList.toArray();
		if (addMatterName.equals(""))
		{
			Toast.makeText(getApplicationContext(), "事项名称不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		if (addMatterExplain.equals(""))
		{
			Toast.makeText(getApplicationContext(), "事项说明不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		if (addMatterLeaderId.equals("未指定")||"".equals(addMatterLeaderId))
		{
			Toast.makeText(getApplicationContext(), "负责人不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		if (addMatterMemberIdChar.equals("未指定") || "".equals(addMatterMemberIdChar))
		{
			Toast.makeText(getApplicationContext(), "成员不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		if (addMatterEndTime.equals("未指定"))
		{
			Toast.makeText(getApplicationContext(), "截止日期不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		setData(theme_id, addMatterName, addMatterExplain, addMatterLeaderId, addMatterMemberIdChar, addMatterEndTime);
	}
	/**
	 * 请求接口 提交数据
	 * 
	 * @param name
	 * @param content
	 * @param leader_id
	 * @param members_id
	 * @param time_end
	 */
	private void setData(final String theme_id, final String name, final String content, final String leader_id,
			final String members_id, final String time_end)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				showWaitingDialog();
				if (!UserManager.uid.equals(""))
				{
					if (Tag.equals("HOME_ACTIVITY"))// 新建
					{
						Log.i("stye", "NEW");
						rrWork = ConnectProvider.addMatter(UserManager.uid, name, content, leader_id, members_id, time_end);
					}
					else if (Tag.equals("MY_MATTER_DIALOG"))// 编辑
					{
						Log.i("style", "EDIT");
						rrWork = ConnectProvider.editMatter(UserManager.uid, theme_id, name, content, time_end, "u01", "u01");
					}
					Log.i("NAME", name);
					Log.i("CONTENT", content);
					Log.i("LEADER_ID", leader_id);
					Log.i("MEMBERS_ID", members_id.toString());
					Log.i("TIME_END", time_end);
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
				if (rrWork.status.equals("0"))
				{
					Toast.makeText(AddMatterActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(AddMatterActivity.this, HomeActivity.class);
					startActivity(intent);
				}
			}
		});
	}
	@Override
	public void onClick(View arg0)
	{
		switch (arg0.getId())
		{
			case R.id.work_home_add_matter_leader_btn:// 指定负责人
				Intent intentLeader = new Intent(this, WorkMemberListActivity.class);
				intentLeader.putExtra("usertype", 0);
				startActivityForResult(intentLeader, 100);
				break;
			case R.id.work_home_add_matter_member_btn:// 指定成员
				Intent intentMember = new Intent(this, WorkMemberListActivity.class);
				intentMember.putExtra("usertype", 1);
				startActivityForResult(intentMember, 200);
				break;
			case R.id.work_home_add_matter_end_time_btn:// 指定截止日期
				// 日期对象
				final Time time = new Time();
				time.setToNow();
				// 选择日期的对话框
				DatePickerDialog dpd = new DatePickerDialog(this, new OnDateSetListener()
				{
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
					{
						// 格式化
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
						Date date = new Date(year - 1900, monthOfYear, dayOfMonth, time.hour, time.minute, time.second);
						Log.i("Date", sdf.format(date));
						add_matter_end_time_btn.setText(sdf.format(date));
						addMatterEndTime = sdf.format(date);
					}
				}, time.year, time.month, time.monthDay);
				dpd.show();
				break;
			case R.id.work_home_add_matter_cancel:// 返回
				finish();
				break;
			case R.id.work_home_add_matter_save:// 保存并请求提交数据
				checkEditText();
				break;
			default:
				break;
		}
	}
}
