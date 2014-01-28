package com.mcds.app.android.estar.ui.my;

import java.util.Date;

import android.content.Intent;
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

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.My_GetReminds;
import com.mcds.app.android.estar.bean.My_IntegrationScoreList;
import com.mcds.app.android.estar.bean.My_SystemMessage;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.ConnectProviderCL;
import com.mcds.app.android.estar.ui.BaseTabActivity;

/**
 * 系统消息页面
 * @author Administrator
 *
 */
public class My_SystemMessageActivity extends BaseTabActivity implements OnClickListener{
	private ImageButton my_systemMessage_back;
	private ListView my_systemMessageListView;
	private My_SystemMessageAdapter adapter;
	private ReturnResult<My_SystemMessage> rrSystemMessage;
	private TextView my_systemMessage_title,my_systemMessage_content_time,my_systemMessage_content;
	private String[] Date_Hours;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_systemmessage);
		my_systemMessage_back = (ImageButton) findViewById(R.id.my_systemMessage_back);
		my_systemMessageListView = (ListView) findViewById(R.id.my_systemMessageListView);
		my_systemMessage_back.setOnClickListener(this);
		adapter = new My_SystemMessageAdapter();
		my_systemMessageListView.setAdapter(adapter);
		getData();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		Intent intent = new Intent(My_SystemMessageActivity.this,RemindActivity.class);
		startActivity(intent);
		
	}
	private void getData() {
		new Thread(new Runnable() {
 
			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					rrSystemMessage = ConnectProviderCL.getSystemMessageList(UserManager.uid, "6", "0", "10");
					
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
				if (rrSystemMessage.status.equals("0")) {
					adapter.setItems(rrSystemMessage.getDatas());
					adapter.notifyDataSetChanged();
			
					
				}
				
			}
		});
	}
	
	String lastDate = "";
	private class My_SystemMessageAdapter extends BaseListAdapter<My_SystemMessage> {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;

			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.my_systemmessage_item, null);

				holder = new ViewHolder();
				holder.my_systemMessage_content_date = (TextView) convertView.findViewById(R.id.my_systemMessage_content_date);
				holder.my_systemMessage_title = (TextView) convertView.findViewById(R.id.my_systemMessage_title);
				holder.my_systemMessage_content_hour = (TextView) convertView.findViewById(R.id.my_systemMessage_content_hour);
				holder.my_systemMessage_content = (TextView) convertView.findViewById(R.id.my_systemMessage_content);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (holder != null) {
				 My_SystemMessage item = this.getItem(position);
				 Date_Hours = (((String) DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date(Long.parseLong(item.get_time)))).split(" "));
//				 holder.my_systemMessage_content_hour.setText(DateFormat.format("yyyy-MM-dd hh:mm:ss", item.get_time));
				 if (!lastDate.equals(Date_Hours[0].toString())) {
					 holder.my_systemMessage_content_date.setText(Date_Hours[0]);
				 }
				 lastDate = Date_Hours[0].toString();
				 
				 holder.my_systemMessage_content_hour.setText(Date_Hours[1]);
				 holder.my_systemMessage_title.setText(item.classification);
				 holder.my_systemMessage_content.setText(item.content_text);
				
//	
				
				
			}
			return convertView;
		}

	}

	private class ViewHolder {
		
	    TextView my_systemMessage_content_date;
		TextView my_systemMessage_title;
		TextView my_systemMessage_content_hour;
		TextView my_systemMessage_content;
	}
	
}
