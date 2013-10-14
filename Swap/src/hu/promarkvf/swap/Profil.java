package hu.promarkvf.swap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;

public class Profil {
	final static String PROFIL_IR_ULR  = SwapActivity.WEB_SERVICE_ULR + "profil_ir.php";
	final static String PROFIL_OLV_ULR = SwapActivity.WEB_SERVICE_ULR + "profil_olv.php";
	private int         id;
	private int         user_id;
	private String      UUID;
	private String      email;
	private String      facebook;
	private String      google;
	private String      address_city;
	private String      address_street;
	private String      address_postcode;
	private float       gps_lat;
	private float       gps_long;
	private String      name;
	private String      rname;
	private String      gmap;
	private String      language;
	private String      activation;
	
	protected Profil(String email, String facebook, String google, String address_city, String address_street, String address_postcode, String name, String rname, String gmap,
	        Float gps_lat, Float gps_long, String language) {
		super();
		this.UUID = SetAppId.id(SwapActivity.maincontext);
		this.email = email;
		this.facebook = facebook;
		this.google = google;
		this.address_city = address_city;
		this.address_street = address_street;
		this.address_postcode = address_postcode;
		this.name = name;
		this.rname = rname;
		this.gmap = gmap;
		this.gps_lat = gps_lat;
		this.gps_long = gps_long;
		this.language = language;
		this.activation = "N";
	}
	
	protected Profil() {
		super();
		this.UUID = SetAppId.id(SwapActivity.maincontext);
		this.email = "";
		this.facebook = "";
		this.google = "";
		this.address_city = "";
		this.address_street = "";
		this.address_postcode = "";
		this.name = "";
		this.rname = "";
		this.gmap = "";
		this.gps_lat = 0f;
		this.gps_long = 0f;
		this.language = "HU";
		this.activation = "N";
	}
	
	public boolean SaveDb() {
		boolean ret = false;
		JSONObject json = this.ToJson();
		if (json.length() != 0) {
			new DataBase(SwapActivity.maincontext) {
				private ProgressDialog progressDialog = null;
				
				@Override
				protected void onPostExecute(String result) {
					this.progressDialog.dismiss();
					if (result != null) {
						System.out.println(result);
					}
				}
				
				@Override
				protected void onPreExecute() {
					this.progressDialog = new ProgressDialog(SwapActivity.maincontext);
					this.progressDialog.setMessage(SwapActivity.maincontext.getText(R.string.assync_msg));
					this.progressDialog.show();
				}
				
			}.execute(PROFIL_IR_ULR, "json=" + json);
		}
		return ret;
	}
	
	public boolean ReadDb() {
		boolean ret = false;
		if (this.UUID != "" || this.email != "") {
			new DataBase(SwapActivity.maincontext) {
				private ProgressDialog progressDialog = null;
				
				@Override
				protected void onPostExecute(String result) {
					this.progressDialog.dismiss();
					if (result != null) {
						Profil.this.FromJson(result);
					}
				}
				
				@Override
				protected void onPreExecute() {
					this.progressDialog = new ProgressDialog(SwapActivity.maincontext);
					this.progressDialog.setMessage(SwapActivity.maincontext.getText(R.string.assync_msg));
					this.progressDialog.show();
				}
				
			}.execute(PROFIL_OLV_ULR, "uuid=" + this.UUID, "email=" + this.email);
		}
		return ret;
	}
	
