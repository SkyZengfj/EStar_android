package com.mcds.app.android.estar.ui.news;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.HeartHomeList;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.ui.BaseActivity;

public class NewsHeartDetailLifeActivity extends BaseActivity{

	private HeartHomeList hhl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_heart_detail_life);
		Bundle bd = getIntent().getExtras();
		hhl = (HeartHomeList) bd.getSerializable("eap_detail");
		if (hhl != null) {

		}

		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		this.runOnUiThread(new Runnable() {

			private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();

			@Override
			public void run() {
				((TextView) findViewById(R.id.news_heart_detail_life_title))
						.setText(hhl.title);
				((TextView) findViewById(R.id.news_heart_detail_life_content))
						.setText(hhl.content);

				DisplayMetrics metrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(metrics);
				int width = (metrics.widthPixels * 660) / 720;
				int height = (metrics.widthPixels * 444) / 720;
				Bitmap bmpbig = asyncLoader
						.loadBitmap(
								(ImageView) findViewById(R.id.news_heart_detail_life_img),
								hhl.image_url, width, height,
								new ImageCallBack() {

									@Override
									public void imageLoad(ImageView imageView,
											Bitmap bitmap) {
										if (bitmap == null) {
											bitmap = BitmapFactory
													.decodeResource(
															getResources(),
															R.drawable.list_item_ic_default);
										}
										imageView.setImageBitmap(bitmap);
									}
								});

				if (bmpbig != null) {
					((ImageView) findViewById(R.id.news_heart_detail_life_img))
							.setImageBitmap(bmpbig);
				}
			}
		});
	}
}
