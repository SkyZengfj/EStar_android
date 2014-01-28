package com.mcds.app.android.estar.ui.news;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.NewsList;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.provider.ConnectProviderZX;
import com.mcds.app.android.estar.ui.BaseTabActivity;
import com.mcds.app.android.estar.util.LayoutUtility;
import com.mcds.app.android.estar.util.Utility;

/**
 * 新闻资讯
 * @author Administrator
 *
 */
public class NewsActivity extends BaseTabActivity{

	private ListView newsListView;
	private NewsAdapter adapter;
	private ReturnResult<NewsList> rrNews;
	private ImageView newsListImgBig;
	private TextView newsListTitleBig;
	private ScrollView scrollView;
	
	private Runnable scrolltorun = new Runnable() {

		@Override
		public void run() {
			scrollView.scrollTo(0, 0);// 改变滚动条的位置
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_news);
		initTabNavigate(R.id.newsNews_tabNavigate, TAB_INDEX_NEWS);
		initListView();
	}

	private void initListView() {
		// TODO Auto-generated method stub
		scrollView = (ScrollView)findViewById(R.id.news_news_big_layout);
		newsListView = (ListView)findViewById(R.id.newsNews_listView);
		newsListImgBig = (ImageView)findViewById(R.id.news_news_big_imgs);
		newsListTitleBig = (TextView)findViewById(R.id.news_news_big_title);
		adapter = new NewsAdapter();
		newsListView.setAdapter(adapter);
		
		newsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.news.NewsDetailActivity.class);
				Bundle bundle = new Bundle();
				if(rrNews != null){
//					bundle.putString("news_id", rrNews.getDatas().get(position).news_id);  
					bundle.putSerializable("NewsList", (NewsList)rrNews.getDatas().get(position));
				}
                intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		getData();
	}
	
	/**
	 * 获取活动列表
	 */
	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					rrNews = ConnectProviderZX.getCompanyNewsList(UserManager.uid, "0", "10");
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

			@Override
			public void run() {
				if (rrNews.status.equals("0")) {
//					((TextView)findViewById(R.id.news_news_big_title)).setText(rrNews.getDatas().get(0).news_title);
//					((TextView)findViewById(R.id.news_news_big_title)).setText(rrNews.getDatas().get(0).news_title);
					adapter.setItems(rrNews.getDatas());
					LayoutUtility.setListViewHeightBasedOnChildren(newsListView);
					Handler handler = new Handler();
					handler.postDelayed(scrolltorun, 200); 
					adapter.notifyDataSetChanged();
				}
			}
		});
	}
	
	private class NewsAdapter extends BaseListAdapter<NewsList>{
		
		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null){
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.news_newslist_item, null);
				holder = new ViewHolder();
				holder.newsItemImage = (ImageView)convertView.findViewById(R.id.news_newslist_item_img);
				holder.newsItemTitle = (TextView)convertView.findViewById(R.id.news_newslist_item_title);
				holder.newsItemContent = (TextView)convertView.findViewById(R.id.news_newslist_item_content);
				holder.newsItemAppr = (TextView)convertView.findViewById(R.id.news_newslist_item_appraise);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			if (holder != null) {
				NewsList item = this.getItem(position);
				
				if(position == 0){
					newsListTitleBig.setText(rrNews.getDatas().get(0).news_title);
					DisplayMetrics metrics = new DisplayMetrics();
					getWindowManager().getDefaultDisplay().getMetrics(metrics);
					int width = (metrics.widthPixels * 720) / 720;
					int height = (metrics.widthPixels * 396) / 720;
					Bitmap bmpbig = asyncLoader.loadBitmap(
							newsListImgBig, 
							item.image_url, 
							width, 
							height, 
							new ImageCallBack() {

						@Override
						public void imageLoad(ImageView imageView, Bitmap bitmap) {
							if (bitmap == null) {
								bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.list_item_ic_default);
							}
							imageView.setImageBitmap(bitmap);
						}
					});

					if (bmpbig != null) {
						newsListImgBig.setImageBitmap(bmpbig);
					}
				}
				
				holder.newsItemTitle.setText(item.news_title);
				holder.newsItemContent.setText(item.news_content);
				holder.newsItemAppr.setText(item.news_praise);

				
				
				Bitmap bmp = asyncLoader.loadBitmap(
						holder.newsItemImage, 
						item.image_url, 
						Utility.dip2px(NewsActivity.this, 100), 
						Utility.dip2px(NewsActivity.this, 100), 
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
					holder.newsItemImage.setImageBitmap(bmp);
				}
			}
			return convertView;
		}
		
	}
	
	private class ViewHolder {
		ImageView newsItemImage;
		TextView newsItemTitle;
		TextView newsItemContent;
		TextView newsItemAppr;
	}
}
