package com.zklogic.androidutility.Cache;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class ZKCache {

	private String name = "ZKCache";

	public ZKCache() {
		// TODO Generated method by Arfan Mirza
		setCacheName(getClass().getName());
	}

	protected SharedPreferences getSharedPresfence(Context context, String name) {
		return context.getSharedPreferences(name, 0);
	}

	protected SharedPreferences getSharedPresfence(Context context) {
		return context.getSharedPreferences(getName(), 0);
	}

	public void setCacheName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract void init(Context context);

	public abstract void save(Context context);

	public void clean(Context context) {
		getSharedPresfence(context).edit().clear().commit();
	}

}
