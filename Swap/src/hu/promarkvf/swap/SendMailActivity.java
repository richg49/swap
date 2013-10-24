package hu.promarkvf.swap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class SendMailActivity extends Activity implements OnClickListener {
	EditText textBody;
	Intent   intent;
	String   parentStr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_mail);
		textBody = (EditText) findViewById(R.id.sm_body);
		
		intent = getIntent();
		
		String body = intent.getStringExtra("Body");
		String fejlec = intent.getStringExtra("Fejlec");
		parentStr = intent.getStringExtra("Parent");
		//		textBody.setText(megjegyzes);
		textBody.setText(body);
		setTitle(fejlec);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// --- Küldés
			case R.id.sm_send:
				Editable body = textBody.getText();
				intent.putExtra("Body", body.toString());
				if (parentStr != null) {
					intent.putExtra("Parent", parentStr);
				}
				setResult(RESULT_OK, intent);
				finish();
				break;
			
			// --- Mégsem
			case R.id.sm_cancel:
				setResult(RESULT_CANCELED);
				finish();
				break;
			
			default:
				break;
		}
	}
}
