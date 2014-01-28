package com.mcds.app.android.estar.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BaseListAdapter<T> extends BaseAdapter {

	protected List<T> _items = null;

	public BaseListAdapter() {
		this._items = new ArrayList<T>();
	}

	public void clearItems() {
		this._items.clear();
	}

	public void addItem(T item) {
		if (item == null) {
			return;
		}

		this._items.add(item);
	}

	public void addItems(T[] items) {
		if (items == null) {
			return;
		}

		int count = items.length;
		for (int i = 0; i < count; i++) {
			this.addItem(items[i]);
		}
	}

	public void addItems(List<T> items) {
		if (items == null) {
			return;
		}
		int count = items.size();
		for (int i = 0; i < count; i++) {
			this.addItem(items.get(i));
		}
	}

	public void setItems(T[] items) {
		this.clearItems();
		addItems(items);
	}

	public void setItems(List<T> items) {
		this.clearItems();
		addItems(items);
	}

	@Override
	public int getCount() {
		return this._items.size();
	}

	@Override
	public T getItem(int position) {
		return this._items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;

	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);

}
