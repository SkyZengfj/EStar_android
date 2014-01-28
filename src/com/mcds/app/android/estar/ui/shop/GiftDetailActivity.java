package com.mcds.app.android.estar.ui.shop;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.GiftDetail;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.Shopcar;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.CacheProvider;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.DatabaseProvider;
import com.mcds.app.android.estar.ui.BaseActivity;

/**
 * 商品详情
 * 
 * @author Sky
 * 
 */
public class GiftDetailActivity extends BaseActivity {

	private String code;
	private ReturnResult<GiftDetail> rrGiftDetail;

	private LinearLayout icGiftContainer;
	private ViewPager icGiftViewPager;
	private ArrayList<View> icGiftPageViews;
	private PagerAdapter icGiftPagerAdapter;
	private OnPageChangeListener icGiftPageChangeListener;
	private int icGiftCurrentIndex = 0;
	private int icGiftWidth = 0;
	private int icGiftIcHeight = 0;
	private LinearLayout icGiftCursorContainer;
	private List<ImageView> icGiftCursors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_gift_detail);

		code = getIntent().getStringExtra("code");

		findViewById(R.id.shopGiftDetail_btnDetailContent).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), GiftDetailContentActivity.class);
				intent.putExtra("code", code);
				startActivity(intent);
			}
		});

		findViewById(R.id.shopGiftDetail_btnBuy).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (addShopcar()) {
					startActivity(new Intent(getApplicationContext(), ShopcarActivity.class));
				} else {
					Toast.makeText(getApplicationContext(), "加入购物车失败！", Toast.LENGTH_LONG).show();
				}
			}
		});

		findViewById(R.id.shopGiftDetail_btnAddShopcar).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (addShopcar()) {
					Toast.makeText(getApplicationContext(), "加入购物车成功！", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(), "加入购物车失败！", Toast.LENGTH_LONG).show();
				}
			}
		});

		initIcGift();

		getData();
	}

	private void initIcGift() {
		icGiftWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
		icGiftIcHeight = (icGiftWidth * 540) / 720;

		icGiftContainer = (LinearLayout) findViewById(R.id.shopGiftDetail_icGiftContainer);
		android.view.ViewGroup.LayoutParams params = icGiftContainer.getLayoutParams();
		params.height = icGiftIcHeight;
		params.width = icGiftWidth;
		icGiftContainer.setLayoutParams(params);

		icGiftPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return icGiftPageViews.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(icGiftPageViews.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(icGiftPageViews.get(position));
				return icGiftPageViews.get(position);
			}
		};

		icGiftPageChangeListener = new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				icGiftCurrentIndex = position;
				setIcGiftCursor();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		};

		icGiftViewPager = new ViewPager(this);
		icGiftContainer.addView(icGiftViewPager);
		icGiftViewPager.setOnPageChangeListener(icGiftPageChangeListener);
		icGiftPageViews = new ArrayList<View>();
		icGiftViewPager.setAdapter(icGiftPagerAdapter);

	}

	private void initIcGiftCursor(int count) {
		if (count <= 0) {
			return;
		}

		icGiftCursorContainer = (LinearLayout) findViewById(R.id.shopGiftDetail_icGiftCursorContainer);
		icGiftCursors = new ArrayList<ImageView>();

		ImageView ivFirst = new ImageView(getApplicationContext());
		ivFirst.setBackgroundResource(R.drawable.shop_gift_detail_ic_gift_cursor_selected);
		icGiftCursors.add(ivFirst);
		icGiftCursorContainer.addView(ivFirst);

		for (int i = 1; i < count; i++) {
			ImageView iv = new ImageView(getApplicationContext());
			iv.setBackgroundResource(R.drawable.shop_gift_detail_ic_gift_cursor_normal);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(20, 0, 0, 0);
			iv.setLayoutParams(lp);
			icGiftCursors.add(iv);
			icGiftCursorContainer.addView(iv);
		}

		findViewById(R.id.shopGiftDetail_icGiftCursorContainerBg).setVisibility(View.VISIBLE);
	}

	private void setIcGiftCursor() {
		if (icGiftCurrentIndex < icGiftCursors.size()) {
			for (int i = 0; i < icGiftCursors.size(); i++) {
				icGiftCursors.get(i).setBackgroundResource(R.drawable.shop_gift_detail_ic_gift_cursor_normal);
			}
			icGiftCursors.get(icGiftCurrentIndex).setBackgroundResource(R.drawable.shop_gift_detail_ic_gift_cursor_selected);
		}
	}

	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					rrGiftDetail = ConnectProvider.getProductDetail(UserManager.uid, code);

					doUI();
				}

				hideWaitingDialog();
			}
		}).start();
	}

	private void doUI() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (rrGiftDetail != null) {
					if (rrGiftDetail.status.equals("0")) {
						if (rrGiftDetail.getDatas().size() > 0) {
							GiftDetail item = rrGiftDetail.getDatas().get(0);
							((TextView) findViewById(R.id.shopGiftDetail_txtName)).setText(item.product_title);
							((TextView) findViewById(R.id.shopGiftDetail_txtIntegral)).setText(item.integral);
							((TextView) findViewById(R.id.shopGiftDetail_txtCode)).setText(item.code);
							((TextView) findViewById(R.id.shopGiftDetail_txtStock)).setText(item.stock);

							for (int i = 0; i < item.imgs.size(); i++) {
								ImageView ic = new ImageView(getApplicationContext());
								Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.shop_detail_gift_default);
								ic.setImageBitmap(bmp);
								ic.setTag(item.imgs.get(i));
								icGiftPageViews.add(ic);
								new AsyncIcGiftViewTask().execute(ic);
							}

							initIcGiftCursor(item.imgs.size());

							icGiftContainer.setBackgroundDrawable(null);
							icGiftPagerAdapter.notifyDataSetChanged();
						}
					} else {
						Toast.makeText(getApplicationContext(), rrGiftDetail.info, Toast.LENGTH_LONG).show();
					}
				}
			}
		});
	}

	private boolean addShopcar() {
		if (rrGiftDetail != null && rrGiftDetail.status.equals("0") && rrGiftDetail.getDatas().size() > 0) {
			GiftDetail giftItem = rrGiftDetail.getDatas().get(0);
			Shopcar shopcarItem = new Shopcar();
			shopcarItem.count = 1;
			shopcarItem.img = getIntent().getStringExtra("img");
			shopcarItem.integral = Integer.valueOf(giftItem.integral);
			shopcarItem.name = giftItem.product_title;
			shopcarItem.productCode = giftItem.code;
			return DatabaseProvider.setShopcar(shopcarItem);
		}
		return false;
	}

	private class AsyncIcGiftViewTask extends AsyncTask<ImageView, Void, Void> {

		private Bitmap bmp;
		private ImageView icView;

		@Override
		protected Void doInBackground(ImageView... params) {
			ImageView view = params[0];
			if (view != null) {
				if (view.getTag() != null) {
					String url = view.getTag().toString();
					bmp = CacheProvider.getBitmap(url, icGiftWidth, icGiftIcHeight);
				}
			}

			icView = view;
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (bmp != null) {
				this.icView.setImageBitmap(bmp);
			}
		}

	}
}
