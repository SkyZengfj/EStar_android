package com.mcds.app.android.estar.ui.shop;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.Shopcar;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.provider.DatabaseProvider;
import com.mcds.app.android.estar.ui.BaseActivity;

/**
 * 购物车
 * 
 * @author Sky
 * 
 */
public class ShopcarActivity extends BaseActivity {

	private ReturnResult<Shopcar> rr;
	private ListView lv;
	private ShopcarAdapter adapter;

	private ReturnResult<String> rrStock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_shopcar);

		findViewById(R.id.shopShopcar_btnOk).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (rr != null && rr.status.equals("0")) {
					if (rr.getDatas().size() > 0) {
						startActivity(new Intent(ShopcarActivity.this, SubmitOrderActivity.class));
					} else {
						Toast.makeText(getApplicationContext(), "请先选购商品！", Toast.LENGTH_LONG).show();
					}
				}
			}
		});

		lv = (ListView) findViewById(R.id.shopShopcar_lv);
		adapter = new ShopcarAdapter();
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Shopcar item = (Shopcar) arg0.getAdapter().getItem(arg2);
				Intent intent = new Intent();
				intent.setClass(ShopcarActivity.this, GiftDetailActivity.class);
				intent.putExtra("code", item.productCode);
				startActivity(intent);
			}
		});

		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				final Shopcar item = (Shopcar) arg0.getAdapter().getItem(arg2);
				new AlertDialog.Builder(ShopcarActivity.this)//
						.setTitle("确认删除")//
						.setIcon(android.R.drawable.ic_dialog_info)//
						.setMessage("确认从购物车移出商品\"" + item.name + "\"？")//
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								deleteShopcar(item.productCode);
							}
						})//
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						}).show();

				return true;
			}
		});

		findViewById(R.id.shopShopcar_btnClear).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(ShopcarActivity.this)//
						.setTitle("确认清空购物车")//
						.setIcon(android.R.drawable.ic_dialog_info)//
						.setMessage("确认移出购物车所有商品？")//
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (!DatabaseProvider.clearShopcar()) {
									Toast.makeText(getApplicationContext(), "清空购物车失败！", Toast.LENGTH_LONG).show();
								}

								dialog.dismiss();

								getData();
							}
						})//
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						}).show();
			}
		});
	}

	@Override
	protected void onResume() {
		getData();
		super.onResume();
	}

	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				rr = DatabaseProvider.getShopcars();

				doUI();

				hideWaitingDialog();
			}
		}).start();
	}

	private void doUI() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (rr != null && rr.status.equals("0")) {
					List<Shopcar> items = rr.getDatas();

					int sumIntegral = 0;
					int giftCount = items.size();
					for (int i = 0; i < giftCount; i++) {
						sumIntegral += items.get(i).integral * items.get(i).count;
					}

					((TextView) findViewById(R.id.shopShopcar_txtGiftCount)).setText(String.valueOf(giftCount));
					((TextView) findViewById(R.id.shopShopcar_txtSumIntegral)).setText(String.valueOf(sumIntegral));

					adapter.setItems(rr.getDatas());
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	private void setShopcarCount(String code, int count) {
		getStock(code, count);
//		DatabaseProvider.setShopcarCount(code, count);
//		getData();
	}

	private void getStock(final String code, final int count) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					rrStock = ConnectProvider.getStock(UserManager.uid, code);

					doStockUI(code, count);
				}

				hideWaitingDialog();
			}
		}).start();
	}

	private void doStockUI(final String code, final int count) {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (rrStock != null && rrStock.status.equals("0")) {
					int stock = Integer.valueOf(rrStock.getDatas().get(0));
					if (stock < count) {
						Toast.makeText(getApplicationContext(), "库存不足! 仅剩" + stock + "件.", Toast.LENGTH_LONG).show();
					} else {
						DatabaseProvider.setShopcarCount(code, count);
						getData();
					}
				} else {
					Toast.makeText(getApplicationContext(), rrStock.info, Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	private void deleteShopcar(String code) {
		DatabaseProvider.removeShopcar(code);
		getData();
	}

	private class ShopcarAdapter extends BaseListAdapter<Shopcar> {
		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;

			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.shop_shopcar_list_item, null);

				holder = new ViewHolder();
				holder.count = (EditText) convertView.findViewById(R.id.shopShopcarListItem_txtCount);
				holder.img = (ImageView) convertView.findViewById(R.id.shopShopcarListItem_img);
				holder.integral = (TextView) convertView.findViewById(R.id.shopShopcarListItem_txtIntegral);
				holder.name = (TextView) convertView.findViewById(R.id.shopShopcarListItem_txtName);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (holder != null) {
				Shopcar item = this.getItem(position);
				holder.count.setText(String.valueOf(item.count));
				holder.count.setTag(item);
				holder.count.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Shopcar item = (Shopcar) arg0.getTag();
						ChangeCountDialog dialog = new ChangeCountDialog(ShopcarActivity.this);
						dialog.setNumber(item.count);
						dialog.setMaxNumber(10);
						dialog.setCode(item.productCode);
						dialog.setGiftName(item.name);
						dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

							@Override
							public void onDismiss(DialogInterface dialog) {
								ChangeCountDialog d = (ChangeCountDialog) dialog;
								if (d.isOk()) {
									setShopcarCount(d.getCode(), d.getNumber());
								}
							}
						});
						dialog.show();
					}
				});

				holder.integral.setText("积分价：" + item.integral);
				holder.name.setText(item.name);

				Bitmap bmp = asyncLoader.loadBitmap(holder.img, item.img, 100, 100, new ImageCallBack() {

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
		public EditText count;
		public TextView integral;
	}
}