	public JSONObject ToJson() {
		JSONObject json;
		json = new JSONObject();
		try {
			json.put("id", this.id);
			json.put("UUID", URLEncoder.encode(this.UUID, "UTF-8"));
			json.put("email", URLEncoder.encode(this.email, "UTF-8"));
			json.put("facebook", URLEncoder.encode(this.facebook, "UTF-8"));
			json.put("google", URLEncoder.encode(this.google, "UTF-8"));
			json.put("address_city", URLEncoder.encode(this.address_city, "UTF-8"));
			json.put("address_street", URLEncoder.encode(this.address_street, "UTF-8"));
			json.put("address_postcode", URLEncoder.encode(this.address_postcode, "UTF-8"));
			json.put("gps_lat", this.gps_lat);
			json.put("gps_long", this.gps_long);
			json.put("name", URLEncoder.encode(this.name, "UTF-8"));
			json.put("rname", URLEncoder.encode(this.rname, "UTF-8"));
			json.put("gmap", URLEncoder.encode(this.gmap, "UTF-8"));
			json.put("language", URLEncoder.encode(this.language, "UTF-8"));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public void FromJson(String jsonstr) {
		JSONObject json;
		try {
			json = new JSONObject(jsonstr);
			if (!json.isNull("UUID")) {
				try {
					this.id = json.getInt("id");
					// this.user_id = json.getInt("user_id");
					this.UUID = json.getString("UUID");
					this.email = json.getString("email");
					this.facebook = json.getString("facebook");
					this.google = json.getString("google");
					this.address_city = json.getString("address_city");
					this.address_street = json.getString("address_street");
					this.address_postcode = json.getString("address_postcode");
					this.gps_lat = json.getLong("gps_lat");
					this.gps_long = json.getLong("gps_long");
					this.name = json.getString("name");
					this.rname = json.getString("rname");
					this.gmap = json.getString("gmap");
					this.language = json.getString("language");
					this.activation = json.getString("activation");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void setId(int value) {
		this.id = value;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getORMID() {
		return this.getId();
	}
	
	public void setUser_id(int value) {
		this.user_id = value;
	}
	
	public int getUser_id() {
		return this.user_id;
	}
	
	public void setUUID(String value) {
		this.UUID = value;
	}
	
	public String getUUID() {
		return this.UUID;
	}
	
	public void setEmail(String value) {
		this.email = value;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setFacebook(String value) {
		this.facebook = value;
	}
	
	public String getFacebook() {
		return this.facebook;
	}
	
	public void setGoogle(String value) {
		this.google = value;
	}
	
	public String getGoogle() {
		return this.google;
	}
	
	public void setAddress_city(String value) {
		this.address_city = value;
	}
	
	public String getAddress_city() {
		return this.address_city;
	}
	
	public void setAddress_street(String value) {
		this.address_street = value;
	}
	
	public String getAddress_street() {
		return this.address_street;
	}
	
	public void setAddress_postcode(String value) {
		this.address_postcode = value;
	}
	
	public String getAddress_postcode() {
		return this.address_postcode;
	}
	
	public void setGps_lat(float value) {
		this.gps_lat = value;
	}
	
	public float getGps_lat() {
		return this.gps_lat;
	}
	
	public void setGps_long(float value) {
		this.gps_long = value;
	}
	
	public float getGps_long() {
		return this.gps_long;
	}
	
	public void setName(String value) {
		this.name = value;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setGmap(String value) {
		this.gmap = value;
	}
	
	public String getGmap() {
		return this.gmap;
	}
	
	public final String getRname() {
		return this.rname;
	}
	
	public final void setRname(String rname) {
		this.rname = rname;
	}
	
	public void setGps_long(String strlong) {
		try {
			this.gps_long = Float.valueOf(strlong);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	public void setGps_lat(String strlat) {
		try {
			this.gps_lat = Float.valueOf(strlat);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	public final String getLanguage() {
		return this.language;
	}
	
	public final void setLanguage(String language) {
		this.language = language;
	}
	
	public boolean isActivated() {
		if (this.activation.length() == 0) {
			return true;
		} else {
			this.ReadDb();
			return false;
		}
	}
	
	public String isActivatedString() {
		String ret = "";
		if (this.isActivated()) {
			ret = (String) SwapActivity.maincontext.getText(R.string.tvProfilStatusOn);
		} else {
			ret = (String) SwapActivity.maincontext.getText(R.string.tvProfilStatusOff);
		}
		return ret;
	}
}
