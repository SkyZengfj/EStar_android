package com.mcds.app.android.estar.ui.shop;

import com.mcds.app.android.estar.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 购物车中修改商品购买数量的弹出对话框
 * @author Sky
 *
 */
public class ChangeCountDialog extends Dialog {

	private String giftName;
	private int number = 0;
	private int maxNum = 0;
	private String code;
	private boolean isOk;

	public ChangeCountDialog(Context context) {
		super(context, R.style.DialogTheme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_shopcar_change_count_dialog);

		findViewById(R.id.dialogChangeNumber_btnOk).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				isOk = true;
				dismiss();
			}
		});

		findViewById(R.id.dialogChangeNumber_btnCancel).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				isOk = false;
				dismiss();
			}
		});

		findViewById(R.id.dialogChangeNumber_btnDecrease).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				changeNumber(false);
			}
		});

		findViewById(R.id.dialogChangeNumber_btnIncrease).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				changeNumber(true);
			}
		});

		setTxtNumber();
	}

	public boolean isOk() {
		return this.isOk;
	}

	public void setNumber(int num) {
		this.number = num;
	}

	public int getNumber() {
		return number;
	}

	public void setMaxNumber(int num) {
		this.maxNum = num;
	}

	public int getMaxNumber() {
		return this.maxNum;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public void setGiftName(String name) {
		this.giftName = name;
	}

	private void changeNumber(boolean increase) {
		if (increase) {
			number++;
		} else {
			number--;
		}
		if (number < 1) {
			number = 1;
		}
		setTxtNumber();
	}

	private void setTxtNumber() {
		if (number == 1) {
			findViewById(R.id.dialogChangeNumber_btnDecrease).setBackgroundResource(R.drawable.shop_shopcar_btn_change_count_decrease_disabled);
		} else {
			findViewById(R.id.dialogChangeNumber_btnDecrease).setBackgroundResource(R.drawable.shop_shopcar_btn_change_count_decrease);
		}

		if (number == maxNum) {
			findViewById(R.id.dialogChangeNumber_btnIncrease).setBackgroundResource(R.drawable.shop_shopcar_btn_change_count_increase_disabled);
		} else {
			findViewById(R.id.dialogChangeNumber_btnIncrease).setBackgroundResource(R.drawable.shop_shopcar_btn_change_count_increase);
		}

		((EditText) findViewById(R.id.dialogChangeNumber_txtNumber)).setText(String.valueOf(number));

		((TextView) findViewById(R.id.dialogChangeNumber_txtGiftName)).setText(this.giftName);
	}
}
