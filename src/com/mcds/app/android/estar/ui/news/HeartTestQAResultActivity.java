package com.mcds.app.android.estar.ui.news;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.NewsList;
import com.mcds.app.android.estar.bean.NewsTestContent;
import com.mcds.app.android.estar.bean.NewsTestResult;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.NewsTestManager;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProviderZX;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.ui.BaseActivity;
import com.mcds.app.android.estar.util.LayoutUtility;

public class HeartTestQAResultActivity extends BaseActivity {

	private int scoreall;
	private ReturnResult<NewsTestResult> rrNewsTestResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_heart_test_qa_result);
		Bundle bundle = getIntent().getExtras();
		scoreall = bundle.getInt("testscore");
		init();
		getData();
	}

	private void init() {
		// TODO Auto-generated method stub
		((TextView) findViewById(R.id.news_heart_test_qa_result_score))
				.setText("" + scoreall);
//		news_heart_test_qa_result_retest_btn
		((TextView) findViewById(R.id.news_heart_test_qa_result_retest_btn)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.news.HeartTestStart.class);
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
						&& !NewsTestManager.test_id.equals("")) {
					rrNewsTestResult = ConnectProviderZX.getTestResult(
							UserManager.uid, NewsTestManager.test_id,
							String.valueOf(scoreall));
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
				if (rrNewsTestResult.status.equals("0")) {
					// rrNewsTestResult.getDatas()
					// news_detail_ontest_qa_result_content
					((TextView) findViewById(R.id.news_detail_ontest_qa_result_content))
							.setText(rrNewsTestResult.getDatas().get(0).result_content);
					
					DisplayMetrics metrics = new DisplayMetrics();
					getWindowManager().getDefaultDisplay().getMetrics(metrics);
					int width = (metrics.widthPixels * 680) / 720;
					int height = (metrics.widthPixels * 470) / 720;

					Bitmap bmp = asyncLoader.loadBitmap((ImageView)findViewById(R.id.news_heart_test_qa_result_img), rrNewsTestResult.getDatas().get(0).result_img, width, height, new ImageCallBack() {

						@Override
						public void imageLoad(ImageView imageView, Bitmap bitmap) {
							if (bitmap == null) {
								bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_default_action_list_item);
							}
							imageView.setImageBitmap(bitmap);
						}
					});

					if (bmp != null) {
						((ImageView)findViewById(R.id.news_heart_test_qa_result_img)).setImageBitmap(bmp);
					}
				}
			}
		});
	}
	
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.news.HeartTestStart.class);
		startActivity(intent);
		super.onBackPressed();
	}

}
