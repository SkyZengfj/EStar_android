package com.mcds.app.android.estar.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class BitmapUtility {

	private static String bitmapHelperUrlString = "http://www.dlb666.com/index.php/ImagesClass-setThumbs-url-";

	public static Bitmap getHttpBitmap(String url, int width, int height) {
		URL myFileURL;
		Bitmap bitmap = null;
		try {

			String urlCode = Base64.encodeToString(url.getBytes(), Base64.NO_WRAP);
			if (width == 0 || height == 0) {
				myFileURL = new URL(bitmapHelperUrlString + urlCode);
			} else {
				myFileURL = new URL(bitmapHelperUrlString + urlCode + "-w-" + width + "-h-" + height);
			}
			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
			// 设置超时时间为5000毫秒
			conn.setConnectTimeout(5000);
			// 连接设置获得数据流
			conn.setDoInput(true);
			// 不使用缓存
			conn.setUseCaches(false);
			// 得到数据流
			InputStream is = conn.getInputStream();
			try {
				// 解析得到图片
				BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inJustDecodeBounds = false;
				opt.outWidth = width;
				opt.outHeight = height;
				bitmap = BitmapFactory.decodeStream(is, null, opt);
			} catch (Exception e) {
				bitmap = null;
			} finally {
				// 关闭数据流
				is.close();
			}

			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	public static byte[] getHttpBitmapBuffer(String url, int width, int height) {
		byte[] buffer = null;
		HttpURLConnection conn = null;
		try {
			String urlCode = Base64.encodeToString(url.getBytes(), Base64.NO_WRAP);
			URL myFileURL;
			if (width == 0 || height == 0) {
				myFileURL = new URL(bitmapHelperUrlString + urlCode);
			} else {
				myFileURL = new URL(bitmapHelperUrlString + urlCode + "-w-" + width + "-h-" + height);
			}

			conn = (HttpURLConnection) myFileURL.openConnection();
			conn.setConnectTimeout(60000);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			InputStream stream = conn.getInputStream();
			if (stream != null) {
				ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
				int cursor;
				while ((cursor = stream.read()) != -1) {
					swapStream.write(cursor);
				}

				buffer = swapStream.toByteArray();
				stream.close();
				swapStream.close();
			}

		} catch (Exception e) {
			buffer = null;
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return buffer;
	}
}
