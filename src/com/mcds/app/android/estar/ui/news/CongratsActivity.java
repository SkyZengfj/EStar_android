package com.mcds.app.android.estar.ui.news;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.BirthdayList;
import com.mcds.app.android.estar.bean.NewsList;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProviderZX;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.ui.BaseTabActivity;

public class CongratsActivity extends BaseTabActivity {

	private ScrollView birthSV;
    private LinearLayout container1, container2;
    private View view;
    private ReturnResult<BirthdayList> rrBirth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_birthday);
		initTabNavigate(R.id.news_birthday_tabNavigate, TAB_INDEX_NEWS);
		init();
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
					rrBirth = ConnectProviderZX.getBirthdayList(UserManager.uid);
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
				if (rrBirth.status.equals("0")) {
					DisplayMetrics metrics = new DisplayMetrics();
					getWindowManager().getDefaultDisplay().getMetrics(metrics);
					for(int i = 0; i < rrBirth.getDatas().size(); i ++){
						BirthdayList bitem = rrBirth.getDatas().get(i);
						View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.news_birthday_item, null);
						TextView nametxt = (TextView)view.findViewById(R.id.news_birth_item_name);
						TextView contenttxt = (TextView)view.findViewById(R.id.news_birth_item_content);
//						TextView appnumtxt = (TextView)view.findViewById(R.id.news_birth_appraise_num);
						nametxt.setText(bitem.received_name);
						contenttxt.setText(bitem.content);
//						appnumtxt.setText(bitem.praise);
						((TextView)view.findViewById(R.id.news_birth_from)).setText(bitem.from_name);
						
						ImageView cardImg = (ImageView)view.findViewById(R.id.news_birth_item_img);
						int width = (metrics.widthPixels * 311) / 720;
						int height = (metrics.widthPixels * 230) / 720;

						Bitmap bmp = asyncLoader.loadBitmap(cardImg, bitem.card_imageurl, width, height, new ImageCallBack() {

							@Override
							public void imageLoad(ImageView imageView, Bitmap bitmap) {
								if (bitmap == null) {
									bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_default_action_list_item);
								}
								imageView.setImageBitmap(bitmap);
							}
						});

						if (bmp != null) {
							cardImg.setImageBitmap(bmp);
						}
						
						
						if(i%2==0){
							//偶数
							container1.addView(view);
						}else{
							//奇数
							container2.addView(view);
						}
						
					}
					
				}
			}
		});
	}

	private void init() {
		// TODO Auto-generated method stub
		birthSV = (ScrollView)this.findViewById(R.id.news_birthday_scrollview);
		container1 = (LinearLayout)this.findViewById(R.id.news_birth_container_1);
		container2 = (LinearLayout)this.findViewById(R.id.news_birth_container_2);
		
		
		
	}
}
