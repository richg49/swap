package hu.promarkvf.swap;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class PgArrayAdapter extends BaseAdapter {
	private ArrayList<ProductGroup> pgList;
	private LayoutInflater          layoutInflater;
	
	public PgArrayAdapter(Context context, ArrayList<ProductGroup> pgList) {
		this.pgList = pgList;
		this.layoutInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return this.pgList.size();
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = this.layoutInflater.inflate(R.layout.pb_list_row_layout, null);
			holder = new ViewHolder();
			holder.textViewn = (TextView) convertView.findViewById(R.id.pb_nev);
			holder.textViewar = (TextView) convertView.findViewById(R.id.pb_ar);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.pb_valaszt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.textViewn.setText(this.pgList.get(position).getDescription());
		holder.textViewar.setText(String.valueOf(this.pgList.get(position).getPrice()) + " " + this.pgList.get(position).getCurrency());
		Date now = new Date();
		Date exp = new Date();
		try {
			exp = SwapActivity.dateFormat_smal.parse(this.pgList.get(position).getExpire());
		} catch (ParseException e) {
			exp = now;
		}
		if (this.pgList.get(position).getExpire() != "" && now.compareTo(exp) < 0) {
			holder.checkBox.setChecked(true);
			holder.checkBox.setEnabled(false);
			holder.checkBox.setText(SwapActivity.maincontext.getText(R.string.elofizetve));
			holder.textViewar.setText(SwapActivity.maincontext.getText(R.string.lejar) + String.valueOf(this.pgList.get(position).getExpire()));
		} else {
			holder.textViewar.setText(String.valueOf(this.pgList.get(position).getPrice()) + " " + this.pgList.get(position).getCurrency());
			holder.checkBox.setChecked(this.pgList.get(position).isWant());
		}
		holder.checkBox.setTag(position);
		
		return convertView;
	}
	
	class ViewHolder {
		TextView textViewn;
		TextView textViewar;
		CheckBox checkBox;
	}
	
	@Override
	public Object getItem(int position) {
		return this.pgList.get(position);
	}
	
}
