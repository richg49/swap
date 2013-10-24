package hu.promarkvf.swap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class VerifyBuyActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_verify_buy);
		
		Button btn_elofizet = (Button) this.findViewById(R.id.btn_elofizet_v);
		Button btn_kilep = (Button) this.findViewById(R.id.btn_cancel_v);
		
		btn_kilep.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				VerifyBuyActivity.this.setResult(RESULT_CANCELED);
				VerifyBuyActivity.this.finish();
			}
		});
		
		btn_elofizet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				VerifyBuyActivity.this.setResult(RESULT_OK);
				VerifyBuyActivity.this.finish();
			}
		});
	}
	
}
