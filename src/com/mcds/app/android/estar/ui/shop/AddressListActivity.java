package com.mcds.app.android.estar.ui.shop;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.Address;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.ui.BaseActivity;

/**
 * 收货地址列表
 * 
 * @author Sky
 * 
 */
public class AddressListActivity extends BaseActivity {

	private AddressAdapter adapter;
	private ListView lv;
	private ReturnResult<Address> rrAddress;
	private ReturnResult<String> rrSetDefaultAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_address_list);

		findViewById(R.id.shopAddressList_btnAdd).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), AddressEditActivity.class);
				startActivityForResult(intent, 10);
			}
		});

		lv = (ListView) findViewById(R.id.shopAddressList_lv);
		adapter = new AddressAdapter();
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Address item = (Address) arg0.getAdapter().getItem(arg2);

				setDefaultAddress(item.address_id);
			}
		});

		getData();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (10 == resultCode) {
			finish();
//			getData();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void setDefaultAddress(final String address_id) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					rrSetDefaultAddress = ConnectProvider.setDefaultAddress(UserManager.uid, address_id);

					doSetDefaultUI();
				}

				hideWaitingDialog();
			}
		}).start();
	}

	private void doSetDefaultUI() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (rrSetDefaultAddress != null) {
					if (rrSetDefaultAddress.status.equals("0")) {
						finish();
					} else {
						Toast.makeText(getApplicationContext(), rrSetDefaultAddress.info, Toast.LENGTH_LONG).show();
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
					rrAddress = ConnectProvider.getAddressList(UserManager.uid);
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
				if (rrAddress != null) {
					if (rrAddress.status.equals("0")) {
						adapter.setItems(rrAddress.getDatas());
						adapter.notifyDataSetChanged();
					} else {
						Toast.makeText(getApplicationContext(), rrAddress.info, Toast.LENGTH_LONG).show();
					}
				}
			}
		});
	}

	private class AddressAdapter extends BaseListAdapter<Address> {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.shop_address_list_item, null);

			Address item = this.getItem(position);

			((TextView) convertView.findViewById(R.id.shopAddressListItem_txtName)).setText(item.name);
			((TextView) convertView.findViewById(R.id.shopAddressListItem_txtPhone)).setText(item.phone);
			((TextView) convertView.findViewById(R.id.shopAddressListItem_txtPostalCode)).setText(item.postal_code);
			((TextView) convertView.findViewById(R.id.shopAddressListItem_txtAddress)).setText(item.address);

			if (item.is_default.equals("1")) {
				convertView.findViewById(R.id.shopAddressListItem_bg).setBackgroundColor(Color.argb(255, 0, 126, 0));
				convertView.findViewById(R.id.shopAddressListItem_icIsDefault).setVisibility(View.VISIBLE);
			}

			return convertView;
		}
	}
}
