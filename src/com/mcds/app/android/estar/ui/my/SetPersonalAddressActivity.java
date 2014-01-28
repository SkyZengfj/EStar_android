package com.mcds.app.android.estar.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.AttentionSomeone;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.SetPersonalAddress;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.ui.BaseTabActivity;

/**
 * 设置个性域名
 * @author Administrator
 *
 */
public class SetPersonalAddressActivity extends BaseTabActivity implements OnClickListener{
	private ImageButton my_btn_personaddress_complete;
	private ImageButton my_document_setpersonaddress_back;
	private ImageView my_document_cancel;
	private EditText my_edit_personAddress;
	private ReturnResult<SetPersonalAddress> rrSetPersonalAddress; 

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_document_personaladdress);
		my_document_setpersonaddress_back = (ImageButton) findViewById(R.id.my_document_setpersonaddress_back);
		my_document_setpersonaddress_back.setOnClickListener(this);
		my_btn_personaddress_complete = (ImageButton) findViewById(R.id.my_btn_personaddress_complete);
		my_btn_personaddress_complete.setOnClickListener(this);
		my_document_cancel = (ImageView) findViewById(R.id.my_document_cancel);
		my_document_cancel.setOnClickListener(this);
		my_edit_personAddress = (EditText) findViewById(R.id.my_edit_personAddress);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_document_cancel:
			my_edit_personAddress.setText("");
			break;
		case R.id.my_btn_personaddress_complete:
				new Thread(new Runnable() {
					 
					@Override 
					public void run() {
						showWaitingDialog();

						if (!UserManager.uid.equals("")) {
							rrSetPersonalAddress = ConnectProvider.setPersonalAddress(UserManager.uid, my_edit_personAddress.getText().toString());
						}
						
						if (rrSetPersonalAddress.status.equals("0")) {
							System.out.println("关注返回结果："+rrSetPersonalAddress.info);
//							if (rrSetPersonalAddress.info.equals("ok")) {
//								Toast.makeText(SetPersonalAddressActivity.this, "个性域名设定成功",Toast.LENGTH_SHORT).show();  
//								Intent intent= new Intent(SetPersonalAddressActivity.this,DocumentActivity.class);
//								startActivity(intent);
//							}
						}
						hideWaitingDialog();
					
					}
				}).start();
				Toast.makeText(SetPersonalAddressActivity.this, "个性域名设定成功",Toast.LENGTH_SHORT).show();  
			break;
		
		case R.id.my_document_setpersonaddress_back:
			Intent intent= new Intent(SetPersonalAddressActivity.this,DocumentActivity.class);
			onDestroy();
			startActivity(intent);
			break;
		
		default:
			break;
		}

	}
}
