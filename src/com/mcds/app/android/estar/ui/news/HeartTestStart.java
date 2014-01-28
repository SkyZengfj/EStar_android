package com.mcds.app.android.estar.ui.news;

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
import com.mcds.app.android.estar.bean.NewsTestContent;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.TestList;
import com.mcds.app.android.estar.manager.NewsTestManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.ConnectProviderZX;
import com.mcds.app.android.estar.ui.BaseActivity;

public class HeartTestStart extends BaseActivity {

	private TestList test;
	private TextView start_btn;
	private ReturnResult<NewsTestContent> rrNewsTestContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_heart_test_start);
		Bundle bundle = getIntent().getExtras();
		if(NewsTestManager.test_list == null){
			test = (TestList) bundle.getSerializable("rr_test");
		}else{
			test = NewsTestManager.test_list;
		}
		

		init();
		getData();
	}

	private void init() {
		// TODO Auto-generated method stub
		start_btn = (TextView) findViewById(R.id.news_heart_test_start_btn);
		start_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO 开始测试
				Intent intent = new Intent(HeartTestStart.this, com.mcds.app.android.estar.ui.news.HeartTestQA.class);
				startActivity(intent);
			}
		});
//		doListViewUI();
	}

	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();
				
				NewsTestManager.test = null;

				rrNewsTestContent = ConnectProviderZX.getTestContent(test.test_id);

				hideWaitingDialog();
				doListViewUI();
			}
		}).start();
	}

	private void doListViewUI() {

		this.runOnUiThread(new Runnable() {

			private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();

			@Override
			public void run() {
				if (rrNewsTestContent.status.equals("0")) {
					NewsTestManager.test = rrNewsTestContent.getDatas().get(0);
					NewsTestManager.test_id = test.test_id;
					NewsTestManager.test_list = test;
					
					if(!NewsTestManager.test.test_title.equals("")){
						((TextView) findViewById(R.id.news_heart_test_start_content)).setText(test.test_content);
						DisplayMetrics metrics = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(metrics);
						int width = (metrics.widthPixels * 660) / 720;
						int height = (metrics.widthPixels * 444) / 720;
						Bitmap bmpbig = asyncLoader.loadBitmap((ImageView) findViewById(R.id.news_heart_test_start_img), test.image_url, width, height, new ImageCallBack() {

							@Override
							public void imageLoad(ImageView imageView, Bitmap bitmap) {
								if (bitmap == null) {
									bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.list_item_ic_default);
								}
								imageView.setImageBitmap(bitmap);
							}
						});

						if (bmpbig != null) {
							((ImageView) findViewById(R.id.news_heart_test_start_img)).setImageBitmap(bmpbig);
						}
					}
				}

//				news_heart_test_start_content
				
			}
		});
	}
}
