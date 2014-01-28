package com.mcds.app.android.estar.ui.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.ShopcarGiftListAdapter;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.Shopcar;
import com.mcds.app.android.estar.provider.DatabaseProvider;
import com.mcds.app.android.estar.ui.BaseActivity;

/**
 * 支付界面
 * @author Sky
 *
 */
public class PayActivity extends BaseActivity {

	private ReturnResult<Shopcar> rr;
	private ListView lv;
	private ShopcarGiftListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_pay);

		lv = (ListView) findViewById(R.id.shopPay_lv);
		adapter = new ShopcarGiftListAdapter(PayActivity.this);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Shopcar item = (Shopcar) arg0.getAdapter().getItem(arg2);
				Intent intent = new Intent();
				intent.setClass(PayActivity.this, GiftDetailActivity.class);
				intent.putExtra("code", item.productCode);
				startActivity(intent);
			}
		});

		getData();
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
//					List<Shopcar> items = rr.getDatas();
//
//					int sumIntegral = 0;
//					int giftCount = items.size();
//					for (int i = 0; i < giftCount; i++) {
//						sumIntegral += items.get(i).integral * items.get(i).count;
//					}

//					((TextView) findViewById(R.id.shopSubmitOrder_txtSumIntegral)).setText(String.valueOf(sumIntegral));

					adapter.setItems(rr.getDatas());
//					LayoutUtility.setListViewHeightBasedOnChildren(lv);
					adapter.notifyDataSetChanged();
				}
			}
		});
	}
}
