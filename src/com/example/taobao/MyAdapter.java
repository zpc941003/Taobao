package com.example.taobao;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import bean.ItemBean;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

public class MyAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<ItemBean> mList;
	private RequestQueue queue;
	private ImageLoader imageLoader;

	public MyAdapter(Context context, List<ItemBean> mList) {
		mInflater = LayoutInflater.from(context);
		this.mList = mList;
		queue = Volley.newRequestQueue(context);
		imageLoader = new ImageLoader(queue, new BitmapCache());
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater
					.inflate(R.layout.goods_items, parent, false);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			holder.sold = (TextView) convertView.findViewById(R.id.sold);
			holder.img = (NetworkImageView) convertView
					.findViewById(R.id.imageView1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ItemBean bean = mList.get(position);
		holder.title.setText(bean.getTitle());
		holder.price.setText(bean.getPrice());
		holder.sold.setText(bean.getSold());
		holder.img.setImageUrl(bean.getImgurl(), imageLoader);
//		String imgUrl=bean.getImgurl();
//		if (imgUrl != null && !imgUrl.equals("")) {
//			holder.img.setDefaultImageResId(R.drawable.ic_launcher);
//			holder.img.setErrorImageResId(R.drawable.ic_launcher);
//			holder.img.setImageUrl(imgUrl, imageLoader);
//		}
		return convertView;
	}

	class ViewHolder {
		public NetworkImageView img;
		public TextView title;
		public TextView price;
		public TextView sold;
	}
}
