package com.mcds.app.android.estar.ui.shop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.ShopcarGiftListAdapter;
import com.mcds.app.android.estar.bean.OrderDetail;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.Shopcar;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.DatabaseProvider;
import com.mcds.app.android.estar.ui.BaseActivity;

/**
 * 订单详情
 * 
 * @author Sky
 * 
 */
public class OrderDetailActivity extends BaseActivity {

	private String code;
	private ReturnResult<OrderDetail> rrOrderDetail;

	private ListView lv;
	private ShopcarGiftListAdapter adapter;

	private ReturnResult<String> rrCancelOrder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_order_detail);

		code = getIntent().getStringExtra("code");

		lv = (ListView) findViewById(R.id.shopOrderDetail_lv);
		adapter = new ShopcarGiftListAdapter(OrderDetailActivity.this);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Shopcar item = (Shopcar) arg0.getAdapter().getItem(arg2);
				Intent intent = new Intent();
				intent.setClass(OrderDetailActivity.this, GiftDetailActivity.class);
				intent.putExtra("code", item.productCode);
				startActivity(intent);
			}
		});

		findViewById(R.id.shopOrderDetail_btnCancel).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new AlertDialog.Builder(OrderDetailActivity.this)//
						.setTitle("确认取消订单")//
						.setIcon(android.R.drawable.ic_dialog_info)//
						.setMessage("确认取消此订单？")//
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								cancelOrder();
								dialog.dismiss();
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

		getData();
	}

	private void cancelOrder() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					rrCancelOrder = ConnectProvider.cancelOrder(UserManager.uid, code);

					doCancelOrderUI();
				}

				hideWaitingDialog();
			}
		}).start();
	}

	private void doCancelOrderUI() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (rrCancelOrder != null) {
					if (rrCancelOrder.status.equals("0")) {
						Toast.makeText(getApplicationContext(), "订单取消成功！", Toast.LENGTH_LONG).show();

						setResult(10);
						finish();
					} else {
						new AlertDialog.Builder(OrderDetailActivity.this)//
								.setTitle("错误")//
								.setIcon(android.R.drawable.ic_dialog_info)//
								.setMessage(rrCancelOrder.info)//
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

				if (!UserManager.uid.equals("")) {
					rrOrderDetail = ConnectProvider.getOrderDetail(UserManager.uid, code);
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
				if (rrOrderDetail != null && rrOrderDetail.status.equals("0")) {
					if (rrOrderDetail.getDatas().size() > 0) {
						OrderDetail item = rrOrderDetail.getDatas().get(0);
						((TextView) findViewById(R.id.shopOrderDetail_txtCode)).setText(item.code);
						((TextView) findViewById(R.id.shopOrderDetail_txtTime)).setText(DateFormat.format("yyyy-MM-dd hh:mm:ss", item.create_time));
						((TextView) findViewById(R.id.shopOrderDetail_txtStatus)).setText(item.status);

						((TextView) findViewById(R.id.shopOrderDetail_txtAddressName)).setText(item.address.name);
						((TextView) findViewById(R.id.shopOrderDetail_txtAddressPhone)).setText(item.address.phone);
						((TextView) findViewById(R.id.shopOrderDetail_txtAddressInfo)).setText(item.address.address);

						if (item.cancelable.equals("1")) {
							findViewById(R.id.shopOrderDetail_btnCancelBg).setVisibility(View.VISIBLE);
						}

						int sumIntegral = 0;
						int giftCount = item.products.size();
						for (int i = 0; i < giftCount; i++) {
							sumIntegral += item.products.get(i).integral * item.products.get(i).count;
						}

						((TextView) findViewById(R.id.shopOrderDetail_txtTotalIntegral)).setText(String.valueOf(sumIntegral));

						adapter.setItems(item.products);
						adapter.notifyDataSetChanged();
					}
				}
			}

		});
	}
}
