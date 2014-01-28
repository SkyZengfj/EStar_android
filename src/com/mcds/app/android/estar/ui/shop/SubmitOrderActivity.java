package com.mcds.app.android.estar.ui.shop;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.ShopcarGiftListAdapter;
import com.mcds.app.android.estar.bean.Address;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.Shopcar;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.DatabaseProvider;
import com.mcds.app.android.estar.ui.BaseActivity;
import com.mcds.app.android.estar.util.LayoutUtility;

/**
 * 提交订单
 * 
 * @author Sky
 * 
 */
public class SubmitOrderActivity extends BaseActivity {

	private ReturnResult<Shopcar> rrShopcar;
	private ListView lv;
	private ShopcarGiftListAdapter adapter;

	private ReturnResult<Address> rrAddress;
	private Address defaultAddress = null;

	private ReturnResult<String> rrSubmitOrder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_submit_order);

		findViewById(R.id.shopSubmitOrder_btnOk).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (defaultAddress == null) {
					Toast.makeText(getApplicationContext(), "请选择收货地址！", Toast.LENGTH_LONG).show();
					return;
				}
				new AlertDialog.Builder(SubmitOrderActivity.this)//
						.setTitle("提交订单")//
						.setIcon(android.R.drawable.ic_dialog_info)//
						.setMessage("确认提交此订单？")//
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								ReturnResult<Shopcar> rrShopcar = DatabaseProvider.getShopcars();
								if (rrShopcar != null && rrShopcar.status.equals("0")) {
									List<Shopcar> products = rrShopcar.getDatas();
									submitOrder(products);
								}
								dialog.dismiss();
							}
						})//
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						}).show();
//				startActivity(new Intent(SubmitOrderActivity.this, PayActivity.class));
			}
		});

		findViewById(R.id.shopSubmitOrder_btnAddress).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(SubmitOrderActivity.this, AddressListActivity.class));
			}
		});

		lv = (ListView) findViewById(R.id.shopSubmitOrder_lv);
		adapter = new ShopcarGiftListAdapter(SubmitOrderActivity.this);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Shopcar item = (Shopcar) arg0.getAdapter().getItem(arg2);
				Intent intent = new Intent();
				intent.setClass(SubmitOrderActivity.this, GiftDetailActivity.class);
				intent.putExtra("code", item.productCode);
				startActivity(intent);
			}
		});

		getData();
	}

	@Override
	protected void onResume() {
		getDefaultAddress();
		super.onResume();
	}

	private void submitOrder(final List<Shopcar> products) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				rrSubmitOrder = ConnectProvider.submitOrder(UserManager.uid, defaultAddress.address_id, products);

				doSubmitOrderUI();

				hideWaitingDialog();
			}
		}).start();

	}

	private void doSubmitOrderUI() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (rrSubmitOrder != null) {
					if (rrSubmitOrder.status.equals("0")) {
						Toast.makeText(getApplicationContext(), "购买成功！", Toast.LENGTH_LONG).show();
						DatabaseProvider.clearShopcar();
						finish();
					} else {
						new AlertDialog.Builder(SubmitOrderActivity.this)//
								.setTitle("错误")//
								.setIcon(android.R.drawable.ic_dialog_info)//
								.setMessage(rrSubmitOrder.info)//
								.setPositiveButton("确定", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
									}
								}).show();
					}
				}
			}
		});
	}

	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				rrShopcar = DatabaseProvider.getShopcars();

				doUI();

				hideWaitingDialog();
			}
		}).start();
	}

	private void doUI() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (rrShopcar != null && rrShopcar.status.equals("0")) {
					List<Shopcar> items = rrShopcar.getDatas();

					int sumIntegral = 0;
					int giftCount = items.size();
					for (int i = 0; i < giftCount; i++) {
						sumIntegral += items.get(i).integral * items.get(i).count;
					}

					((TextView) findViewById(R.id.shopSubmitOrder_txtSumIntegral)).setText(String.valueOf(sumIntegral));

					adapter.setItems(rrShopcar.getDatas());
//					LayoutUtility.setListViewHeightBasedOnChildren(lv);
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	private void getDefaultAddress() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("0")) {
					rrAddress = ConnectProvider.getAddressList(UserManager.uid);
					doDefaultAddressUI();
				}

				hideWaitingDialog();
			}
		}).start();
	}

	private void doDefaultAddressUI() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (rrAddress != null && rrAddress.status.equals("0")) {
					List<Address> items = rrAddress.getDatas();
					for (int i = 0; i < items.size(); i++) {
						if (items.get(i).is_default.equals("1")) {
							defaultAddress = items.get(i);
							break;
						}
					}
				}

				if (defaultAddress != null) {
					((TextView) findViewById(R.id.shopSubmitOrder_txtName)).setText(defaultAddress.name);
					((TextView) findViewById(R.id.shopSubmitOrder_txtPhone)).setText(defaultAddress.phone);
					((TextView) findViewById(R.id.shopSubmitOrder_txtAddress)).setText(defaultAddress.address);
				}
			}
		});
	}
}
