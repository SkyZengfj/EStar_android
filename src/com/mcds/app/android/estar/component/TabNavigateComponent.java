package com.mcds.app.android.estar.component;

import com.mcds.app.android.estar.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class TabNavigateComponent extends LinearLayout {

	public TabNavigateComponent(Context context) {
		super(context);
	}

	public TabNavigateComponent(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.component_tab_navigate, this);
	}

}
