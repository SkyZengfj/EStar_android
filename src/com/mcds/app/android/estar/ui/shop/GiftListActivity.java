package com.mcds.app.android.estar.ui.shop;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.Gift;
import com.mcds.app.android.estar.bean.GiftCategory;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.ui.BaseActivity;

/**
 * 商品列表
 * 
 * @author Sky
 * 
 */
public class GiftListActivity extends BaseActivity {

	private MenuDrawer giftCategoryFilterMenuDrawer;
	private LinearLayout giftCategoryFilterLayout;

//  0:按最新上架排序 1：最旧上架排序
//  2:按兑换积分排序（小-大） 3: 积分(大-小)
//  4: 按置顶(默认)
	private String sort_type;
	private String filter_type;
	private String page_num;
	private String page_size;

	private ReturnResult<GiftCategory> rrCategory;
	private ReturnResult<Gift> rrGift;

	private ListView lvGift;
	private GiftAdapter adapterGift;

	private ListView lvGiftCategory;
	private CategoryAdapter adapterGiftCategory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		giftCategoryFilterMenuDrawer = MenuDrawer.attach(this, Position.RIGHT);
		giftCategoryFilterMenuDrawer.closeMenu();
		giftCategoryFilterMenuDrawer.setContentView(R.layout.shop_gift_list);
		giftCategoryFilterMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
		giftCategoryFilterLayout = (LinearLayout) LinearLayout.inflate(this, R.layout.shop_gift_list_filter, null);
		giftCategoryFilterMenuDrawer.setMenuView(giftCategoryFilterLayout);

