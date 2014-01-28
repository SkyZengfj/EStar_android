package com.mcds.app.android.estar.provider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.mcds.app.android.estar.util.BitmapUtility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class CacheProvider {

	public static void init() throws IOException {
		File file = SdCardProvider.getApplicationFile("Cache");
		if (!file.exists()) {
			file.mkdir();
		}
	}

	public static Bitmap getBitmap(String url, int width, int height) {
		Bitmap bmp = null;
		String fileName = Base64.encodeToString(url.getBytes(), Base64.NO_WRAP);
		fileName = fileName + "-w-" + width + "-h-" + height;
		byte[] buffer = getFileStream(fileName);
		boolean isCached = true;
		if (buffer == null) {
			isCached = false;
			buffer = BitmapUtility.getHttpBitmapBuffer(url, width, height);
		}
		if (buffer != null) {
			bmp = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
			if (!isCached) {
				putFileStream(fileName, buffer);
			}
		}
		return bmp;
	}

	private static byte[] getFileStream(String fileName) {
		byte[] buffer = null;
		try {
			File file = SdCardProvider.getApplicationFile("Cache/" + fileName);
			if (file.exists()) {
				FileInputStream stream = new FileInputStream(file);
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
			}
		} catch (IOException ioe) {
			buffer = null;
		}

		return buffer;
	}

	private static void putFileStream(String fileName, byte[] buffer) {
		if (buffer == null) {
			return;
		}

		try {
			File file = SdCardProvider.getApplicationFile("Cache/" + fileName);
			FileOutputStream fileStream = new FileOutputStream(file);
			fileStream.write(buffer);
			fileStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
