package hu.promarkvf.swap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SwapActivity extends Activity {
	public final static String WEB_SERVICE_ULR = "http://swap.promarkvf.hu/swap/web_service/";
	private static final int   RESULT_SETTINGS = 1;
	static Context             maincontext;
	static SetAppId            appID;
	static Profil              profil;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_swap);
		maincontext = SwapActivity.this;
		profil = new Profil();
		
		Button btnProfil = null;
		btnProfil = (Button) this.findViewById(R.id.btn_profil);
		OnClickListener profilClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		};
		btnProfil.setOnClickListener(profilClick);
		
		appID = new SetAppId();
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
	
}
