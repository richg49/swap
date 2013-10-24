package hu.promarkvf.swap;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class KinalatArrayAdapter extends BaseAdapter {
	private ArrayList<ProductKinalat> list;
	private LayoutInflater            layoutInflater;
	
	public KinalatArrayAdapter(Context context, ArrayList<ProductKinalat> kinalatList) {
		this.list = kinalatList;
		this.layoutInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return this.list.size();
	}
	
	@Override
	public Object getItem(int position) {
		return this.list.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	class ViewHolder {
		TextView    textViewn;
		ImageButton bottom_email;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = this.layoutInflater.inflate(R.layout.kinalat_list_row_layout, null);
			holder = new ViewHolder();
			holder.textViewn = (TextView) convertView.findViewById(R.id.kinalat_nev);
			holder.bottom_email = (ImageButton) convertView.findViewById(R.id.kinalat_imageButton);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.textViewn.setText(this.list.get(position).getName());
		holder.bottom_email.setTag(position);
		
		return convertView;
	}
	
}
