package hu.promarkvf.swap;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;

public class ProductGroups {
	private static final String PRODUCTGROUPS_OLV_ULR = SwapActivity.WEB_SERVICE_ULR + "productgroupall.php";
	private ProductGroup[]      productGroups;
	public ArrayList<String>    descriptions          = new ArrayList<String>();
	
	public ArrayList<ProductGroup> getAll() {
		ArrayList<ProductGroup> pgList = new ArrayList<ProductGroup>();
		if (productGroups != null) {
			for (ProductGroup pg : productGroups) {
				pgList.add(pg);
			}
		}
		return pgList;
	}
	
	public boolean ReadDb(final Context context) {
		boolean ret = false;
		new DataBase(context) {
			private ProgressDialog progressDialog = null;
			
			@Override
			protected void onPostExecute(String result) {
				this.progressDialog.dismiss();
				if (result != null) {
					ProductGroups.this.FromJson(result);
				}
			}
			
			@Override
			protected void onPreExecute() {
				this.progressDialog = new ProgressDialog(context);
				this.progressDialog.setMessage(SwapActivity.maincontext.getText(R.string.assync_msg));
				this.progressDialog.show();
			}
			
		}.execute(PRODUCTGROUPS_OLV_ULR, "json=" + SwapActivity.dbProfil.ToJson().toString());
		return ret;
	}
	
	public void FromJson(String jsonstr) {
		JSONObject json;
		try {
			json = new JSONObject(jsonstr);
			// -- üress-e a root
			if (!json.isNull("productgroups")) {
				JSONArray jsonpgs = json.getJSONArray("productgroups");
				int db = jsonpgs.length();
				productGroups = new ProductGroup[db];
				// --- JSON feldolgozás
				for (int i = 0; i < db; i++) {
					JSONObject jsonpgr = jsonpgs.getJSONObject(i);
					productGroups[i] = new ProductGroup();
					productGroups[i].FromJson(jsonpgr);
					Date now = new Date();
					Date exp = new Date();
					try {
						exp = SwapActivity.dateFormat_smal.parse(productGroups[i].getExpire());
					} catch (ParseException e) {
						exp = now;
					}
					if (productGroups[i].getExpire() != "" && now.compareTo(exp) < 0) {
						this.descriptions.add(productGroups[i].getDescription());
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void DescriptionsRefresh() {
		Date now = new Date();
		Date exp = new Date();
		this.descriptions.clear();
		for (int i = 0; i < this.productGroups.length; i++) {
			try {
				exp = SwapActivity.dateFormat_smal.parse(productGroups[i].getExpire());
			} catch (ParseException e) {
				exp = now;
			}
			if (this.productGroups[i].getExpire() != "" && now.compareTo(exp) < 0) {
				this.descriptions.add(this.productGroups[i].getDescription());
			}
		}
	}
	
	public ProductGroup GetProducGroup(int index) {
		ProductGroup productgp = new ProductGroup();
		if (index >= 0 && index < this.productGroups.length) {
			productgp = this.productGroups[index];
		}
		return productgp;
	}
}
