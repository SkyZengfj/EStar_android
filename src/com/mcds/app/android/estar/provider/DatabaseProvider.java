package com.mcds.app.android.estar.provider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.mcds.app.android.estar.bean.Shopcar;
import com.mcds.app.android.estar.bean.ReturnResult;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseProvider {
	public static void init(Context ctx) {
		try {
			File dbFile = SdCardProvider.getDbFile();
			if (!dbFile.exists()) {
				copyDB(ctx);
			} else {
				copyTmpDB(ctx);

				int dbVersion = 0;
				SQLiteDatabase db = getDb();
				if (db != null) {
					try {
						String query = "select * from Version where Application='DB'";

						Cursor c = db.rawQuery(query, null);
						if (c != null) {
							c.moveToFirst();
							while (!c.isAfterLast()) {
								dbVersion = c.getInt(c.getColumnIndex("VersionCode"));
								c.moveToNext();
							}
							c.close();
							c = null;
						}
					} catch (Exception e) {} finally {
						db.close();
						db = null;
					}
				}

				int tmpDbVersion = 0;
				SQLiteDatabase tmpDb = getTempDb();
				if (tmpDb != null) {
					try {
						String query = "select * from Version where Application='DB'";

						Cursor c = tmpDb.rawQuery(query, null);
						if (c != null) {
							c.moveToFirst();
							while (!c.isAfterLast()) {
								tmpDbVersion = c.getInt(c.getColumnIndex("VersionCode"));
								c.moveToNext();
							}
							c.close();
							c = null;
						}
					} catch (Exception e) {} finally {
						try {
							tmpDb.close();
							tmpDb = null;
						} catch (Exception e) {}
					}
				}

				if (tmpDbVersion > dbVersion) {
					copyDB(ctx);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean setShopcar(Shopcar item) {
		SQLiteDatabase db = getDb();
		if (db != null) {
			try {
				String query = "select * from Shopcar where ProductCode='" + item.productCode + "'";
				Cursor c = db.rawQuery(query, null);
				boolean hasExisted = false;
				c.moveToFirst();
				while (!c.isAfterLast()) {
					hasExisted = true;

					c.moveToNext();
				}
				c.close();
				c = null;
				if (hasExisted) {
					query = "update Shopcar set " //
							+ "Name='" + item.name + "', Img='" + item.img + "', Integral=" + String.valueOf(item.integral) + ", Count=" + String.valueOf(item.count) + " " //
							+ "where ProductCode='" + item.productCode + "'";
				} else {
					query = "insert into Shopcar(ProductCode, Name, Img, Integral, Count) " //
							+ "values('" + item.productCode + "', '" + item.name + "', '" + item.img + "', " + String.valueOf(item.integral) + ", " + String.valueOf(item.count) + ")";
				}
				try {
					db.beginTransaction();
					db.execSQL(query);
					db.setTransactionSuccessful();
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				} finally {
					db.endTransaction();
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				db.close();
				db = null;
			}
		}
		return false;
	}

	public static boolean setShopcarCount(String code, int count) {
		SQLiteDatabase db = getDb();
		if (db != null) {
			try {
				db.beginTransaction();
				String query = "update Shopcar set Count=" + count + " where ProductCode='" + code + "'";
				db.execSQL(query);
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
				db.close();
				db = null;
			}
			return true;
		}

		return false;
	}

	public static boolean removeShopcar(String code) {
		SQLiteDatabase db = getDb();
		if (db != null) {
			try {
				db.beginTransaction();
				String query = "delete from Shopcar where ProductCode='" + code + "'";
				db.execSQL(query);
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				db.endTransaction();
				db.close();
				db = null;
			}
			return true;
		}

		return false;
	}

	public static boolean clearShopcar() {
		SQLiteDatabase db = getDb();
		if (db != null) {
			try {
				db.beginTransaction();
				String query = "delete from Shopcar";
				db.execSQL(query);
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				db.endTransaction();
				db.close();
				db = null;
			}
			return true;
		}

		return false;
	}

	public static ReturnResult<Shopcar> getShopcars() {
		ReturnResult<Shopcar> rr = new ReturnResult<Shopcar>();

		SQLiteDatabase db = getDb();
		if (db != null) {
			try {
				String query = "select * from Shopcar";

				Cursor c = db.rawQuery(query, null);
				c.moveToFirst();
				while (!c.isAfterLast()) {
					Shopcar item = new Shopcar();
					item.count = c.getInt(c.getColumnIndex("Count"));
					item.img = c.getString(c.getColumnIndex("Img"));
					item.integral = c.getInt(c.getColumnIndex("Integral"));
					item.name = c.getString(c.getColumnIndex("Name"));
					item.productCode = c.getString(c.getColumnIndex("ProductCode"));

					rr.addData(item);

					c.moveToNext();
				}
				c.close();
				c = null;

				rr.status = "0";
			} catch (Exception e) {
				rr.status = "-1";
				rr.info = e.getMessage();
			} finally {
				db.close();
				db = null;
			}
		}
		return rr;
	}

	private static SQLiteDatabase getDb() {
		SQLiteDatabase db = null;
		if (db == null) {
			File dbFile = null;
			try {
				dbFile = SdCardProvider.getDbFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (dbFile != null) {
				db = SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
			}
		}
		return db;
	}

	private static SQLiteDatabase getTempDb() {
		SQLiteDatabase tmpDb = null;
		if (tmpDb == null) {
			File dbFile = null;
			try {
				dbFile = SdCardProvider.getTempDbFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (dbFile != null) {
				tmpDb = SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
			}
		}
		return tmpDb;
	}

	private static void copyDB(Context ctx) {
		try {
			File file = SdCardProvider.getDbFile();

			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();

			InputStream inputStream = ctx.getAssets().open("estar.db");
			OutputStream outputStream = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, length);
			}

			outputStream.flush();
			outputStream.close();
			inputStream.close();
		} catch (Exception e) {}
	}

	private static void copyTmpDB(Context ctx) {
		try {
			File file = SdCardProvider.getTempDbFile();
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();

			InputStream inputStream = ctx.getAssets().open("estar.db");
			OutputStream outputStream = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, length);
			}

			outputStream.flush();
			outputStream.close();
			inputStream.close();
		} catch (Exception e) {}
	}
}
