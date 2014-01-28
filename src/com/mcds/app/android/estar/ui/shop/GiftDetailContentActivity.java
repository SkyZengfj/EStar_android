package com.mcds.app.android.estar.ui.shop;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.util.ConnectUtility;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * 商品图文详情
 * 
 * @author Sky
 * 
 */
public class GiftDetailContentActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_gift_detail_content);

		String code = getIntent().getStringExtra("code");

		WebView wv = (WebView) findViewById(R.id.shopGiftDetailContent_wv);
		wv.getSettings().setBuiltInZoomControls(true);
		wv.getSettings().setUseWideViewPort(true);
		wv.loadUrl(ConnectUtility.GIFT_DETAIL_CONTENT_ADDRESS + code + ".shtml");
	}
}
