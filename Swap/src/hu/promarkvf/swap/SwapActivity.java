package hu.promarkvf.swap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SwapActivity extends Activity {
	public final static String WEB_SERVICE_ULR              = "http://swap.promarkvf.hu/swap/web_service/";
	public final static String WEB_HOST                     = "http://swap.promarkvf.hu";
	final static String        START_OLV_ULR                = SwapActivity.WEB_SERVICE_ULR + "start_olv.php";
	final static String        MESSAGE_OLV_ULR              = SwapActivity.WEB_SERVICE_ULR + "message_olv.php";
	private static final int   RESULT_SETTINGS              = 1;
	protected static final int SETTING_ACTIVITY_ID          = 1;
	protected static final int PRODUCTGROUP_BUY_ACTIVITY_ID = 2;
	protected static final int KEZEL_ACTIVITY_ID            = 3;
	protected static final int UZENET_ACTIVITY_ID           = 4;
	private static final int   NOTIFICATION_ID              = 1;
	static SimpleDateFormat    dateFormat                   = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat    dateFormat_smal              = new SimpleDateFormat("yyyy-MM-dd");
	static Context             maincontext;
	static SetAppId            appID;
	static Profil              profil;
	static Profil              dbProfil;
	static Button              btnKezel                     = null;
	static Button              btnUzenet                    = null;
	static Button              btnPartner                   = null;
	static Button              btnProfil                    = null;
	static Button              btnBuy                       = null;
	static Button              btnKivansag                  = null;
	static TextView            tvProfil                     = null;
	TimerTask                  idoTask_fo;
	static ProductGroups       pgs;
	Spinner                    spinner_gyujt;
	ArrayAdapter<String>       aa_gyujt                     = null;
	static int                 gyujtemenyIndex              = 0;
	static ArrayList<Message>  messages_to                  = new ArrayList<Message>();
	static ArrayList<Message>  messages_from                = new ArrayList<Message>();
	static ArrayList<Message>  messages_trash_to            = new ArrayList<Message>();
	static ArrayList<Message>  messages_trash_from          = new ArrayList<Message>();
	static ArrayList<Message>  messages_flow                = new ArrayList<Message>();
	Date                       last_message_read;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_swap);
		maincontext = SwapActivity.this;
		profil = new Profil();
		dbProfil = new Profil();
		
		//		btnKeres = (Button) this.findViewById(R.id.btn_keres);
		//		OnClickListener profilClick = new OnClickListener() {
		//			@Override
		//			public void onClick(View v) {
		//				Intent i = null;
		//				i = new Intent(SwapActivity.maincontext, KeresActivity.class);
		//				startActivityForResult(i, KERES_ACTIVITY_ID);
		//			}
		//		};
		//		btnKeres.setOnClickListener(profilClick);
		
		btnKezel = (Button) this.findViewById(R.id.swap_kezel);
		OnClickListener btn_kinalClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dbProfil.isActivated()) {
					Intent i = null;
					i = new Intent(SwapActivity.maincontext, KezelActivity.class);
					startActivityForResult(i, KEZEL_ACTIVITY_ID);
				} else {
					Toast.makeText(SwapActivity.this, getString(R.string.profilinaktiv_msg), Toast.LENGTH_SHORT).show();
				}
			}
		};
		btnKezel.setOnClickListener(btn_kinalClick);
		
		btnUzenet = (Button) this.findViewById(R.id.swap_uzenetek);
		OnClickListener btn_UzenetClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dbProfil.isActivated()) {
					Intent i = null;
					i = new Intent(SwapActivity.maincontext, MessagesActivity.class);
					startActivityForResult(i, UZENET_ACTIVITY_ID);
				} else {
					Toast.makeText(SwapActivity.this, getString(R.string.profilinaktiv_msg), Toast.LENGTH_SHORT).show();
				}
			}
		};
		btnUzenet.setOnClickListener(btn_UzenetClick);
		
		btnProfil = (Button) this.findViewById(R.id.swap_profil);
		OnClickListener btn_ProfilClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = null;
				i = new Intent(SwapActivity.maincontext, ProfilSetActivity.class);
				startActivityForResult(i, SETTING_ACTIVITY_ID);
			}
		};
		btnProfil.setOnClickListener(btn_ProfilClick);
		
		btnBuy = (Button) this.findViewById(R.id.swap_gyujtemeny);
		OnClickListener btn_BuyClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dbProfil.isActivated()) {
					Intent i = null;
					i = new Intent(SwapActivity.maincontext, ProductBuyActivity.class);
					startActivityForResult(i, PRODUCTGROUP_BUY_ACTIVITY_ID);
				} else {
					Toast.makeText(SwapActivity.this, getString(R.string.profilinaktiv_msg), Toast.LENGTH_SHORT).show();
				}
			}
		};
		btnBuy.setOnClickListener(btn_BuyClick);
		
		tvProfil = (TextView) this.findViewById(R.id.tVProfil);
		this.spinner_gyujt = (Spinner) this.findViewById(R.id.spinner_gyujt);
		
		pgs = new ProductGroups();
		appID = new SetAppId();
		this.loadPref();
		
		if (isOnline()) {
			if (profil.getUUID() != "" || profil.getEmail() != "") {
				new DataBase(SwapActivity.maincontext) {
					private ProgressDialog progressDialog = null;
					
					@Override
					protected void onPostExecute(String result) {
						this.progressDialog.dismiss();
						if (result != null) {
							JSONObject json;
							try {
								json = new JSONObject(result);
								if (!json.isNull("profil")) {
									JSONObject jsonprofil = json.getJSONObject("profil");
									dbProfil.FromJson(jsonprofil.toString());
									SwapActivity.tvProfil.setText(dbProfil.isActivatedString());
								} else {
									SwapActivity.tvProfil.setText(getText(R.string.tvProfilStatusEmpty));
								}
								if (!json.isNull("productgroups")) {
									JSONObject jsonpg = json.getJSONObject("productgroups");
									pgs.FromJson(jsonpg.toString());
									if (aa_gyujt == null && pgs.descriptions != null) {
										aa_gyujt = new ArrayAdapter<String>(SwapActivity.maincontext, android.R.layout.simple_spinner_item, pgs.descriptions);
										spinner_gyujt.setAdapter(aa_gyujt);
										// SwapActivity.this.spinner_gyujt.invalidate();
									}
								}
								//							System.out.println("ss");
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
					
					@Override
					protected void onPreExecute() {
						this.progressDialog = new ProgressDialog(SwapActivity.maincontext);
						this.progressDialog.setMessage(SwapActivity.maincontext.getText(R.string.assync_msg));
						this.progressDialog.show();
					}
					
				}.execute(START_OLV_ULR, "uuid=" + profil.getUUID(), "email=" + profil.getEmail());
			} else {
				Toast.makeText(SwapActivity.this, getString(R.string.tvProfilStatusEmpty), Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(SwapActivity.this, getString(R.string.internet_hiba), Toast.LENGTH_SHORT).show();
		}
		
		// dbProfil.ReadDb();
		// System.out.println(dbProfil.getEmail());
		// pgs.ReadDb(maincontext);
		readAllMessages();
		idoTask_fo = this.idofut_socket();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.swap, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i = null;
		switch (item.getItemId()) {
		
			case R.id.action_settings:
				i = new Intent(this, ProfilSetActivity.class);
				this.startActivityForResult(i, SETTING_ACTIVITY_ID);
				break;
			
			case R.id.action_buy:
				i = new Intent(this, ProductBuyActivity.class);
				this.startActivityForResult(i, PRODUCTGROUP_BUY_ACTIVITY_ID);
				break;
		}
		
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
			case SETTING_ACTIVITY_ID:
				break;
			case PRODUCTGROUP_BUY_ACTIVITY_ID:
				//				aa_gyujt = new ArrayAdapter<String>(SwapActivity.maincontext, android.R.layout.simple_spinner_item, pgs.descriptions);
				//				spinner_gyujt.setAdapter(aa_gyujt);
				spinner_gyujt.invalidate();
				break;
			case UZENET_ACTIVITY_ID:
				showNotification();
				break;
			case KEZEL_ACTIVITY_ID:
				readAllMessages();
				break;
		
		}
		
	}
	
	/*
	 * Beállítások betöltése
	 */
	private void loadPref() {
		SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		SwapActivity.profil.setEmail(mySharedPreferences.getString("prefemail", ""));
		SwapActivity.profil.setName(mySharedPreferences.getString("prefname", ""));
		SwapActivity.profil.setRname(mySharedPreferences.getString("prefrname", ""));
		SwapActivity.profil.setAddress_postcode(mySharedPreferences.getString("prefaddress_postcode", ""));
		SwapActivity.profil.setAddress_city(mySharedPreferences.getString("prefaddress_city", ""));
		SwapActivity.profil.setAddress_street(mySharedPreferences.getString("prefaddress_streat", ""));
		SwapActivity.profil.setGps_long(mySharedPreferences.getString("preflong", "0.0"));
		SwapActivity.profil.setGps_lat(mySharedPreferences.getString("preflat", "0.0"));
	}
	
	/*
	 * Internet ellenőrzése
	 */
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}
	
	private TimerTask idofut_socket() {
		Timer t = new Timer();
		// Set the schedule function and rate
		TimerTask idoTask2 = (new TimerTask() {
			@Override
			public void run() {
				SwapActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (dbProfil.isActivated()) {
							SwapActivity.tvProfil.setText(SwapActivity.this.getText(R.string.tvProfilStatusOn));
						} else {
							SwapActivity.tvProfil.setText(SwapActivity.this.getText(R.string.tvProfilStatusOff));
						}
						if (SwapActivity.this.aa_gyujt == null && pgs.descriptions != null) {
							SwapActivity.this.spinner_gyujt.invalidate();
						}
						readAllMessages();
					};// --
				});// --
			};
		});
		t.scheduleAtFixedRate(idoTask2, 0, 20 * 1000);
		return idoTask2;
	}
	
	@SuppressWarnings("deprecation")
	public void showNotification() {
		Date mess_max_date = null;
		for (Message m : messages_to) {
			int comp = mess_max_date != null ? m.getDateDate().compareTo(mess_max_date) : 1;
			if (comp > 0 && m.isNew_message_to()) {
				mess_max_date = m.getDateDate();
			}
		}
		if (mess_max_date != null) {
			int comp = last_message_read != null ? last_message_read.compareTo(mess_max_date) : -1;
			if (comp < 0) {
				last_message_read = mess_max_date;
				NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				
				Intent notificationIntent = new Intent(this, MessagesActivity.class);
				PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
				
				Notification notification = new Notification(R.drawable.icon, getString(R.string.messages_notification_read), System.currentTimeMillis());
				notification.defaults |= Notification.DEFAULT_SOUND;
				notification.defaults |= Notification.DEFAULT_VIBRATE;
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				notification.setLatestEventInfo(this, getString(R.string.messages_notification_read), "", contentIntent);
				
				mNotificationManager.notify(NOTIFICATION_ID, notification);
				
				//		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.icon).setContentTitle("My notification").setContentText("Hello World!");
				//		// Creates an explicit intent for an Activity in your app
				//		Intent resultIntent = new Intent(this, SwapActivity.class);
				//		
				//		// The stack builder object will contain an artificial back stack for the
				//		// started Activity.
				//		// This ensures that navigating backward from the Activity leads out of
				//		// your application to the Home screen.
				//		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
				//		// Adds the back stack for the Intent (but not the Intent itself)
				//		stackBuilder.addParentStack(SwapActivity.class);
				//		// Adds the Intent that starts the Activity to the top of the stack
				//		stackBuilder.addNextIntent(resultIntent);
				//		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
				//		mBuilder.setContentIntent(resultPendingIntent);
				//		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				//		// mId allows you to update the notification later on.
				//		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
			}
		}
	}
	
	public void readAllMessages() {
		messages_to.clear();
		messages_from.clear();
		messages_flow.clear();
		messages_trash_to.clear();
		messages_trash_from.clear();
		JSONObject json;
		json = new JSONObject();
		try {
			json.put("UUID", SwapActivity.profil.getUUID());
			json.put("email", SwapActivity.profil.getEmail());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		new DataBase(SwapActivity.this) {
			private ProgressDialog progressDialog = null;
			
			@Override
			protected void onPostExecute(String result) {
				this.progressDialog.dismiss();
				if (result != null) {
					JSONObject json;
					try {
						json = new JSONObject(result);
						// -- üress-e a root
						if (!json.isNull("to")) {
							JSONArray json_to = json.getJSONArray("to");
							int db = json_to.length();
							// --- JSON feldolgozás
							for (int i = 0; i < db; i++) {
								JSONObject json_t = (json_to.getJSONObject(i)).getJSONObject("to");
								Message message = new Message(json_t);
								messages_to.add(message);
							}
						}
						if (!json.isNull("from")) {
							JSONArray json_from = json.getJSONArray("from");
							int db = json_from.length();
							// --- JSON feldolgozás
							for (int i = 0; i < db; i++) {
								JSONObject json_f = (json_from.getJSONObject(i)).getJSONObject("from");
								Message message = new Message(json_f);
								messages_from.add(message);
							}
						}
						if (!json.isNull("trash_to")) {
							JSONArray json_from = json.getJSONArray("trash_to");
							int db = json_from.length();
							// --- JSON feldolgozás
							for (int i = 0; i < db; i++) {
								JSONObject json_tr = (json_from.getJSONObject(i)).getJSONObject("trash_to");
								Message message = new Message(json_tr);
								messages_trash_to.add(message);
							}
						}
						if (!json.isNull("trash_from")) {
							JSONArray json_from = json.getJSONArray("trash_from");
							int db = json_from.length();
							// --- JSON feldolgozás
							for (int i = 0; i < db; i++) {
								JSONObject json_tr = (json_from.getJSONObject(i)).getJSONObject("trash_from");
								Message message = new Message(json_tr);
								messages_trash_from.add(message);
							}
						}
						showNotification();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			
			@Override
			protected void onPreExecute() {
				this.progressDialog = new ProgressDialog(SwapActivity.this);
				//				this.progressDialog.setMessage(SwapActivity.maincontext.getText(R.string.assync_msg));
				//				this.progressDialog.show();
			}
			
		}.execute(MESSAGE_OLV_ULR, "json=" + json.toString());
		
	}
}
