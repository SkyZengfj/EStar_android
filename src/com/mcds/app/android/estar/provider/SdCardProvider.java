package com.mcds.app.android.estar.provider;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

public class SdCardProvider {
	public static void init() throws IOException {
		boolean isSdCardReady = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if (isSdCardReady) {
			String sdPath = Environment.getExternalStorageDirectory().toString();
			String appPath = sdPath + "/EStar";
			File file = new File(appPath);
			if (!file.exists()) {
				file.mkdir();
			}
		} else {
			throw new IOException("未找到SD卡.");
		}
	}

	public static File getDbFile() throws IOException {
		File file = getApplicationFile("estar.db");
		return file;
	}

	public static File getTempDbFile() throws IOException {
		File file = getApplicationFile("estar_temp.db");
		return file;
	}

	public static File getApplicationFile(String filePath) throws IOException {
		boolean isSdCardReady = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if (isSdCardReady) {
			String sdPath = Environment.getExternalStorageDirectory().toString();
			String appPath = sdPath + "/EStar/" + filePath;
			File file = new File(appPath);
			return file;
		} else {
			throw new IOException("未找到SD卡.");
		}
	}
}
