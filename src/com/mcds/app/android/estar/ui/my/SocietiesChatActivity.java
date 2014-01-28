package com.mcds.app.android.estar.ui.my;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.SocietiesChatAdapter;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.SocietiesChatReceive;
import com.mcds.app.android.estar.bean.SocietiesChatSend;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProviderTC;
import com.mcds.app.android.estar.ui.BaseTabActivity;
import com.zvezda.data.utils.DataSP;

/**
 * 社团聊天群
 * 
 * @author TangChao
 */
public class SocietiesChatActivity extends BaseTabActivity implements OnClickListener
{
	private String club_id;
	private ImageButton cancelBtn;
	private ImageButton infoBtn;
	private TextView sendBtn;
	private ReturnResult<SocietiesChatReceive> rrGetMessagr;
	private long timestamp;
	private ArrayList<Object> arrayList;
	private ListView listView;
	private SocietiesChatAdapter adapter;
	private List<SocietiesChatSend> listSend;
	private List<SocietiesChatReceive> listReceive;
	private EditText editText;
	private String inputContent;
	private SocietiesChatSend societiesChatSend;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.my_societies_chat);
		cancelBtn = (ImageButton) findViewById(R.id.societies_chat_cancel_btn);
		infoBtn = (ImageButton) findViewById(R.id.societies_chat_info_btn);
		sendBtn = (TextView) findViewById(R.id.societies_chat_send);
		listView = (ListView) findViewById(R.id.my_societies_chat_list_view);
		editText = (EditText) findViewById(R.id.societies_chat_edit);
		cancelBtn.setOnClickListener(this);
		infoBtn.setOnClickListener(this);
		sendBtn.setOnClickListener(this);
		club_id = getIntent().getStringExtra("club_id");
		Log.i("club_id------>", club_id);
		handler.post(myRunnable);
		arrayList = new ArrayList<Object>();
		adapter = new SocietiesChatAdapter(getApplicationContext(), arrayList);
		listView.setAdapter(adapter);
		listSend = new ArrayList<SocietiesChatSend>();
		listReceive = new ArrayList<SocietiesChatReceive>();
	}
	@Override
	protected void onPause()
	{
		super.onPause();
		handler.removeCallbacks(myRunnable);
	}
	
	Handler handler = new Handler();
	Runnable myRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			handler.postDelayed(this, 10000);
//			该处用于标记  注释即可
			String timeLocal = new DataSP(SocietiesChatActivity.this, "TIMESTAMP_XML").getSValue("TIMESTAMP", "197001010000");
			timestamp = Long.parseLong(timeLocal);
			getClubNewMessage(club_id, timestamp);
		}
	};
	
	/**
	 * 接收社团消息列表
	 */
	private void getClubNewMessage(final String club_id, final long timestamp)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				if (!UserManager.uid.equals(""))
				{
					rrGetMessagr = ConnectProviderTC.getClubNewMessage(UserManager.uid, club_id, timestamp);
					listReceive = rrGetMessagr.getDatas();
					doUI(listReceive);
				}
			}
		}).start();
	}
	/**
	 * 发送社团消息
	 */
	private void setClubNewMessage(final String club_id, final String content)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				if (!UserManager.uid.equals(""))
				{
					societiesChatSend = ConnectProviderTC.setClubNewMessage(UserManager.uid, club_id, content);
					listSend.add(societiesChatSend);
					doUI(listSend);
				}
			}
		}).start();
	}
	/**
	 * 刷新界面
	 */
	private void doUI(final List<?> list)
	{
		this.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				arrayList.addAll(list);
				adapter.notifyDataSetChanged();
			}
		});
	}
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.societies_chat_cancel_btn:// 取消
				finish();
				break;
			case R.id.societies_chat_info_btn:// 查看社团信息
				Intent intent = new Intent(this, SocietiesInfoActivity.class);
				intent.putExtra("club_id", club_id);
				startActivity(intent);
				break;
			case R.id.societies_chat_send:// 发送消息
				inputContent = editText.getText().toString();
				if (inputContent.equals(""))
				{
					Toast.makeText(getApplicationContext(), "请输入发送内容", Toast.LENGTH_SHORT).show();
					return;
				}
				setClubNewMessage(club_id, inputContent);
				/**
				 * 发送成功后,清除编辑框
				 */
				// if (workChatSend.status.equals("0"))
				// {
				editText.setText("");
				inputContent = "";
				// }
				break;
			default:
				break;
		}
	}
}