		findViewById(R.id.shopGiftList_btnFilter).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				giftCategoryFilterMenuDrawer.toggleMenu();
			}
		});

		lvGiftCategory = (ListView) giftCategoryFilterLayout.findViewById(R.id.shopGiftListFilter_lv);
		adapterGiftCategory = new CategoryAdapter();
		lvGiftCategory.setAdapter(adapterGiftCategory);
		lvGiftCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				GiftCategory item = (GiftCategory) arg0.getAdapter().getItem(arg2);

				filter_type = item.id;
				((TextView) giftCategoryFilterLayout.findViewById(R.id.shopGiftListFilter_txtCurrentCategory)).setText(item.name);
				giftCategoryFilterMenuDrawer.toggleMenu();

				getGiftListData();
			}
		});

		lvGift = (ListView) findViewById(R.id.shopGiftList_lv);
		adapterGift = new GiftAdapter();
		lvGift.setAdapter(adapterGift);
		lvGift.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Gift item = (Gift) arg0.getAdapter().getItem(arg2);
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), GiftDetailActivity.class);
				intent.putExtra("code", item.products_code);
				intent.putExtra("img", item.img);
				startActivity(intent);
			}
		});

		findViewById(R.id.shopGiftList_btnDateSort).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!sort_type.equals("0")) {
					sort_type = "0";

					resetBtnSortStatus();
					findViewById(R.id.shopGiftList_btnDateSort).setBackgroundColor(Color.argb(255, 135, 135, 135));
					findViewById(R.id.shopGiftList_icDateSort).setBackgroundResource(R.drawable.shop_list_ic_sort_down_selected);

					getGiftListData();
				}
			}
		});

		findViewById(R.id.shopGiftList_btnIntegralSort).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!sort_type.equals("2")) {
					sort_type = "2";
					resetBtnSortStatus();
					findViewById(R.id.shopGiftList_btnIntegralSort).setBackgroundColor(Color.argb(255, 135, 135, 135));
					findViewById(R.id.shopGiftList_icIntegralSort).setBackgroundResource(R.drawable.shop_list_ic_sort_up_selected);

					getGiftListData();
				} else if (!sort_type.equals("3")) {
					sort_type = "3";
					resetBtnSortStatus();
					findViewById(R.id.shopGiftList_btnIntegralSort).setBackgroundColor(Color.argb(255, 135, 135, 135));
					findViewById(R.id.shopGiftList_icIntegralSort).setBackgroundResource(R.drawable.shop_list_ic_sort_down_selected);

					getGiftListData();
				}
			}
		});

		sort_type = "4";
		filter_type = "0";
		page_num = "0";
		page_size = "10";
		getCategoryData();
		getGiftListData();
	}

	private void resetBtnSortStatus() {
		findViewById(R.id.shopGiftList_btnIntegralSort).setBackgroundColor(Color.argb(255, 153, 152, 152));
		findViewById(R.id.shopGiftList_btnDateSort).setBackgroundColor(Color.argb(255, 153, 152, 152));

		findViewById(R.id.shopGiftList_icDateSort).setBackgroundResource(R.drawable.shop_list_ic_sort_down);
		findViewById(R.id.shopGiftList_icIntegralSort).setBackgroundResource(R.drawable.shop_list_ic_sort_down);
	}

	private void getCategoryData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					rrCategory = ConnectProvider.getGiftCategory(UserManager.uid);

					doCategoryUI();
				}

				hideWaitingDialog();
			}
		}).start();
	}

	private void doCategoryUI() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (rrCategory != null) {
					if (rrCategory.status.equals("0")) {
						List<GiftCategory> items = new ArrayList<GiftCategory>();
						GiftCategory item1 = new GiftCategory();
						item1.id = "0";
						item1.name = "全部";
						items.add(item1);
						items.addAll(rrCategory.getDatas());
						adapterGiftCategory.setItems(items);
						adapterGiftCategory.notifyDataSetChanged();
					} else {
//						Toast.makeText(getApplicationContext(), rrGift.info, Toast.LENGTH_LONG).show();
					}
				}
			}
		});
	}

	private void getGiftListData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					rrGift = ConnectProvider.getInsGiftList(UserManager.uid, sort_type, filter_type, page_num, page_size);

					doGiftListUI();
				}

				hideWaitingDialog();
			}
		}).start();
	}

	private void doGiftListUI() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (rrGift != null) {
					if (rrGift.status.equals("0")) {
						adapterGift.setItems(rrGift.getDatas());
						adapterGift.notifyDataSetChanged();
					} else {
						Toast.makeText(getApplicationContext(), rrGift.info, Toast.LENGTH_LONG).show();
					}
				}
			}
		});
	}

	private class GiftAdapter extends BaseListAdapter<Gift> {
		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;

			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.shop_gift_list_item, null);

				holder = new ViewHolder();
				holder.img = (ImageView) convertView.findViewById(R.id.shopGiftListItem_img);
				holder.name = (TextView) convertView.findViewById(R.id.shopGiftListItem_txtName);
				holder.code = (TextView) convertView.findViewById(R.id.shopGiftListItem_txtCode);
				holder.stock = (TextView) convertView.findViewById(R.id.shopGiftListItem_txtStock);
				holder.integral = (TextView) convertView.findViewById(R.id.shopGiftListItem_txtIntegral);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (holder != null) {
				Gift item = this.getItem(position);
				holder.name.setText(item.product_title);
				holder.code.setText(item.products_code);
				if (Integer.valueOf(item.stock) > 0) {
					holder.stock.setText("有货  下单即可发货！");
				} else {
					holder.stock.setText("缺货");
				}
				holder.integral.setText(item.integral);

				Bitmap bmp = asyncLoader.loadBitmap(holder.img, item.img, 150, 150, new ImageCallBack() {

					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						if (bitmap == null) {
							bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.list_item_ic_default);
						}
						imageView.setImageBitmap(bitmap);
					}
				});

				if (bmp != null) {
					holder.img.setImageBitmap(bmp);
				}
			}

			return convertView;
		}

	}

	private class ViewHolder {
		public ImageView img;
		public TextView name;
		public TextView code;
		public TextView stock;
		public TextView integral;
	}

	private class CategoryAdapter extends BaseListAdapter<GiftCategory> {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.shop_gift_list_filter_list_item, null);

			GiftCategory item = getItem(position);
			((TextView) convertView.findViewById(R.id.shopGiftListFilterListItem_txtName)).setText(item.name);

			return convertView;
		}

	}
}
