package com.mcds.app.android.estar.ui.shop;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.Area;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.ui.BaseActivity;

/**
 * 新增收货地址
 * 
 * @author Sky
 * 
 */
public class AddressEditActivity extends BaseActivity {

	private Spinner address1Spinner;
	private Spinner address2Spinner;
	private Spinner address3Spinner;
	private ReturnResult<Area> rrArea1;
	private ReturnResult<Area> rrArea2;
	private ReturnResult<Area> rrArea3;
	private ArrayAdapter<String> area1Adapter;
	private ArrayAdapter<String> area2Adapter;
	private ArrayAdapter<String> area3Adapter;
	private String area1Pk = "";
	private String area2Pk = "";
	private String area3Pk = "";

	private String name;
	private String phone;
	private String address;
	private String postalCode;
	private ReturnResult<String> rrNewAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_address_edit);

		initAddress();

		findViewById(R.id.shopAddressEdit_btnOk).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				newAddress();
			}
		});

		loadAddress1();
	}

	private void initAddress() {
		address1Spinner = (Spinner) findViewById(R.id.shopAddressEdit_spinner1);
		address2Spinner = (Spinner) findViewById(R.id.shopAddressEdit_spinner2);
		address3Spinner = (Spinner) findViewById(R.id.shopAddressEdit_spinner3);
		address1Spinner.setPrompt("省/直辖市");
		address2Spinner.setPrompt("市");
		address3Spinner.setPrompt("区");

		area1Adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item);
		area2Adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item);
		area3Adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item);

		address1Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (rrArea1 != null && rrArea1.status.equals("0")) {
					List<Area> items = rrArea1.getDatas();
					if (!area1Pk.equals(items.get(arg2).pk)) {
						area1Pk = items.get(arg2).pk;
						loadAddress2();
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		address2Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (rrArea2 != null && rrArea2.status.equals("0")) {
					List<Area> items = rrArea2.getDatas();
					if (!area2Pk.equals(items.get(arg2).pk)) {
						area2Pk = items.get(arg2).pk;
						loadAddress3();
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		address3Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (rrArea3 != null && rrArea3.status.equals("0")) {
					List<Area> items = rrArea3.getDatas();
					if (!area3Pk.equals(items.get(arg2).pk)) {
						area3Pk = items.get(arg2).pk;
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private void loadAddress1() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					rrArea1 = ConnectProvider.getArea(UserManager.uid, "");

					if (rrArea1 != null && rrArea1.status.equals("0")) {
						AddressEditActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								List<Area> areas = rrArea1.getDatas();
								area1Adapter.clear();
								for (int i = 0; i < areas.size(); i++) {
									area1Adapter.add(areas.get(i).name);
								}

								address1Spinner.setAdapter(area1Adapter);
								address1Spinner.setSelection(0);
							}
						});
					}
				}

				hideWaitingDialog();
			}
		}).start();
	}

	private void loadAddress2() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					rrArea2 = ConnectProvider.getArea(UserManager.uid, area1Pk);

					if (rrArea2 != null && rrArea2.status.equals("0")) {
						AddressEditActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								List<Area> areas = rrArea2.getDatas();
								area2Adapter.clear();
								for (int i = 0; i < areas.size(); i++) {
									area2Adapter.add(areas.get(i).name);
								}

								address2Spinner.setAdapter(area2Adapter);
								address2Spinner.setSelection(0);
							}
						});
					}
				}

				hideWaitingDialog();
			}
		}).start();
	}

	private void loadAddress3() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					rrArea3 = ConnectProvider.getArea(UserManager.uid, area2Pk);

					if (rrArea3 != null && rrArea3.status.equals("0")) {
						AddressEditActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								List<Area> areas = rrArea3.getDatas();
								area3Adapter.clear();
								for (int i = 0; i < areas.size(); i++) {
									area3Adapter.add(areas.get(i).name);
								}

								address3Spinner.setAdapter(area3Adapter);
								address3Spinner.setSelection(0);
							}
						});
					}
				}

				hideWaitingDialog();
			}
		}).start();
	}

	private void newAddress() {
		name = ((TextView) findViewById(R.id.shopAddressEdit_txtName)).getText().toString().trim();
		if (name.length() == 0) {
			Toast.makeText(getApplicationContext(), "请输入收货人姓名.", Toast.LENGTH_LONG).show();
			findViewById(R.id.shopAddressEdit_txtName).requestFocus();
			return;
		}

		phone = ((TextView) findViewById(R.id.shopAddressEdit_txtPhone)).getText().toString().trim();
		if (phone.length() == 0) {
			Toast.makeText(getApplicationContext(), "请输入收货人联系电话.", Toast.LENGTH_LONG).show();
			findViewById(R.id.shopAddressEdit_txtPhone).requestFocus();
			return;
		}

		address = ((TextView) findViewById(R.id.shopAddressEdit_txtAddress)).getText().toString().trim();
		if (address.length() == 0) {
			Toast.makeText(getApplicationContext(), "请输入收货详细地址.", Toast.LENGTH_LONG).show();
			findViewById(R.id.shopAddressEdit_txtAddress).requestFocus();
			return;
		}

		postalCode = ((TextView) findViewById(R.id.shopAddressEdit_txtPostalCode)).getText().toString().trim();
		if (postalCode.length() == 0) {
			Toast.makeText(getApplicationContext(), "请输入收货地址邮编.", Toast.LENGTH_LONG).show();
			findViewById(R.id.shopAddressEdit_txtPostalCode).requestFocus();
			return;
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					rrNewAddress = ConnectProvider.newAddress(UserManager.uid, name, phone, area1Pk, area2Pk, area3Pk, address, postalCode);
					if (rrNewAddress.status.equals("0") && rrNewAddress.getDatas().size() > 0) {
						String address_id = rrNewAddress.getDatas().get(0);
						ConnectProvider.setDefaultAddress(UserManager.uid, address_id);
					}

					doNewAddressUI();
				}

				hideWaitingDialog();
			}
		}).start();
	}

	private void doNewAddressUI() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (rrNewAddress != null) {
					if (rrNewAddress.status.equals("0")) {
						setResult(10);
						finish();
					} else {
						Toast.makeText(getApplicationContext(), rrNewAddress.info, Toast.LENGTH_LONG).show();
					}
				}
			}
		});
	}
}
