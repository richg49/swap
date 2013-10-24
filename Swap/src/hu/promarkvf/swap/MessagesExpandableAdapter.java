package hu.promarkvf.swap;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

public class MessagesExpandableAdapter extends BaseExpandableListAdapter {
	private Activity           activity;
	private ArrayList<Object>  childtems;
	private LayoutInflater     inflater;
	private ArrayList<String>  parentItems;
	private ArrayList<Message> child;
	
	// constructor
	public MessagesExpandableAdapter(ArrayList<String> parents, ArrayList<Object> childern) {
		this.parentItems = parents;
		this.childtems = childern;
	}
	
	public void setInflater(LayoutInflater inflater, Activity activity) {
		this.inflater = inflater;
		this.activity = activity;
	}
	
	public class ViewHolder {
		TextView  detail;
		TextView  date;
		TextView  from_to;
		ImageView torol;
		ImageView tovabbit;
		int       group_pos;
		int       child_pos;
	}
	
	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		child = ((ArrayList<Message>) childtems.get(groupPosition));
		
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.activity_messages_child, null);
			holder.detail = (TextView) convertView.findViewById(R.id.mess_ch_detail);
			holder.from_to = (TextView) convertView.findViewById(R.id.mess_ch_partner);
			holder.date = (TextView) convertView.findViewById(R.id.mess_ch_date);
			holder.torol = (ImageView) convertView.findViewById(R.id.mess_torles);
			holder.tovabbit = (ImageView) convertView.findViewById(R.id.mess_tovabbitas);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.from_to.setText(groupPosition == 0 ? child.get(childPosition).user_from_name : child.get(childPosition).user_to_name);
		holder.date.setText(child.get(childPosition).getDate());
		holder.detail.setText(child.get(childPosition).getMessage());
		holder.torol.setTag(childPosition);
		holder.tovabbit.setTag(childPosition);
		holder.group_pos = groupPosition;
		holder.child_pos = childPosition;
		
		// set the ClickListener to handle the click event on child item
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				MessagesExpandableAdapter.ViewHolder h;
				h = (MessagesExpandableAdapter.ViewHolder) ((View) view).getTag();
				if (h.group_pos == 0 && child.get(h.child_pos).isNew_message_to()) {
					view.setBackgroundColor(SwapActivity.maincontext.getResources().getColor(R.color.msg_no_new_backg));
					child.get(h.child_pos).setStatus_to(Message.MESSAGE_READ);
					JSONObject json;
					json = new JSONObject();
					try {
						json.put("UUID", SwapActivity.profil.getUUID());
						json.put("email", SwapActivity.profil.getEmail());
						json.put("message_id", child.get(h.child_pos).getId());
						json.put("to_from", h.group_pos);
						json.put("mod_tip", Message.MESSAGE_READ);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					new DataBase() {
						@Override
						protected void onPostExecute(String result) {
							//							System.out.println("EE");
						}
						
						@Override
						protected void onPreExecute() {
						}
						
					}.execute(Message.MESSAGE_STATUS_IR_ULR, "json=" + json.toString());
				}
			}
		});
		
		if (groupPosition == 0) {
			holder.tovabbit.setVisibility(View.VISIBLE);
			if (child.get(childPosition).isNew_message_to()) {
				convertView.setBackgroundColor(SwapActivity.maincontext.getResources().getColor(R.color.msg_new_backg));
			}
			if (child.get(childPosition).isRead_message_to()) {
				convertView.setBackgroundColor(SwapActivity.maincontext.getResources().getColor(R.color.msg_no_new_backg));
			}
		} else {
			convertView.setBackgroundColor(SwapActivity.maincontext.getResources().getColor(R.color.msg_no_new_backg));
			holder.tovabbit.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}
	
	// method getGroupView is called automatically for each parent item
	// Implement this method as per your requirement
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_messages, null);
		}
		
		((CheckedTextView) convertView).setText(parentItems.get(groupPosition));
		((CheckedTextView) convertView).setChecked(isExpanded);
		
		return convertView;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		return ((ArrayList<Object>) childtems.get(groupPosition)).size();
	}
	
	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}
	
	@Override
	public int getGroupCount() {
		return parentItems.size();
	}
	
	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}
	
	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}
	
	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}
	
	@Override
	public boolean hasStableIds() {
		return false;
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	
}
