package com.mcds.app.android.estar.ui.my;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.My_SystemMessage;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.provider.ConnectProviderCL;
import com.mcds.app.android.estar.ui.BaseTabActivity;
import com.mcds.app.android.estar.util.Utility;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class MyWeiboPraisedActivity extends BaseTabActivity {

	private TextView my_weibo_praised_layout_title;
	private ImageButton my_weibo_praised_back_button;
	private ListView my_weibo_praised_layout_listView;
	private MyWeiboPraisedAdapter mwpadapter;
	private ReturnResult<My_SystemMessage> my_SystemMessages;
	private int page_num = 0;
	private final String PAGE_SIZE = "10";
	int type = 2 ;
	private String uid = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_weibo_praised);
		Intent in = getIntent();
		type = in.getIntExtra("type", type);
		uid = in.getStringExtra("uid");
		if ("".equals(uid)||uid==null) {
			uid = UserManager.uid;
		}
		my_weibo_praised_layout_title = (TextView) findViewById(R.id.my_weibo_praised_layout_title);
		my_weibo_praised_back_button = (ImageButton) findViewById(R.id.my_weibo_praised_back_button);
		my_weibo_praised_layout_listView = (ListView) findViewById(R.id.my_weibo_praised_layout_listView);
		mwpadapter = new MyWeiboPraisedAdapter();
		my_weibo_praised_layout_listView.setAdapter(mwpadapter);
		
		if (type == 2) {//收到的肯定
			my_weibo_praised_layout_title.setText("收到的肯定");
		} else if (type == 6) {//发出的肯定
			my_weibo_praised_layout_title.setText("发出的肯定");
		} 
		
		my_weibo_praised_back_button.setOnClickListener(new ImageButton.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent i = new Intent(MyWeiboPraisedActivity.this, com.mcds.app.android.estar.ui.my.HomeActivity.class);
//				startActivity(i);
				MyWeiboPraisedActivity.this.finish();
			}
		});
		
		//列表点击事件
		my_weibo_praised_layout_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
//				MyWeiboPraisedInfo item = (MyWeiboPraisedInfo)arg0.getAdapter().getItem(arg2);
//				String weibo_id = item.weiboid;
//				
//				Intent intent = new Intent(MyWeiboPraisedActivity.this, HomeListDetailsActivity.class);
//				intent.putExtra("weibo_id", weibo_id);
//				startActivity(intent);
			}
		});

		// 向下滑动事件
		my_weibo_praised_layout_listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						page_num = page_num + 1;
						getData();
						mwpadapter.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});
		getData();
	}
	
	
	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					System.out.println("scj这是type的值："+type);
					if (type == 2) {//收到的肯定
						my_SystemMessages = ConnectProviderCL.getSystemMessageList(uid,String.valueOf(type),String.valueOf(page_num), PAGE_SIZE);
					} else if (type == 6) {//发出的肯定
						my_SystemMessages = ConnectProviderCL.getSystemMessageList(uid,String.valueOf(type),String.valueOf(page_num), PAGE_SIZE);
					} 
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
				if (my_SystemMessages.status.equals("0")) {
					mwpadapter.addItems(my_SystemMessages.getDatas());
					mwpadapter.notifyDataSetChanged();
				}
			}
		});
	}
	TextView user_name = null;
	TextView user_classification = null;
	TextView user_time = null;
	TextView user_content = null;
	ImageView iv = null;

	//列表数据
	private class MyWeiboPraisedAdapter extends BaseListAdapter<My_SystemMessage> {
		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			final My_SystemMessage item = this.getItem(position);
			if (convertView==null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.my_weibo_praised_item, null);
				user_name = (TextView) convertView.findViewById(R.id.my_weibo_praised_item_user_name);
				user_classification = (TextView) convertView.findViewById(R.id.my_weibo_praised_user_work);
				user_content = (TextView) convertView.findViewById(R.id.my_weibo_praised_item_message_content);
				user_time = (TextView) convertView.findViewById(R.id.my_weibo_praised_item_attention_flag);
				iv = (ImageView)convertView.findViewById(R.id.my_weibo_praised_item_user_avatar);
			}
			
			user_name.setText(item.sender);
			user_classification.setText(item.classification);
			user_content.setText(item.content_text);
			user_time.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(item.get_time))));
			if (!"".equals(item.uid)) {
				iv.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
//						Toast.makeText(MyWeiboPraisedActivity.this, "点了头像", Toast.LENGTH_LONG).show();
						Intent itt = new Intent(MyWeiboPraisedActivity.this,HisHomeActivity.class);
						itt.putExtra("hisId", item.uid);
						startActivity(itt);
					}
				});
			}
			Bitmap bmp = asyncLoader.loadBitmap(iv, item.sender_img, Utility.dip2px(MyWeiboPraisedActivity.this, (float) 40.0), Utility.dip2px(MyWeiboPraisedActivity.this, (float) 40.0), new ImageCallBack() {

				@Override
				public void imageLoad(ImageView imageView, Bitmap bitmap) {
					if (bitmap == null) {
						bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
					}
					imageView.setImageBitmap(bitmap);
				}
			});

			if (bmp != null) {
				iv.setImageBitmap(bmp);
			}
			
			return convertView;
		}

	}
}
