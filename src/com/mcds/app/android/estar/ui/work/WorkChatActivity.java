package com.mcds.app.android.estar.ui.work;

import java.util.ArrayList;
import java.util.List;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.WorkChatAdapter;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.WorkChatReceive;
import com.mcds.app.android.estar.bean.WorkChatSend;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProviderTC;
import com.mcds.app.android.estar.ui.BaseActivity;

public class WorkChatActivity extends BaseActivity implements OnClickListener
{
	/**
	 * theme_id
	 */
	private String theme_id;
	/**
	 * 取消按钮
	 */
	private ImageButton cancelBtnButton;
	/**
	 * 发送按钮
	 */
	private TextView sendBtn;
	/**
	 * 获取主题消息的响应
	 */
	private ReturnResult<WorkChatReceive> rrReceiveMatterChat;
	/**
	 * 发送主题消息的响应
	 */
	// private ReturnResult<WorkChatSend> rrSendMatterChat;
	/**
	 * 发送消息的时间戳
	 */
	private long timestamp;
	/**
	 * 聊天内容输入框
	 */
	private EditText editText;
	/**
	 * 输入的内容
	 */
	private String inputContent;
	/**
	 * 用于装载发送和接收到的数据
	 */
	private ArrayList<Object> arrayList;
	/**
	 * 发送内容对象
	 */
	private WorkChatSend workChatSend;
	/**
	 * 适配器
	 */
	private WorkChatAdapter adapter;
	/**
	 * 聊天窗口
	 */
	private ListView listView;
	private List<WorkChatSend> listSend;
	private List<WorkChatReceive> listReceive;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.work_chat);
		/*--------------------------------------------------------------------------*/
		cancelBtnButton = (ImageButton) findViewById(R.id.work_chat_cancel_btn);
		sendBtn = (TextView) findViewById(R.id.work_chat_send);
		editText = (EditText) findViewById(R.id.work_chat_edit);
		listView = (ListView) findViewById(R.id.work_chat_list_view);
		cancelBtnButton.setOnClickListener(this);
		sendBtn.setOnClickListener(this);
		/*---------------------------------------------*/
		arrayList = new ArrayList<Object>();
		adapter = new WorkChatAdapter(getApplicationContext(), arrayList);
		listView.setAdapter(adapter);
		theme_id = getIntent().getStringExtra("theme_id");
		handler.post(myRunnable);
		listSend = new ArrayList<WorkChatSend>();
		listReceive = new ArrayList<WorkChatReceive>();
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
			getMaterChat(theme_id, timestamp);
		}
	};
	
	/**
	 * 接收消息
	 */
	private void getMaterChat(final String theme_id, final long timestamp)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				if (!UserManager.uid.equals(""))
				{
					rrReceiveMatterChat = ConnectProviderTC.getMatterChat(UserManager.uid, theme_id, timestamp);
					listReceive = rrReceiveMatterChat.getDatas();
					doUI(listReceive);
				}
			}
		}).start();
	}
	/**
	 * 发送消息
	 */
	private void setMatterChat(final String theme_id, final String content)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				if (!UserManager.uid.equals(""))
				{
					workChatSend = ConnectProviderTC.setMatterChat(UserManager.uid, theme_id, content);
					listSend.add(workChatSend);
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
			case R.id.work_chat_cancel_btn:// 取消
				finish();
				break;
			case R.id.work_chat_send:// 发送
				if (theme_id != null)
				{
					inputContent = editText.getText().toString();
					if (inputContent.equals(""))
					{
						Toast.makeText(getApplicationContext(), "请输入发送内容", Toast.LENGTH_SHORT).show();
						return;
					}
					setMatterChat(theme_id, inputContent);
					/**
					 * 发送成功后,清楚编辑框
					 */
					// if (workChatSend.status.equals("0"))
					// {
					editText.setText("");
					inputContent = "";
					// }
				}
				break;
			default:
				break;
		}
	}
}
