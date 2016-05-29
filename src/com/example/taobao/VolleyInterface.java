package com.example.taobao;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import android.content.Context;
import android.widget.Toast;

public abstract class VolleyInterface {
	public Context mContext;
	public static Listener<String> mListener;
	public static ErrorListener mErrorListener;

	public VolleyInterface(Context context, Listener<String> listener,
			ErrorListener errorListener) {
		this.mContext = context;
		this.mErrorListener = errorListener;
		this.mListener = listener;
	}

	public abstract void onMySuccess(String result);
	public abstract void onMyError(VolleyError error);
	
	public Listener<String> loadingListener() {
		mListener = new Listener<String>() {
			@Override
			public void onResponse(String arg0) {
				Toast.makeText(mContext, "加载中", 3000).show();
				onMySuccess(arg0);

			}
		};
		return mListener;
	}

	public ErrorListener errorListener() {
		mErrorListener=new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				onMyError(arg0);
				Toast.makeText(mContext, "加载出错", 3000).show();
			}
		};
		return mErrorListener;
		
	}
}
