package com.mcds.app.android.estar.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import com.mcds.app.android.estar.util.BitmapUtility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.widget.ImageView;

public class AsyncBitmapLoader {

	// 内存图片软引用缓冲
	private HashMap<String, SoftReference<Bitmap>> imageCache = null;

	public AsyncBitmapLoader() {
		imageCache = new HashMap<String, SoftReference<Bitmap>>();
	}

	public Bitmap loadBitmap(final ImageView imageView, final String imageUrl, final int width, final int height, final ImageCallBack imageCallBack) {
		final String fileName = Base64.encodeToString(imageUrl.getBytes(), Base64.NO_WRAP) + "-w-" + width + "-h-" + height;

		// 在内存缓存中，则返回Bitmap对象
		Bitmap bmp = getMemoryCacheBmp(fileName);
		if (bmp != null) {
			return bmp;
		}

		bmp = getCacheBmp(fileName);
		if (bmp != null) {
			return bmp;
		}

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				imageCallBack.imageLoad(imageView, (Bitmap) msg.obj);
			}
		};

		// 如果不在内存缓存中，也不在本地（被jvm回收掉），则开启线程下载图片
		new Thread() {
			@Override
			public void run() {
				Bitmap bmp = BitmapUtility.getHttpBitmap(imageUrl, width, height);
				putMemoryCacheBmp(fileName, bmp);
				putCacheBmp(fileName, bmp);

				Message msg = handler.obtainMessage(0, bmp);
				handler.sendMessage(msg);
			}
		}.start();

		return null;
	}

	/**
	 * 回调接口
	 * 
	 */
	public interface ImageCallBack {
		public void imageLoad(ImageView imageView, Bitmap bitmap);
	}

	private Bitmap getMemoryCacheBmp(String fileName) {
		if (imageCache.containsKey(fileName)) {
			SoftReference<Bitmap> reference = imageCache.get(fileName);
			Bitmap bmp = reference.get();
			if (bmp != null) {
				return bmp;
			}
		}
		return null;
	}

	private void putMemoryCacheBmp(String fileName, Bitmap bmp) {
		if (imageCache.containsKey(fileName)) {
			imageCache.remove(fileName);
		}
		imageCache.put(fileName, new SoftReference<Bitmap>(bmp));
	}

	private Bitmap getCacheBmp(String fileName) {
		try {
			File file = SdCardProvider.getApplicationFile("Cache/" + fileName);
			if (file.exists()) {
				FileInputStream stream = null;
				try {
					stream = new FileInputStream(file);
					Bitmap bmp = BitmapFactory.decodeStream(stream);
					if (bmp != null) {
						return bmp;
					}
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {}

		return null;
	}

	private void putCacheBmp(String fileName, Bitmap bmp) {
		try {
			File file = SdCardProvider.getApplicationFile("Cache/" + fileName);
			if (file.exists()) {
				file.delete();
			}

			file.createNewFile();
			FileOutputStream fileStream = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, fileStream);
			fileStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
