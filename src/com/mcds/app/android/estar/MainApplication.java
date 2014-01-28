package com.mcds.app.android.estar;

import com.mcds.app.android.estar.provider.PersistenceProvider;

import android.app.Application;

public class MainApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		PersistenceProvider.init(this);
	}
}
