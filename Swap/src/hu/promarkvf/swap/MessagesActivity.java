package hu.promarkvf.swap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

public class MessagesActivity extends ExpandableListActivity implements OnClickListener {
	protected static final int SEND_EMAIL_ACTIVITY_ID = 1;
	// Create ArrayList to hold parent Items and Child Items
	private ArrayList<String>  parentItems            = new ArrayList<String>();
	private ArrayList<Object>  childItems             = new ArrayList<Object>();
	MessagesExpandableAdapter  adapter;
	Message                    message                = new Message();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		// Create Expandable List and set it's properties
		ExpandableListView expandableList = getExpandableListView();
		expandableList.setDividerHeight(2);
		expandableList.setGroupIndicator(null);
		expandableList.setClickable(true);
		expandableList.setBackgroundColor(getResources().getColor(R.color.color_default_backg));
		
		// Set the Items of Parent
		setGroupParents();
		// Set The Child Data
		setChildData();
		
		// Create the Adapter
		adapter = new MessagesExpandableAdapter(parentItems, childItems);
		
		adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
		
		// Set the Adapter to expandableList
		expandableList.setAdapter(adapter);
		expandableList.setOnChildClickListener(this);
	}
	
	// method to add parent Items
	public void setGroupParents() {
		parentItems.add(getString(R.string.messages_map_in));
		parentItems.add(getString(R.string.messages_map_out));
		parentItems.add(getString(R.string.messages_map_flow));
		parentItems.add(getString(R.string.messages_map_trash_in));
		parentItems.add(getString(R.string.messages_map_trash_out));
	}
	
	// method to set child data of each parent
	public void setChildData() {
		
		ArrayList<Message> child;
		// Add Child Items for messages to
		child = new ArrayList<Message>();
		child.addAll(SwapActivity.messages_to);
		childItems.add(child);
		
		// Add Child Items for messages from
		child = new ArrayList<Message>();
		child.addAll(SwapActivity.messages_from);
		childItems.add(child);
		
		// Add Child Items for  messages flow
		child = new ArrayList<Message>();
		child.addAll(SwapActivity.messages_flow);
		childItems.add(child);
		
		// Add Child Items for  trash
		child = new ArrayList<Message>();
		child.addAll(SwapActivity.messages_trash_to);
		childItems.add(child);
		
		// Add Child Items for  trash
		child = new ArrayList<Message>();
		child.addAll(SwapActivity.messages_trash_from);
		childItems.add(child);
	}
	
	@Override
	public void onClick(View v) {
		if (v instanceof ImageView) {
			Object o1 = (View) v.getParent();
			Object o2 = ((View) o1).getParent();
			Object o3 = ((View) o2).getParent();
			MessagesExpandableAdapter.ViewHolder h;
			h = (MessagesExpandableAdapter.ViewHolder) ((View) o3).getTag();
			@SuppressWarnings("unchecked")
			ArrayList<Message> arrayList = (ArrayList<Message>) childItems.get(h.group_pos);
			Message m = arrayList.get(h.child_pos);
			switch (v.getId()) {
				case R.id.mess_torles:
					if (h.group_pos < 2) { // áttenni a tööltek közzé
						@SuppressWarnings("unchecked")
						ArrayList<Message> arrayList_torolt = (ArrayList<Message>) childItems.get(h.group_pos + 3);
						if (h.group_pos == 0) { // beérkezett
							m.setStatus_to(Message.MESSAGE_DEL);
						} else { // elküldött
							m.setStatus_from(Message.MESSAGE_DEL);
						}
						arrayList_torolt.add(m);
					}
					arrayList.remove(h.child_pos);
					adapter.notifyDataSetChanged();
					JSONObject json;
					json = new JSONObject();
					try {
						json.put("UUID", SwapActivity.profil.getUUID());
						json.put("email", SwapActivity.profil.getEmail());
						json.put("message_id", m.getId());
						json.put("to_from", h.group_pos);
						json.put("mod_tip", Message.MESSAGE_DEL);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					new DataBase() {
						@Override
						protected void onPostExecute(String result) {
							if (result != null) {
								if (result.length() == 1) {
									Toast.makeText(MessagesActivity.this, getString(R.string.sikerestorles), Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(MessagesActivity.this, getString(R.string.hibastorles) + '\n' + result, Toast.LENGTH_SHORT).show();
								}
							}
						}
						
						@Override
						protected void onPreExecute() {
						}
						
					}.execute(Message.MESSAGE_STATUS_IR_ULR, "json=" + json.toString());
					break;
				
				case R.id.mess_tovabbitas:
					message.setProduct_id(m.getProduct_id());
					message.setUser_from(SwapActivity.dbProfil.getId());
					message.setUser_to(m.getUser_from());
					Intent intent = null;
					intent = new Intent(this, SendMailActivity.class);
					String to_name = m.getUser_from_name();
					String from_name = SwapActivity.dbProfil.getName();
					intent.putExtra("Body", "\n\n-----------------------------\n" + m.getMessage());
					String fejlec = getString(R.string.messages_valasz);
					fejlec = fejlec.replace("XXXYYY", to_name);
					intent.putExtra("Fejlec", fejlec);
					intent.putExtra("Parent", m.getId());
					startActivityForResult(intent, SEND_EMAIL_ACTIVITY_ID);
					break;
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case SEND_EMAIL_ACTIVITY_ID:
				if (resultCode == RESULT_OK) {
					String body = data.getStringExtra("Body");
					int parent = data.getIntExtra("Parent", 0);
					Date now = new Date();
					Calendar cal = Calendar.getInstance();
					cal.setTime(now);
					now = cal.getTime();
					message.setDate(SwapActivity.dateFormat.format(now));
					message.setMessage(body);
					message.setParent_id(parent);
					JSONObject json;
					json = new JSONObject();
					json = message.toJson();
					try {
						json.put("UUID", SwapActivity.dbProfil.getUUID());
						json.put("email", SwapActivity.dbProfil.getEmail());
					} catch (JSONException e) {
						e.printStackTrace();
					}
					new DataBase(MessagesActivity.this) {
						private ProgressDialog progressDialog = null;
						
						@Override
						protected void onPostExecute(String result) {
							this.progressDialog.dismiss();
							if (result != null) {
								if (result.length() == 1) {
									Toast.makeText(MessagesActivity.this, getString(R.string.messages_send_ok), Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(MessagesActivity.this, getString(R.string.messages_send_error) + '\n' + result, Toast.LENGTH_SHORT).show();
								}
							}
						}
						
						@Override
						protected void onPreExecute() {
							this.progressDialog = new ProgressDialog(MessagesActivity.this);
							this.progressDialog.setMessage(SwapActivity.maincontext.getText(R.string.assync_msg));
							this.progressDialog.show();
						}
						
					}.execute(Message.MESSAGE_IR_ULR, "json=" + json.toString());
					
				}
				break;
		}
		
	}
}
