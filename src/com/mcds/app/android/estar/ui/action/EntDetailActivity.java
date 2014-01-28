package com.mcds.app.android.estar.ui.action;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.Action;
import com.mcds.app.android.estar.bean.ActionDetail;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.provider.ConnectProviderZX;
import com.mcds.app.android.estar.ui.BaseActivity;
import com.mcds.app.android.estar.util.Utility;

public class EntDetailActivity extends BaseActivity{
	
	private ImageButton actionDetailBackButton;
	private ImageView actionDetailImage;
	private TextView actionDetailTitle;
	private TextView actionDetailStatus;
	private TextView actionDetailStartTime;
	private TextView actionDetailEndTime;
	private TextView actionDetailWriter;
	private TextView actionDetailExpend;
	private TextView actionDetailAddress;
	private TextView actionDetailJoined;
	private TextView actionDetailFavor;
	private TextView actionDetailContent;
	
	private TextView actionDetailJoinButton;
	private TextView actionDetailCommentButton;
	private TextView actionDetailCommentNum;
	private TextView actionDetailPhotoButton;
	private TextView actionDetailPhotoNum;
	
	private ReturnResult<String> rrInterests;
	
	/**
	 * 列表项图片宽度和高度
	 */
	private final static float ACTION_DETAIL_ITEM_IMAGE_H = 240;
	
	private ReturnResult<ActionDetail> rrActionDetail;
	private String curActionID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.action_detail);
		Bundle bundle = getIntent().getExtras();  
		curActionID = bundle.getString("activity_id");

		init();
		getData();
	}

	private void init() {
		// TODO Auto-generated method stub
		actionDetailImage = (ImageView)findViewById(R.id.action_detail_image);
		actionDetailTitle = (TextView)findViewById(R.id.action_detail_title);
		actionDetailStatus = (TextView)findViewById(R.id.action_detail_status);
		actionDetailStartTime = (TextView)findViewById(R.id.action_detail_time_start);
		actionDetailEndTime = (TextView)findViewById(R.id.action_detail_time_end);
		actionDetailWriter = (TextView)findViewById(R.id.action_detail_writer);
		actionDetailExpend = (TextView)findViewById(R.id.action_detail_expend);
		actionDetailAddress = (TextView)findViewById(R.id.action_detail_address);
		actionDetailJoined = (TextView)findViewById(R.id.action_detail_joined);
		actionDetailFavor = (TextView)findViewById(R.id.action_detail_favor);
		actionDetailContent = (TextView)findViewById(R.id.action_detail_content);
		actionDetailCommentNum = (TextView)findViewById(R.id.action_detail_comments_num);
		actionDetailPhotoNum = (TextView)findViewById(R.id.action_detail_photos_num);
		
		
		actionDetailJoinButton = (TextView)findViewById(R.id.action_detail_join_btn);
		actionDetailCommentButton = (TextView)findViewById(R.id.action_detail_comments_btn);
		actionDetailPhotoButton = (TextView)findViewById(R.id.action_detail_photos_btn);
		actionDetailBackButton = (ImageButton)findViewById(R.id.action_detail_back_btn);
		
		
		
		findViewById(R.id.action_detail_join_btn).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO 参加
				
				if (!curActionID.equals("") && !UserManager.uid.equals("")) {
					 ReturnResult<String> rr = ConnectProviderZX.joinTheActivity(curActionID, UserManager.uid);
					 String notice = "";
					 if(rr.status.equals("0")){
						 notice = "√ 成功参与活动";
					 }else{
						 notice = rr.info;
					 }
					 Toast toast = Toast.makeText(EntDetailActivity.this, notice, Toast.LENGTH_SHORT);
					 toast.setGravity(Gravity.CENTER, 0, 0);
					 toast.show();
				}
				
			}
			
		});
		
		actionDetailCommentButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO 评论
				Intent intent = new Intent(EntDetailActivity.this, com.mcds.app.android.estar.ui.action.CommentListDetailActivity.class);
				Bundle bd = new Bundle();
				bd.putString("activity_id", curActionID);
				intent.putExtras(bd);
				startActivity(intent);
			}
			
		});
		
		actionDetailPhotoButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO 照片
				Intent intent = new Intent(EntDetailActivity.this, com.mcds.app.android.estar.ui.action.PhotoListActivity.class);
				Bundle bd = new Bundle();
				bd.putString("activity_id", curActionID);
				intent.putExtras(bd);
				startActivity(intent);
			}
			
		});
		
		actionDetailBackButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				startActivity(new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.action.HomeActivity.class));
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
					
				}
				doListViewUI();

				hideWaitingDialog();
			}
		}).start();
	}

	/**
	 * 更新界面
	 */
	private void doListViewUI() {
		this.runOnUiThread(new Runnable() {

			private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();
			
			@Override
			public void run() {
				if (rrActionDetail.status.equals("0")) {
					//更新
					ActionDetail detail = new ActionDetail();
					detail = rrActionDetail.getDatas().get(0);
					if(!detail.equals("")){
						((TextView)findViewById(R.id.action_detail_title)).setText(detail.title);
						actionDetailStatus.setText(detail.status);
						actionDetailStartTime.setText(detail.time_start);
						actionDetailEndTime.setText(detail.time_end);
						actionDetailWriter.setText(detail.writer);
						actionDetailExpend.setText(detail.expend);
						actionDetailAddress.setText(detail.address);
						actionDetailJoined.setText(detail.join_num + "人");
						actionDetailFavor.setText(detail.favor_num + "人");
						actionDetailFavor.setTag(detail);
						actionDetailFavor.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View view) {
								// TODO Auto-generated method stub
								ActionDetail detail = (ActionDetail)view.getTag();
								showWaitingDialog();
								if (!curActionID.equals("")) {
									rrInterests = ConnectProviderZX.intrestActivity(UserManager.uid, curActionID);
									if("0".equals(rrInterests.status)){
										actionDetailFavor.setText((Integer.valueOf(detail.favor_num) + 1) + "人");
									}
								}
								

								hideWaitingDialog();
							}
						});
						
						actionDetailContent.setText(detail.content);
						actionDetailCommentNum.setText("(" + detail.commit_num + ")");
						actionDetailPhotoNum.setText("(" + detail.photo_num + ")");
						
						DisplayMetrics metrics = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(metrics);
						
						Bitmap bmp = asyncLoader.loadBitmap(
								actionDetailImage, 
								detail.img, 
								Utility.dip2px(EntDetailActivity.this, metrics.widthPixels), 
								Utility.dip2px(EntDetailActivity.this, ACTION_DETAIL_ITEM_IMAGE_H), 
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
							actionDetailImage.setImageBitmap(bmp);
						}
						
					}
					
				}
			}
		});
	}
	
	public void onBackPressed() {  
		super.onBackPressed();
	}  
	
	
	
	



}
