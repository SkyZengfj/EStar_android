package com.mcds.app.android.estar.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.My_FriendsDeclaration;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.ui.BaseTabActivity;

/**
 * 设置我的交友宣言
 * @author chenliang
 *
 */
public class My_FriendsDeclarationActivity extends BaseTabActivity implements OnClickListener{
	private ImageButton my_btn_setFriendsDeclaration_complete;
	private ImageButton my_document_setFriendsDeclaration_back;
	private ImageView my_document_cancel;
	private EditText my_edit_setFriendsDeclaration;
	private TextView my_txt_friendsDeclarationNum;
	private ReturnResult<My_FriendsDeclaration> rrFriendsDeclaration; 

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_set_friends_declaration);
		my_document_setFriendsDeclaration_back = (ImageButton) findViewById(R.id.my_document_setFriendsDeclaration_back);
		my_document_setFriendsDeclaration_back.setOnClickListener(this);
		my_btn_setFriendsDeclaration_complete = (ImageButton) findViewById(R.id.my_btn_setFriendsDeclaration_complete);
		my_btn_setFriendsDeclaration_complete.setOnClickListener(this);
		my_edit_setFriendsDeclaration = (EditText) findViewById(R.id.my_edit_setFriendsDeclaration);
		my_txt_friendsDeclarationNum = (TextView) findViewById(R.id.my_txt_friendsDeclarationNum);
		my_edit_setFriendsDeclaration.addTextChangedListener(mTextWatcher);
		
		
		Intent getIntent =getIntent();
		String friendString = getIntent.getStringExtra("friendString");
		System.out.println(friendString);
		my_edit_setFriendsDeclaration.setHint(friendString);
	}

    
    TextWatcher mTextWatcher = new TextWatcher() {  
        private CharSequence temp;  
        private int editStart ;  
        private int editEnd ;  
        @Override  
        public void onTextChanged(CharSequence s, int start, int before, int count) {  
            // TODO Auto-generated method stub  
             temp = s;  
             
        }  
          
        @Override  
        public void beforeTextChanged(CharSequence s, int start, int count,  
                int after) {  
            // TODO Auto-generated method stub  
//          my_txt_friendsDeclarationNum.setText(s);//将输入的内容实时显示  
        }  
          
        public void afterTextChanged(Editable s) {  
            // TODO Auto-generated method stub  
            editStart = my_edit_setFriendsDeclaration.getSelectionStart();  
            editEnd = my_edit_setFriendsDeclaration.getSelectionEnd();  
            my_txt_friendsDeclarationNum.setText("您输入了" + temp.length() + "个字符");  
            if (temp.length() > 120) {  
                Toast.makeText(My_FriendsDeclarationActivity.this,  
                        "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT)  
                        .show();  
                s.delete(editStart-1, editEnd);  
                int tempSelection = editStart;  
                my_edit_setFriendsDeclaration.setText(s);  
                my_edit_setFriendsDeclaration.setSelection(tempSelection);  
            }  
        }

	 
    };  
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_btn_setFriendsDeclaration_complete:
				new Thread(new Runnable() {
					 
					@Override 
					public void run() {
						showWaitingDialog();

						if (!UserManager.uid.equals("")) {
							rrFriendsDeclaration= ConnectProvider.setDeclaration(UserManager.uid, my_edit_setFriendsDeclaration.getText().toString());
						}
						
						if (rrFriendsDeclaration.status.equals("0")) {
							System.out.println("关注返回结果："+rrFriendsDeclaration.info);
							hideWaitingDialog();
							My_FriendsDeclarationActivity.this.runOnUiThread( new Runnable() {
								public void run() {
									Toast.makeText(getApplicationContext(), "恭喜您，设定成功",Toast.LENGTH_SHORT).show();  
									Intent intent= new Intent();
									intent.putExtra("123", my_edit_setFriendsDeclaration.getText().toString());
									setResult(123, intent);
									finish();
								}
							});
								
						}else {
							Toast.makeText(getApplicationContext(), "设置失败，请检查网络是否正常连接",Toast.LENGTH_SHORT).show();
						}
						hideWaitingDialog();
					
					}
				}).start();
				
			break;
		
		case R.id.my_document_setFriendsDeclaration_back:
			Intent intent= new Intent(My_FriendsDeclarationActivity.this,DocumentActivity.class);
			startActivity(intent);
			break;
		
		default:
			break;
		}

	}
}

