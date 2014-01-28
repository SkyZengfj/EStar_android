package com.mcds.app.android.estar.ui.action;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.ActionDetail;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.QuestionairManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.provider.ConnectProviderZX;
import com.mcds.app.android.estar.ui.BaseActivity;
import com.mcds.app.android.estar.ui.news.HeartTestStart;
import com.mcds.app.android.estar.util.Utility;

public class OnlineTestDetailActivity extends BaseActivity{

	private String curActionID;
	private ReturnResult<ActionDetail> rrActionDetail;
	/**
	 * 列表项图片宽度和高度
	 */
	private final static float ACTION_DETAIL_ITEM_IMAGE_H = 240;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.action_detail_ontest);
		Bundle bundle = getIntent().getExtras();  
		curActionID = bundle.getString("activity_id");

		init();
		getData();
	}

	private void init() {
		// TODO Auto-generated method stub
		findViewById(R.id.action_detail_ont_join_btn).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO 参加
				Intent intent = new Intent(OnlineTestDetailActivity.this, com.mcds.app.android.estar.ui.action.QuestionairActivity.class);
				QuestionairManager.activity_id = curActionID;
				startActivity(intent);
				startActivity(intent);
				
			}
			
		});
		
		
		findViewById(R.id.action_detail_ont_back_btn).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				startActivity(new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.action.HomeActivity.class));
				onBackPressed();
			}
			
		});
		
//		
		findViewById(R.id.action_detail_onlinetest_comment).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(OnlineTestDetailActivity.this, com.mcds.app.android.estar.ui.action.CommentListDetailActivity.class);
				Bundle bd = new Bundle();
				bd.putString("activity_id", curActionID);
				intent.putExtras(bd);
				startActivity(intent);
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
						((TextView)findViewById(R.id.action_detail_ont_title)).setText(detail.title);
						((TextView)findViewById(R.id.action_detail_ont_status)).setText(detail.status);
						((TextView)findViewById(R.id.action_detail_ont_time_start)).setText(detail.time_start);
						((TextView)findViewById(R.id.action_detail_ont_time_end)).setText(detail.time_end);
						((TextView)findViewById(R.id.action_detail_ont_writer)).setText(detail.writer);
						((TextView)findViewById(R.id.action_detail_ont_content)).setText(detail.content);
//						((TextView)findViewById(R.id.action_detail_ont_comments_num)).setText("(" + detail.commit_num + ")");
						((TextView)findViewById(R.id.action_detail_ont_award)).setText(detail.award);
						
						DisplayMetrics metrics = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(metrics);
						ImageView imv = (ImageView)findViewById(R.id.action_detail_ont_image);
						Bitmap bmp = asyncLoader.loadBitmap(
								imv, 
								detail.img, 
								Utility.dip2px(OnlineTestDetailActivity.this, metrics.widthPixels), 
								Utility.dip2px(OnlineTestDetailActivity.this, ACTION_DETAIL_ITEM_IMAGE_H), 
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
	
}
