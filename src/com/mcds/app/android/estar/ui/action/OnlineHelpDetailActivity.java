package com.mcds.app.android.estar.ui.action;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.ActionDetail;
import com.mcds.app.android.estar.bean.ActivitCommitList;
import com.mcds.app.android.estar.bean.ActivityHelpReply;
import com.mcds.app.android.estar.bean.BirthdayList;
import com.mcds.app.android.estar.bean.HeartHomeList;
import com.mcds.app.android.estar.bean.HonorsList;
import com.mcds.app.android.estar.bean.NewsListDetail;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.provider.ConnectProviderZX;
import com.mcds.app.android.estar.ui.BaseActivity;
import com.mcds.app.android.estar.ui.news.HeartTestStart;
import com.mcds.app.android.estar.util.LayoutUtility;
import com.mcds.app.android.estar.util.Utility;

public class OnlineHelpDetailActivity extends BaseActivity{

	private String curActionID;
	private ReturnResult<ActionDetail> rrActionDetail;
	private ReturnResult<ActivityHelpReply> rrHelpReply;
	
	private String help_page_num = "0";
	private String help_page_size = "5";
	/**
	 * 列表项图片宽度和高度
	 */
	private final static float ACTION_DETAIL_ITEM_IMAGE_H = 240;
	
	private ListView actionDetailOnhListView;
	private ActionOnhDetailAdapter adapter;
	
	private ImageView im_accept;
	
