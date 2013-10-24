package hu.promarkvf.swap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

public class ProductBuyActivity extends Activity implements OnClickListener {
	protected static final int         VERIFY_BUY_ACTIVITY_ID = 1;
	protected static final String      PRODUCTGROUP_BUY_ULR   = SwapActivity.WEB_SERVICE_ULR + "profil_pg_ir.php";
	ArrayList<ProductGroup>            pgList                 = new ArrayList<ProductGroup>();
	AdapterView.AdapterContextMenuInfo info;
	int                                ret                    = 0;
	ListView                           lv1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_product_buy);
		
		this.pgList = SwapActivity.pgs.getAll();
		
		this.lv1 = (ListView) this.findViewById(R.id.pgb_list);
		this.lv1.setAdapter(new PgArrayAdapter(this, this.pgList));
		this.lv1.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				Object o = ProductBuyActivity.this.lv1.getItemAtPosition(position);
				ProductGroup pg = (ProductGroup) o;
				Toast.makeText(ProductBuyActivity.this, "Selected :" + " " + pg.getDescription(), Toast.LENGTH_LONG).show();
			}
			
		});
		
		Button btn_elofizet = (Button) this.findViewById(R.id.btn_elofizet);
		Button btn_kilep = (Button) this.findViewById(R.id.btn_cancel);
		
		btn_kilep.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ProductBuyActivity.this.finish();
			}
		});
		
		btn_elofizet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent myIntent = new Intent();
				myIntent.setClass(ProductBuyActivity.this, VerifyBuyActivity.class);
				ProductBuyActivity.this.startActivityForResult(myIntent, VERIFY_BUY_ACTIVITY_ID);
			}
		});
		
		// Feliratkozás a hosszú lenyomás hatására előjövő menü kezelésére
		// registerForContextMenu(getListView());
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case VERIFY_BUY_ACTIVITY_ID:
				if (resultCode == RESULT_OK) {
					Date now = new Date();
					Calendar cal = Calendar.getInstance();
					cal.setTime(now);
					cal.add(Calendar.YEAR, 1);
					now = cal.getTime();
					JSONObject json;
					json = new JSONObject();
					int darab = 0;
					for (int i = 0; i < this.pgList.size(); i++) {
						ProductGroup pg = this.pgList.get(i);
						if (pg.isWant()) {
							try {
								JSONObject pgobject = new JSONObject();
								pgobject.put("productgroup_id", pg.getId());
								pg.setExpire(pg.getExpire() == "" ? SwapActivity.dateFormat_smal.format(now) : SwapActivity.dateFormat_smal.format(now));
								pgobject.put("expire", pg.getExpire());
								pgobject.put("message", pg.getMessage());
								json.put(Integer.toString(darab), pgobject);
								darab++;
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
					try {
						json.put("profil_id", SwapActivity.dbProfil.getId());
						json.put("darab", darab);
						json.put("UUID", SwapActivity.dbProfil.getUUID());
						json.put("email", SwapActivity.dbProfil.getEmail());
					} catch (JSONException e) {
						e.printStackTrace();
					}
					new DataBase(ProductBuyActivity.this) {
						private ProgressDialog progressDialog = null;
						
						@Override
						protected void onPostExecute(String result) {
							this.progressDialog.dismiss();
							if (result != null) {
								ProductBuyActivity.this.lv1.invalidateViews();
								SwapActivity.pgs.DescriptionsRefresh();
							}
						}
						
						@Override
						protected void onPreExecute() {
							this.progressDialog = new ProgressDialog(ProductBuyActivity.this);
							this.progressDialog.setMessage(SwapActivity.maincontext.getText(R.string.assync_msg));
							this.progressDialog.show();
						}
						
					}.execute(PRODUCTGROUP_BUY_ULR, "json=" + json.toString());
					
				}
				break;
		}
		
	}
	
	@Override
	public void onClick(View arg0) {
		if (arg0 instanceof CheckBox) {
			CheckBox cb = (CheckBox) arg0;
			int position = (Integer) cb.getTag();
			this.pgList.get(position).setWant(cb.isChecked());
		}
	}
}
