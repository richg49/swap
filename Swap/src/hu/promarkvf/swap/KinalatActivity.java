package hu.promarkvf.swap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class KinalatActivity extends Activity implements OnClickListener {
	protected static final int    SEND_EMAIL_ACTIVITY_ID = 1;
	protected static final String PRODUCT_KINALAT_ULR    = SwapActivity.WEB_SERVICE_ULR + "product_kinalat.php";
	ArrayList<ProductKinalat>     kinalatList            = new ArrayList<ProductKinalat>();
	int                           ret                    = 0;
	ListView                      lv1;
	Message                       message                = new Message();
	String                        productName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kinalat);
		
		Intent intent = getIntent();
		
		String productId = intent.getStringExtra("productId");
		productName = intent.getStringExtra("productName");
		
		TextView header = (TextView) findViewById(R.id.kinalat_header);
		header.setText(productName);
		
		JSONObject json;
		json = new JSONObject();
		try {
			json.put("product_id", productId);
			json.put("UUID", SwapActivity.dbProfil.getUUID());
			json.put("email", SwapActivity.dbProfil.getEmail());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		new DataBase(KinalatActivity.this) {
			private ProgressDialog progressDialog = null;
			
			@Override
			protected void onPostExecute(String result) {
				this.progressDialog.dismiss();
				if (result != null) {
					JSONObject json;
					try {
						json = new JSONObject(result);
						// -- üress-e a root
						if (!json.isNull("profils")) {
							JSONArray jsonprofils = json.getJSONArray("profils");
							int db = jsonprofils.length();
							// --- JSON feldolgozás
							for (int i = 0; i < db; i++) {
								JSONObject jsonprofil = jsonprofils.getJSONObject(i);
								ProductKinalat productKinalat = new ProductKinalat(jsonprofil);
								if (productKinalat.getProfil_id() != SwapActivity.dbProfil.getId()) {
									kinalatList.add(productKinalat);
								}
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if (kinalatList.size() > 0) {
						lv1 = (ListView) findViewById(R.id.kinalat_list);
						lv1.setAdapter(new KinalatArrayAdapter(KinalatActivity.this, kinalatList));
						lv1.setClickable(true);
						OnItemClickListener mailClick = new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
								System.out.print("w");
							}
						};
						lv1.setOnItemClickListener(mailClick);
						KinalatActivity.this.lv1.invalidateViews();
					}
				}
			}
			
			@Override
			protected void onPreExecute() {
				this.progressDialog = new ProgressDialog(KinalatActivity.this);
				this.progressDialog.setMessage(SwapActivity.maincontext.getText(R.string.assync_msg));
				this.progressDialog.show();
			}
			
		}.execute(PRODUCT_KINALAT_ULR, "json=" + json.toString());
		
	}
	
	@Override
	public void onClick(View v) {
		message.setProduct_id(kinalatList.get((Integer) v.getTag()).getProduct_id());
		message.setUser_from(SwapActivity.dbProfil.getId());
		message.setUser_to(kinalatList.get((Integer) v.getTag()).getProfil_id());
		Intent intent = null;
		intent = new Intent(this, SendMailActivity.class);
		String to_name = kinalatList.get((Integer) v.getTag()).getName();
		String from_name = SwapActivity.dbProfil.getName();
		String body = getString(R.string.uzenet_body);
		body = body.replace("XXXYYY", to_name);
		body = body.replace("ZZZZZ", from_name);
		body = body.replace("PPPPP", productName);
		intent.putExtra("Body", body);
		intent.putExtra("Fejlec", (String) kinalatList.get((Integer) v.getTag()).getName());
		startActivityForResult(intent, SEND_EMAIL_ACTIVITY_ID);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case SEND_EMAIL_ACTIVITY_ID:
				if (resultCode == RESULT_OK) {
					String body = data.getStringExtra("Body");
					Date now = new Date();
					Calendar cal = Calendar.getInstance();
					cal.setTime(now);
					now = cal.getTime();
					message.setDate(SwapActivity.dateFormat.format(now));
					message.setMessage(body);
					JSONObject json;
					json = new JSONObject();
					json = message.toJson();
					try {
						json.put("UUID", SwapActivity.dbProfil.getUUID());
						json.put("email", SwapActivity.dbProfil.getEmail());
					} catch (JSONException e) {
						e.printStackTrace();
					}
					new DataBase(KinalatActivity.this) {
						private ProgressDialog progressDialog = null;
						
						@Override
						protected void onPostExecute(String result) {
							this.progressDialog.dismiss();
							if (result != null) {
								if (result.length() == 1) {
									Toast.makeText(KinalatActivity.this, getString(R.string.messages_send_ok), Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(KinalatActivity.this, getString(R.string.messages_send_error) + '\n' + result, Toast.LENGTH_SHORT).show();
								}
							}
						}
						
						@Override
						protected void onPreExecute() {
							this.progressDialog = new ProgressDialog(KinalatActivity.this);
							this.progressDialog.setMessage(SwapActivity.maincontext.getText(R.string.assync_msg));
							this.progressDialog.show();
						}
						
					}.execute(Message.MESSAGE_IR_ULR, "json=" + json.toString());
					
				}
				break;
		}
		
	}
}
