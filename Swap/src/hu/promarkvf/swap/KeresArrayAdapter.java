package hu.promarkvf.swap;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

public class KeresArrayAdapter extends BaseAdapter {
	private ArrayList<Product> pList;
	private LayoutInflater     layoutInflater;
	
	public KeresArrayAdapter(Context context) {
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		pList = SwapActivity.pgs.GetProducGroup(SwapActivity.gyujtemenyIndex).getProductList();
	}
	
	@Override
	public int getCount() {
		return this.pList.size();
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = this.layoutInflater.inflate(R.layout.keres_list_row_layout, null);
			holder = new ViewHolder();
			holder.image = (hu.promarkvf.swap.TouchHighlightImageButton) convertView.findViewById(R.id.keres_button);
			holder.textView = (TextView) convertView.findViewById(R.id.keres_details);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.keres_valaszt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.textView.setText(this.pList.get(position).getDescription());
		holder.image.setImageBitmap(this.pList.get(position).getImage_s());
		holder.image.setTag(position);
		holder.checkBox.setTag(position);
		holder.checkBox.setChecked(this.pList.get(position).isSearch());
		
		return convertView;
	}
	
	class ViewHolder {
		ImageButton image;
		TextView    textView;
		CheckBox    checkBox;
	}
	
	@Override
	public Product getItem(int position) {
		return this.pList.get(position);
	}
	
}
