package com.mcds.app.android.estar.ui.my;

import java.util.Date;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.MyFans;
import com.mcds.app.android.estar.bean.My_IntegrationScoreList;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.provider.ConnectProviderCL;
import com.mcds.app.android.estar.ui.BaseTabActivity;

/**
 * 积分
 * @author chenliang
 *
 */
public class IntegrationActivity extends BaseTabActivity implements OnClickListener{
	private ImageButton my_document_integration_back;
	private int page_num = 0;
	private final String PAGE_SIZE = "10";
	private ListView my_intergrationListView;
	private MyIntegrationAdapter adapter;
	private ReturnResult<My_IntegrationScoreList> rrIntegration;
	private TextView my_integration_score_totle,my_integration_access;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_integration);
		my_document_integration_back = (ImageButton) findViewById(R.id.my_document_integration_back);
		my_document_integration_back.setOnClickListener(this);
		my_integration_score_totle =(TextView) findViewById(R.id.my_integration_score_totle);
		my_integration_access = (TextView) findViewById(R.id.my_integration_access);
		my_intergrationListView = (ListView) findViewById(R.id.my_integrationListView);
		
		adapter = new MyIntegrationAdapter();
		my_intergrationListView.setAdapter(adapter);
		getData();
	}

	@Override 
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(IntegrationActivity.this,HomeActivity.class);
		startActivity(intent);
		
	}
	
	private void getData() {
		new Thread(new Runnable() {
 
			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					rrIntegration = ConnectProviderCL.getScoreList(UserManager.uid, "0", "10");
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
				if (rrIntegration.status.equals("0")) {
					adapter.setItems(rrIntegration.getDatas());
					adapter.notifyDataSetChanged();
				}
			}
		});
	}
	private class MyIntegrationAdapter extends BaseListAdapter<My_IntegrationScoreList> {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;

			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.my_integration_item, null);

				holder = new ViewHolder();
				holder.my_integration_headPic = (ImageView) convertView.findViewById(R.id.my_integration_headPic);
				holder.my_integration_title = (TextView) convertView.findViewById(R.id.my_integration_title);
				holder.my_integration_expend = (TextView) convertView.findViewById(R.id.my_integration_expend);
				holder.my_integration_time = (TextView) convertView.findViewById(R.id.my_integration_time);
				holder.my_integration_date = (TextView) convertView.findViewById(R.id.my_integration_date);
				holder.my_integration_score_type = (TextView) convertView.findViewById(R.id.my_integration_score_type);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (holder != null) {
				 My_IntegrationScoreList item = this.getItem(position);
				holder.my_integration_title.setText(item.score_content);
				if (item.score_type.equals("1")) {
					holder.my_integration_expend.setText("-"+item.score_num);
					holder.my_integration_headPic.setBackgroundResource(R.drawable.my_icon_purse_minus);
					
				}if (item.score_type.equals("0")) {
					holder.my_integration_expend.setText("+"+item.score_num);
					holder.my_integration_headPic.setBackgroundResource(R.drawable.my_icon_purse_add);
					
				}
				String setTime = "";
				setTime = (String) DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date(Long.parseLong(item.score_time)));
				
				holder.my_integration_time.setText(setTime);
				if (item.score_type.equals("1")) {
					holder.my_integration_score_type.setText("支出");
				}else{
					holder.my_integration_score_type.setText("获得");
				}
				
				
				my_integration_score_totle.setText("累计："+My_IntegrationScoreList.my_score_totle);
				my_integration_access.setText("可用："+My_IntegrationScoreList.my_score_access);
				
			}
			return convertView;
		}

	}

	private class ViewHolder {
		
		ImageView my_integration_headPic;
		TextView my_integration_title;
		TextView my_integration_expend;
		TextView my_integration_time;
		TextView my_integration_date;
		TextView my_integration_score_type;
	}
}
