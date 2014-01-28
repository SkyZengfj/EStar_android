package com.mcds.app.android.estar.ui.action;

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
import com.mcds.app.android.estar.bean.ActionQuestionairResult;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.NewsTestManager;
import com.mcds.app.android.estar.manager.QuestionairManager;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProviderZX;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.ui.BaseActivity;

public class QuestionairResultActivity extends BaseActivity {

	private ReturnResult<ActionQuestionairResult> rractionTestResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.action_detail_ontest_qa_result);
		init();
		getData();
		
	}

	private void init() {
		// TODO Auto-generated method stub

		// news_heart_test_qa_result_retest_btn
		((TextView) findViewById(R.id.action_test_qa_result_retest_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						//重测
						Intent intent = new Intent(
								getApplicationContext(),
								com.mcds.app.android.estar.ui.action.QuestionairActivity.class);
						startActivity(intent);
					}
				});
	}

	/**
	 * 获取活动列表
	 */
	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")
						&& !QuestionairManager.question_result.equals("")
						&& !QuestionairManager.activity_id.equals("")) {
					rractionTestResult = ConnectProviderZX
							.getActivityTestResult(UserManager.uid,
									QuestionairManager.activity_id,
									QuestionairManager.question_result);
					doListViewUI();
				}

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
				if (rractionTestResult.status.equals("0")) {
					// rrNewsTestResult.getDatas()
					// news_detail_ontest_qa_result_content
					ActionQuestionairResult result = rractionTestResult.getDatas().get(0);
					if(result.activity_type.equals("0")){
						//趣味测试
//						
						((TextView) findViewById(R.id.action_detail_ontest_qa_result_content))
						.setText(result.content);
						
						DisplayMetrics metrics = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(metrics);
						int width = (metrics.widthPixels * 680) / 720;
						int height = (metrics.widthPixels * 470) / 720;

						Bitmap bmp = asyncLoader
								.loadBitmap(
										(ImageView) findViewById(R.id.action_detail_ontest_qa_result_img),
										result.content_img,
										width, height, new ImageCallBack() {

											@Override
											public void imageLoad(
													ImageView imageView,
													Bitmap bitmap) {
												if (bitmap == null) {
													bitmap = BitmapFactory
															.decodeResource(
																	getResources(),
																	R.drawable.ic_default_action_list_item);
												}
												imageView.setImageBitmap(bitmap);
											}
										});

						if (bmp != null) {
							((ImageView) findViewById(R.id.action_detail_ontest_qa_result_img))
									.setImageBitmap(bmp);
						}
					}else if(rractionTestResult.getDatas().get(0).activity_type.equals("2")){
						//测评
						
					}

					
				}
			}
		});
	}

	public void onBackPressed() {
		Intent intent = new Intent(
				getApplicationContext(),
				com.mcds.app.android.estar.ui.action.QuestionairActivity.class);
		startActivity(intent);
		super.onBackPressed();
	}

}
