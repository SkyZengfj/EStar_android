package com.mcds.app.android.estar.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.Shopcar;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;

public class ShopcarGiftListAdapter extends BaseListAdapter<Shopcar> {
	private Context context;

	public ShopcarGiftListAdapter(Context ctx) {
		this.context = ctx;
	}

	private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(this.context).inflate(R.layout.shop_submit_order_list_item, null);

			holder = new ViewHolder();
			holder.count = (TextView) convertView.findViewById(R.id.shopSubmitOrderListItem_txtCount);
			holder.img = (ImageView) convertView.findViewById(R.id.shopSubmitOrderListItem_img);
			holder.integral = (TextView) convertView.findViewById(R.id.shopSubmitOrderListItem_txtIntegral);
			holder.name = (TextView) convertView.findViewById(R.id.shopSubmitOrderListItem_txtName);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (holder != null) {
			Shopcar item = this.getItem(position);
			holder.count.setText(String.valueOf(item.count));
			holder.integral.setText("积分价：" + item.integral);
			holder.name.setText(item.name);

			Bitmap bmp = asyncLoader.loadBitmap(holder.img, item.img, 100, 100, new ImageCallBack() {

				@Override
				public void imageLoad(ImageView imageView, Bitmap bitmap) {
					if (bitmap == null) {
						bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.list_item_ic_default);
					}
					imageView.setImageBitmap(bitmap);
				}
			});

			if (bmp != null) {
				holder.img.setImageBitmap(bmp);
			}
		}

		return convertView;
	}

	private class ViewHolder {
		public ImageView img;
		public TextView name;
		public TextView count;
		public TextView integral;
	}
}