	private List<String> accepttag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.action_detail_onlinehelp);
		Bundle bundle = getIntent().getExtras();  
		curActionID = bundle.getString("activity_id");

		init();
		getData();
	}

	private void init() {
		// TODO Auto-generated method stub
		accepttag = new ArrayList<String>();
		((TextView)findViewById(R.id.action_detail_onlinehelp_comment)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 回复
				Intent intent = new Intent(OnlineHelpDetailActivity.this, com.mcds.app.android.estar.ui.action.CommentReplyActivity.class);
				Bundle bd = new Bundle();
				bd.putString("activity_id", curActionID);
				intent.putExtras(bd);
				startActivity(intent);
			}
		});
		
		actionDetailOnhListView = (ListView)findViewById(R.id.action_detail_onh_listview);
		adapter = new ActionOnhDetailAdapter();
		
		actionDetailOnhListView.setAdapter(adapter);
		setListViewHeightBasedOnChildren(actionDetailOnhListView);
		actionDetailOnhListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				ActivityHelpReply ahp = rrHelpReply.getDatas().get(position);
				if(!UserManager.uid.equals(ahp.reply_id)){
					ReturnResult<String> rrAccept = ConnectProviderZX.acceptTheActivityHelpReply(curActionID, UserManager.uid, ahp.reply_id);
					if(rrAccept.status.equals("0")){
						im_accept.setVisibility(View.VISIBLE);
					}
				}
			}
		});
		findViewById(R.id.action_detail_onh_back_btn).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
			
		});
		
	}
	
	/**
	 * 获取活动详情
	 */
	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				if (!curActionID.equals("")) {
					rrActionDetail = ConnectProviderZX.getActivityDetail(curActionID);
					rrHelpReply = ConnectProviderZX.getActivityHelpReply(curActionID, help_page_num, help_page_size);

//					for(int i = 0; i < rrHelpReply.getDatas().size(); i ++){
//						ActivityHelpReply ahp = rrHelpReply.getDatas().get(i);
//						if(!UserManager.uid.equals(ahp.reply_id)){
//							ReturnResult<String> rrAccept = ConnectProviderZX.acceptTheActivityHelpReply(curActionID, UserManager.uid, ahp.reply_id);
//							if(rrAccept.status.equals("0")){
//								accepttag.add("1");
//							}else{
//								accepttag.add("0");
//							}
//						}else{
//							accepttag.add("0");
//						}
//					}
				}
				doContentUI();
				doListViewUI();
				hideWaitingDialog();
			}
		}).start();
	}
	
	/**
	 * 更新界面
	 */
	private void doContentUI() {
		this.runOnUiThread(new Runnable() {

			private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();
			
			@Override
			public void run() {
				if (rrActionDetail.status.equals("0")) {
					//更新
					ActionDetail detail = new ActionDetail();
					detail = rrActionDetail.getDatas().get(0);
					if(!detail.equals("")){
						((TextView)findViewById(R.id.action_detail_onh_title)).setText(detail.title);
						((TextView)findViewById(R.id.action_detail_onh_status)).setText(detail.status);
						((TextView)findViewById(R.id.action_detail_onh_time_start)).setText(detail.time_start);
						((TextView)findViewById(R.id.action_detail_onh_time_end)).setText(detail.time_end);
						((TextView)findViewById(R.id.action_detail_onh_writer)).setText(detail.writer);
						((TextView)findViewById(R.id.action_detail_onh_content)).setText(detail.content);
						
						DisplayMetrics metrics = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(metrics);
						ImageView imv = (ImageView)findViewById(R.id.action_detail_onh_image);
						Bitmap bmp = asyncLoader.loadBitmap(
								imv, 
								detail.img, 
								Utility.dip2px(OnlineHelpDetailActivity.this, metrics.widthPixels), 
								Utility.dip2px(OnlineHelpDetailActivity.this, ACTION_DETAIL_ITEM_IMAGE_H), 
								new ImageCallBack() {

							@Override
							public void imageLoad(ImageView imageView, Bitmap bitmap) {
								if (bitmap == null) {
									bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.list_item_ic_default);
								}
								imageView.setImageBitmap(bitmap);
							}
						});

						if (bmp != null) {
							imv.setImageBitmap(bmp);
						}
						
					}
					
				}
			}
		});
	}
	
	/**
	 * 更新界面
	 */
	private void doListViewUI() {
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (rrHelpReply.status.equals("0")) {
					adapter.setItems(rrHelpReply.getDatas());
					LayoutUtility.setListViewHeightBasedOnChildren(actionDetailOnhListView);
					adapter.notifyDataSetChanged();
				}
			}
		});
	}
	
	private class ActionOnhDetailAdapter extends BaseListAdapter<ActivityHelpReply>{
		
		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null){
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.action_detail_onlinehelp_item, null);
				holder = new ViewHolder();
				holder.actionHelpItemImage = (ImageView)convertView.findViewById(R.id.action_detail_onh_item_img);
				holder.actionHelpItemName = (TextView)convertView.findViewById(R.id.action_detail_onh_item_name);
				holder.actionHelpItemTime = (TextView)convertView.findViewById(R.id.action_detail_onh_item_time);
				holder.actionHelpItemContent = (TextView)convertView.findViewById(R.id.action_detail_onh_item_content);
				holder.actionHelpItemcommit_num = (TextView)convertView.findViewById(R.id.action_detail_onh_item_commit_num);
				holder.actionHelpItemshare_num = (TextView)convertView.findViewById(R.id.action_detail_onh_item_share_num);

				
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			im_accept = (ImageView)convertView.findViewById(R.id.action_detail_onh_item_acception);
			
			if (holder != null) {
				ActivityHelpReply item = this.getItem(position);
				if(accepttag.get(position).equals("1")){
					im_accept.setVisibility(View.VISIBLE);
				}else{
					im_accept.setVisibility(View.INVISIBLE);
				}
				
				holder.actionHelpItemName.setText(item.name);
				holder.actionHelpItemTime.setText(item.time);
				holder.actionHelpItemContent.setText(item.content);
				holder.actionHelpItemcommit_num.setText(item.commit_num);
				holder.actionHelpItemshare_num.setText(item.share_num);
				

				
				Bitmap bmp = asyncLoader.loadBitmap(
						holder.actionHelpItemImage, 
						item.img, 
						Utility.dip2px(OnlineHelpDetailActivity.this, 50), 
						Utility.dip2px(OnlineHelpDetailActivity.this, 50), 
						new ImageCallBack() {

					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						if (bitmap == null) {
							bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.list_item_ic_default);
						}
						imageView.setImageBitmap(bitmap);
					}
				});

				if (bmp != null) {
					holder.actionHelpItemImage.setImageBitmap(bmp);
				}
			}
			return convertView;
		}
		
		
		
	}
	
	private class ViewHolder {
		ImageView actionHelpItemImage;
		TextView actionHelpItemName;
		TextView actionHelpItemTime;
		TextView actionHelpItemContent;
		TextView actionHelpItemcommit_num;
		TextView actionHelpItemshare_num;
	}
	
	public void setListViewHeightBasedOnChildren(ListView listView) {  
	    ListAdapter listAdapter = listView.getAdapter();   
	    if (listAdapter == null) {  
	        return;  
	    }  

	    int totalHeight = 0;  
	    for (int i = 0; i < listAdapter.getCount(); i++) {  
	        View listItem = listAdapter.getView(i, null, listView);  
	        listItem.measure(0, 0);  
	        totalHeight += listItem.getMeasuredHeight();  
	    }  

	    ViewGroup.LayoutParams params = listView.getLayoutParams();  
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
//	    ((MarginLayoutParams)params).setMargins(10, 10, 10, 10);
	    listView.setLayoutParams(params);  
	} 

	
}
