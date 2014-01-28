package com.mcds.app.android.estar.ui.my;


import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.CommitInfo;
import com.mcds.app.android.estar.bean.MyAtMeInfo;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.ui.BaseActivity;
import com.mcds.app.android.estar.ui.weibo.HomeListDetailsActivity;
import com.mcds.app.android.estar.util.Utility;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 我的评论
 * @author 
 *
 */
public class CommitActivity extends BaseActivity {

	
	private TextView commit_list_layout_title;
	private ImageButton commit_list_back_button;
	private ListView commit_list_layout_listView;
	private CommitAdapter comAdapter;
	private ReturnResult<CommitInfo> commitInfos;
	private int page_num = 0;
	private final String PAGE_SIZE = "10";
	private int commit_type = 2;//0:全部评论（默认） 1:我关注人的评论 2:我的评论
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commit_list);
		
		commit_list_layout_title = (TextView) findViewById(R.id.commit_list_layout_title);
		commit_list_back_button = (ImageButton) findViewById(R.id.commit_list_back_button);
		commit_list_layout_listView = (ListView) findViewById(R.id.commit_list_layout_listView);
		
		comAdapter = new CommitAdapter();
		commit_list_layout_listView.setAdapter(comAdapter);
		//返回按钮
		commit_list_back_button.setOnClickListener(new ImageButton.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent i = new Intent(CommitActivity.this, com.mcds.app.android.estar.ui.my.HomeActivity.class);
//				startActivity(i);
				CommitActivity.this.finish();
			}
		});
		
		// 列表点击事件
		commit_list_layout_listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					CommitInfo item = (CommitInfo)arg0.getAdapter().getItem(arg2);
					 String weibo_id = item.weibo_id;
					
					 Intent intent = new Intent(CommitActivity.this, HomeListDetailsActivity.class);
					 intent.putExtra("weibo_id", weibo_id);
					 startActivity(intent);
				}
			});
		
		commit_list_layout_listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						page_num = page_num + 1;
						getData();
						comAdapter.notifyDataSetChanged();
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
					commitInfos = ConnectProvider.getCommitList(UserManager.uid, String.valueOf(commit_type), String.valueOf(page_num), PAGE_SIZE);
					
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
				if (commitInfos.status.equals("0")) {
					comAdapter.addItems(commitInfos.getDatas());
					comAdapter.notifyDataSetChanged();
				}
			}
		});
	}
	
	
	TextView user_name = null;
	ImageView user_avatar = null;
	TextView user_work = null;
	TextView user_time = null;
	TextView original_comtent = null;//原微博内容
	TextView content_text = null;//回复内容
	TextView share_num = null;//	转发
	TextView comment_num = null;//	评论 
	TextView good_num = null;//	赞

	
	//列表数据
	private class CommitAdapter extends BaseListAdapter<CommitInfo> {
		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			final CommitInfo item = this.getItem(position);
			if (convertView==null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.commit_list_time, null);
				user_name = (TextView) convertView.findViewById(R.id.commit_list_item_user_name);
				user_avatar = (ImageView) convertView.findViewById(R.id.commit_list_item_user_avatar);
				user_time = (TextView) convertView.findViewById(R.id.commit_list_item_user_time);
				user_work = (TextView) convertView.findViewById(R.id.commit_list_user_work);
				original_comtent = (TextView) convertView.findViewById(R.id.commit_list_item_message_content);
				content_text = (TextView) convertView.findViewById(R.id.commit_list_item_message_content2);
				share_num = (TextView) convertView.findViewById(R.id.commit_list_item_forwording_count);
				comment_num = (TextView) convertView.findViewById(R.id.commit_list_item_comment_count);
				good_num = (TextView) convertView.findViewById(R.id.commit_list_item_praise_count);
				
			}
			
			user_name.setText(item.user_name);
			user_time.setText(item.time);
			user_work.setText(item.user_work);
			content_text.setText("回复："+item.content_text);
			original_comtent.setText(item.reply_to);
			share_num.setText(item.share_num);
			comment_num.setText(item.comment_num);
			good_num.setText(item.good_num);
			if (!"".equals(item.uid)) {
				user_avatar.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent itt = new Intent(CommitActivity.this,HisHomeActivity.class);
						itt.putExtra("hisId", item.uid);
						startActivity(itt);
					}
				});
			}
			Bitmap bmp = asyncLoader.loadBitmap(user_avatar, item.user_img, Utility.dip2px(CommitActivity.this, (float) 40.0), Utility.dip2px(CommitActivity.this, (float) 40.0), new ImageCallBack() {

				@Override
				public void imageLoad(ImageView imageView, Bitmap bitmap) {
					if (bitmap == null) {
						bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
					}
					imageView.setImageBitmap(bitmap);
				}
			});

			if (bmp != null) {
				user_avatar.setImageBitmap(bmp);
			}
			
			return convertView;
		}
	}
}
