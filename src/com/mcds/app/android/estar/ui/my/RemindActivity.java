package com.mcds.app.android.estar.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.MyFans;
import com.mcds.app.android.estar.bean.My_GetReminds;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.ui.BaseTabActivity;
/**
 * 提醒页面
 * @author chenliang
 *
 */

public class RemindActivity extends BaseTabActivity implements OnClickListener{
	private LinearLayout my_btn_remind_at,my_btn_remind_commit,my_btn_remind_activity,my_btn_remind_system;
	private TextView my_txt_remind_atNum,my_txt_remind_commitNum,my_txt_remind_activityNum,my_txt_remind_systemNum;
	private ReturnResult<My_GetReminds> rrRemind;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_remind);
		my_btn_remind_at = (LinearLayout) findViewById(R.id.my_btn_remind_at);
		my_btn_remind_at.setOnClickListener(this);
		my_btn_remind_commit =(LinearLayout) findViewById(R.id.my_btn_remind_commit);
		my_btn_remind_commit.setOnClickListener(this);
		my_btn_remind_activity = (LinearLayout) findViewById(R.id.my_btn_remind_activity);
		my_btn_remind_activity.setOnClickListener(this);
		my_btn_remind_system = (LinearLayout) findViewById(R.id.my_btn_remind_system);
		my_btn_remind_system.setOnClickListener(this);
		
		my_txt_remind_atNum = (TextView) findViewById(R.id.my_txt_remind_atNum);
		my_txt_remind_commitNum = (TextView) findViewById(R.id.my_txt_remind_commitNum);
		my_txt_remind_activityNum =(TextView) findViewById(R.id.my_txt_remind_activityNum);
		my_txt_remind_systemNum = (TextView) findViewById(R.id.my_txt_remind_systemNum);
		getData();
		
	}
	private void getData() {
		new Thread(new Runnable() {
 
			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					rrRemind = ConnectProvider.getMyReminds(UserManager.uid);
					
					doListViewUI();
				}

				hideWaitingDialog();
			}
		}).start();
	}
	private void doListViewUI() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				//还要判断一次解决断网退出的异常
				if (rrRemind.status.equals("0")) {
					My_GetReminds info= new My_GetReminds();
					
					info = rrRemind.getDatas().get(0);
					if (!info.at_num.equals("")) {
						 
						my_txt_remind_atNum.setVisibility(View.VISIBLE);
						my_txt_remind_atNum.setText(info.at_num);
						my_txt_remind_commitNum.setVisibility(View.VISIBLE);
						my_txt_remind_commitNum.setText(info.commit_num);
						my_txt_remind_activityNum.setVisibility(View.VISIBLE);
						my_txt_remind_activityNum.setText(info.activity_num);
						my_txt_remind_systemNum.setVisibility(View.VISIBLE);
						my_txt_remind_systemNum.setText(info.system_num);
						
					}else {
						
					}
					
				}
//				my_txt_remind_atNum.setVisibility(View.VISIBLE);
//				my_txt_remind_atNum.setText("2");
//				my_txt_remind_commitNum.setVisibility(View.VISIBLE);
//				my_txt_remind_commitNum.setText("8");
//				my_txt_remind_activityNum.setVisibility(View.VISIBLE);
//				my_txt_remind_activityNum.setText("9");
//				my_txt_remind_systemNum.setVisibility(View.VISIBLE);
//				my_txt_remind_systemNum.setText("12");
				
			}
		});
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.my_btn_remind_at:
			Intent myin = new Intent(getApplicationContext(), MyAtMeActivity.class);
			myin.putExtra("type", 1);//提到我的动态
			startActivity(myin);
			break;
		case R.id.my_btn_remind_commit://评论
			Intent mn = new Intent(getApplicationContext(), CommitActivity.class);
			startActivity(mn);
			break;
		case R.id.my_btn_remind_activity:
			Intent intentActivity = new Intent (RemindActivity.this,MyActivityNotify.class);
			startActivity(intentActivity);
			
			break;
		case R.id.my_btn_remind_system:
			Intent intent = new Intent (RemindActivity.this,My_SystemMessageActivity.class);
			startActivity(intent);
			
			break;
		default:
			break;
		}
	}
}
