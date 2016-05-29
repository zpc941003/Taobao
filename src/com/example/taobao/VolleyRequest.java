package com.example.taobao;

import java.util.Map;

import android.content.Context;

import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;

public class VolleyRequest {

	public static StringRequest stringRequest;
	public static Context context;

	public static void RequestGet(Context mContext, String url, String tag,
			VolleyInterface vif) {
		// TODO Auto-generated method stub
		MyApplication.getHttpQueues().cancelAll(tag);
		stringRequest = new StringRequest(Method.GET, url,
				vif.loadingListener(), vif.errorListener());
		stringRequest.setTag(tag);
		MyApplication.getHttpQueues().add(stringRequest);
		MyApplication.getHttpQueues().start();
	}

	public static void RequestPost(Context mcontext, String url, String tag,
			final Map<String, String> params, VolleyInterface vif) {
		MyApplication.getHttpQueues().cancelAll(tag);
		stringRequest = new StringRequest(url, vif.loadingListener(),
				vif.errorListener()) {
			protected java.util.Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				return params;
			};
		};
		stringRequest.setTag(tag);
		MyApplication.getHttpQueues().add(stringRequest);
		MyApplication.getHttpQueues().start();
	}
}
