package com.mcds.app.android.estar.ui.news;

import java.net.URI;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.ActionDetail;
import com.mcds.app.android.estar.bean.NewsList;
import com.mcds.app.android.estar.bean.NewsListDetail;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.provider.ConnectProviderZX;
import com.mcds.app.android.estar.ui.BaseTabActivity;
import com.mcds.app.android.estar.ui.action.CommentListDetailActivity;
import com.mcds.app.android.estar.ui.action.EntDetailActivity;
import com.mcds.app.android.estar.util.BitmapUtility;
import com.mcds.app.android.estar.util.Utility;

public class NewsDetailActivity extends BaseTabActivity{

	private TextView newsDetailContent;
	private TextView newsDetailTitle;
	private ImageView newsDetailImg;
	private NewsList newsList;
	
	/**
	 * 列表项图片宽度和高度
	 */
//	private final static float NEWS_DETAIL_ITEM_IMAGE_H = 240;
	
//	private ReturnResult<NewsListDetail> rrnewsDetail;
//	private String curNewsID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_news_detail);
		Bundle bundle = getIntent().getExtras();  
		newsList = (NewsList) bundle.getSerializable("NewsList");
		
//		curNewsID = bundle.getString("news_id");
//		curNewsID = newsList.news_id;
		initContent();
		getData();
//		Bitmap bt = BitmapUtility.getHttpBitmap(newsList.image_url, 660, 444);
//		newsDetailImg.setImageBitmap(bt);
	}
	private void initContent() {
		// TODO Auto-generated method stub
		newsDetailImg = (ImageView)findViewById(R.id.newsNews_detail_img);
		newsDetailContent = (TextView)findViewById(R.id.newsNews_detail_content);
		newsDetailTitle = (TextView)findViewById(R.id.newsNews_detail_title);
	}
	
	/**
	 * 获取活动列表
	 */
	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

//				if (!"".equals(curNewsID) && !"".equals(UserManager.uid)) {
//					rrnewsDetail = ConnectProviderZX.getNewsDetailed(UserManager.uid, curNewsID);
//				}
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
				
				if (newsList!=null) {
					newsDetailTitle.setText(newsList.news_title);
					newsDetailContent.setText(newsList.news_content);
					DisplayMetrics metrics = new DisplayMetrics();
					getWindowManager().getDefaultDisplay().getMetrics(metrics);
					int width = (metrics.widthPixels * 660) / 720;
					int height = (metrics.widthPixels * 444) / 720;
					Bitmap bmp = asyncLoader.loadBitmap(
							newsDetailImg, 
							newsList.image_url, 
							width,
							height,
//							Utility.dip2px(NewsDetailActivity.this, metrics.widthPixels), 
//							Utility.dip2px(NewsDetailActivity.this, NEWS_DETAIL_ITEM_IMAGE_H), 
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
						newsDetailImg.setImageBitmap(bmp);
					}
				}
				
//				if (rrnewsDetail.status.equals("0")) {
//					//更新
//					NewsListDetail detail = new NewsListDetail();
//					detail = rrnewsDetail.getDatas().get(0);
//					if(!detail.equals("")){
//						newsDetailTitle.setText(detail.news_title);
//						newsDetailContent.setText(detail.content);
//						
//						DisplayMetrics metrics = new DisplayMetrics();
//						getWindowManager().getDefaultDisplay().getMetrics(metrics);
//						
//						Bitmap bmp = asyncLoader.loadBitmap(
//								newsDetailImg, 
//								detail.image_url, 
//								Utility.dip2px(NewsDetailActivity.this, metrics.widthPixels), 
//								Utility.dip2px(NewsDetailActivity.this, NEWS_DETAIL_ITEM_IMAGE_H), 
//								new ImageCallBack() {
//
//							@Override
//							public void imageLoad(ImageView imageView, Bitmap bitmap) {
//								if (bitmap == null) {
//									bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.list_item_ic_default);
//								}
//								imageView.setImageBitmap(bitmap);
//							}
//						});
//
//						if (bmp != null) {
//							newsDetailImg.setImageBitmap(bmp);
//						}
//						
//					}
//					
//				}
			}
		});
	}

}
