package com.mcds.app.android.estar.ui.shop;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.Order;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.ui.BaseActivity;

/**
 * 订单列表
 * 
 * @author Sky
 * 
 */
public class OrderListActivity extends BaseActivity {

	private ReturnResult<Order> rrOrder;
	private ListView lv;
	private OrderAdapter adapter;

	private String order_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_order_list);

		lv = (ListView) findViewById(R.id.shopOrderList_lv);
		adapter = new OrderAdapter();
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Order item = (Order) arg0.getAdapter().getItem(arg2);
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), OrderDetailActivity.class);
				intent.putExtra("code", item.code);
//				startActivity(intent);
				startActivityForResult(intent, 10);
			}
		});

		findViewById(R.id.shopOrderList_btnThisMonth).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				arg0.setBackgroundResource(R.drawable.shop_list_btn_type_selected_bg);
				((TextView) findViewById(R.id.shopOrderList_txtThisMonth)).setTextColor(Color.argb(255, 235, 70, 102));

				findViewById(R.id.shopOrderList_btnOlder).setBackgroundResource(R.drawable.shop_list_btn_type_bg);
				((TextView) findViewById(R.id.shopOrderList_txtOlder)).setTextColor(Color.argb(255, 68, 68, 68));

				order_type = "0";
				getData();
			}
		});

		findViewById(R.id.shopOrderList_btnOlder).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				arg0.setBackgroundResource(R.drawable.shop_list_btn_type_selected_bg);
				((TextView) findViewById(R.id.shopOrderList_txtOlder)).setTextColor(Color.argb(255, 235, 70, 102));

				findViewById(R.id.shopOrderList_btnThisMonth).setBackgroundResource(R.drawable.shop_list_btn_type_bg);
				((TextView) findViewById(R.id.shopOrderList_txtThisMonth)).setTextColor(Color.argb(255, 68, 68, 68));

				order_type = "1";
				getData();
			}
		});

		order_type = "0";
		getData();
	}

	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					rrOrder = ConnectProvider.getOrderList(UserManager.uid, order_type, "0", "10");

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
				if (rrOrder != null) {
					if (rrOrder.status.equals("0")) {
						adapter.setItems(rrOrder.getDatas());
						adapter.notifyDataSetChanged();
					} else {
						Toast.makeText(getApplicationContext(), rrOrder.info, Toast.LENGTH_LONG).show();
					}
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (10 == resultCode) {
			getData();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private class OrderAdapter extends BaseListAdapter<Order> {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.shop_order_list_item, null);

			Order item = getItem(position);
			((TextView) convertView.findViewById(R.id.shopOrderListItem_txtCode)).setText(item.code);
			((TextView) convertView.findViewById(R.id.shopOrderListItem_txtIntegral)).setText(item.total_integral);
			((TextView) convertView.findViewById(R.id.shopOrderListItem_txtStatus)).setText(item.status);
			((TextView) convertView.findViewById(R.id.shopOrderListItem_txtDate)).setText(DateFormat.format("yyyy-MM-dd hh:mm:ss", item.create_time));
			return convertView;
		}

	}
}
