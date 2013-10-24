package hu.promarkvf.swap;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

public class KezelArrayAdapter extends BaseAdapter {
	private ArrayList<Product> pList;
	private LayoutInflater     layoutInflater;
	
	public KezelArrayAdapter(Context context) {
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
			convertView = this.layoutInflater.inflate(R.layout.kezel_list_row_layout, null);
			holder = new ViewHolder();
			holder.image = (ImageButton) convertView.findViewById(R.id.kezel_button);
			holder.textView_detail = (TextView) convertView.findViewById(R.id.kezel_details);
			holder.button_keszlet = (Button) convertView.findViewById(R.id.kezel_keszlet);
			holder.checkBox_keres = (CheckBox) convertView.findViewById(R.id.kezel_keres);
			holder.checkBox_kinal = (CheckBox) convertView.findViewById(R.id.kezel_kinal);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.textView_detail.setText(this.pList.get(position).getDescription());
		holder.button_keszlet.setText(SwapActivity.maincontext.getText(R.string.keszlet) + this.pList.get(position).getKeszletStr());
		holder.button_keszlet.setTag(position);
		if (this.pList.get(position).getKeszlet() > 0) {
			holder.button_keszlet.setVisibility(View.VISIBLE);
		} else {
			holder.button_keszlet.setVisibility(View.INVISIBLE);
		}
		holder.image.setImageBitmap(this.pList.get(position).getImage_s());
		holder.image.setTag(position);
		holder.checkBox_keres.setTag(position);
		holder.checkBox_keres.setChecked(this.pList.get(position).isSearch());
		holder.checkBox_kinal.setTag(position);
		holder.checkBox_kinal.setChecked(this.pList.get(position).isOffer());
		
		return convertView;
	}
	
	class ViewHolder {
		ImageButton image;
		TextView    textView_detail;
		Button      button_keszlet;
		CheckBox    checkBox_keres;
		CheckBox    checkBox_kinal;
	}
	
	@Override
	public Product getItem(int position) {
		return this.pList.get(position);
	}
	
}
