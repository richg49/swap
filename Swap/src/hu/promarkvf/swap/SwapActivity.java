package hu.promarkvf.swap;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SwapActivity extends Activity {
	public final static String WEB_SERVICE_ULR = "http://swap.promarkvf.hu/swap/web_service/";
	private static final int   RESULT_SETTINGS = 1;
	static Context             maincontext;
	static SetAppId            appID;
	static Profil              profil;
	static Profil              dbProfil;
	static Button              btnKeres       = null;
	static TextView            tvProfil        = null;
	TimerTask                  idoTask_fo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_swap);
		maincontext = SwapActivity.this;
		profil = new Profil();
		dbProfil = new Profil();
		btnKeres = (Button) this.findViewById(R.id.btn_keres );
		OnClickListener profilClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		};
		btnKeres.setOnClickListener(profilClick);
		
		tvProfil = (TextView) this.findViewById(R.id.tVProfil);
		
		appID = new SetAppId();
		this.loadPref();
		System.out.println(appID.toString());
		
		this.idoTask_fo = this.idofut_socket();
		// status sor időzítése
		/*
		 * ScheduledExecutorService scheduler =
		 * Executors.newSingleThreadScheduledExecutor();
		 * 
		 * scheduler.scheduleAtFixedRate(new Runnable() { public void run() {
		 * int m = 0; Toast.makeText(maincontext, String.valueOf(m++),
		 * Toast.LENGTH_LONG).show(); } }, 10, 10, TimeUnit.SECONDS);
		 */
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.swap, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			
			case R.id.action_settings:
				Intent i = new Intent(this, ProfilSetActivity.class);
				this.startActivityForResult(i, RESULT_SETTINGS);
				break;
				
		}
		
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
			case RESULT_SETTINGS:
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
		dbProfil.setUUID(SwapActivity.profil.getUUID());
		dbProfil.setEmail(SwapActivity.profil.getEmail());
		dbProfil.ReadDb();
		System.out.println(dbProfil.getEmail());
	}
	
	/*
	 * Internet ellenőrzése
	 */
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
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
						}
						else {
							SwapActivity.tvProfil.setText(SwapActivity.this.getText(R.string.tvProfilStatusOff));
						}
					};// --
				});// --
			};
		});
		t.scheduleAtFixedRate(idoTask2, 0, 20 * 1000);
		return idoTask2;
	}
	
}
