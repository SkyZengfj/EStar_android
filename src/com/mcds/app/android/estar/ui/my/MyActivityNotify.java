package com.mcds.app.android.estar.ui.my;

import java.util.Date;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.My_SystemMessage;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProviderCL;
import com.mcds.app.android.estar.ui.BaseActivity;

/**
 * 
 * @author Administrator
 *
 */
public class MyActivityNotify extends BaseActivity{

	private ListView my_activity_notify_listview;
	private MyActivityNotifyAdapter mna;
	private ReturnResult<My_SystemMessage> mnls;
	private String lastDate = "";
	private String[] Date_Hours;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_activity_notify);
		my_activity_notify_listview = (ListView) findViewById(R.id.my_activity_notify_listview);
		
		
		
		mna = new MyActivityNotifyAdapter();
		my_activity_notify_listview.setAdapter(mna);
		
		getData();
	}
	
	
	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				if (!"".equals(UserManager.uid)&& UserManager.uid!=null) {
					mnls = ConnectProviderCL.getSystemMessageList(UserManager.uid, "4", "0", "10");
					doUI();
				}

				hideWaitingDialog();
			}
		}).start();
	}

	private void doUI() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (mnls != null) {
					if (mnls.status.equals("0")) {
						mna.setItems(mnls.getDatas());
						mna.notifyDataSetChanged();
					} else {
						Toast.makeText(getApplicationContext(), mnls.info, Toast.LENGTH_LONG).show();
					}
				}
			}
		});
	}
	
	private class MyActivityNotifyAdapter extends BaseListAdapter<My_SystemMessage> {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.my_activity_notify_item, null);
			My_SystemMessage item = this.getItem(position);
			Date_Hours = (((String) DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date(Long.parseLong(item.get_time)))).split(" "));
			((TextView) convertView.findViewById(R.id.my_activity_notify_item_hours)).setText(Date_Hours[1]);
			((TextView) convertView.findViewById(R.id.my_activity_notify_item_content)).setText(item.content_text);
			if (!lastDate.equals(Date_Hours[0])) {
				((TextView) convertView.findViewById(R.id.my_activity_notify_item_date)).setText(Date_Hours[1]);
				((LinearLayout)findViewById(R.id.my_activity_notify_item_timelayout)).setVisibility(View.VISIBLE);
				
			}else {
				lastDate = Date_Hours[1];
				((LinearLayout)findViewById(R.id.my_activity_notify_item_timelayout)).setVisibility(View.INVISIBLE);
			}
			return convertView;
		}
	}
	
}

