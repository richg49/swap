package hu.promarkvf.swap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;

public class Profil {
	private int    id;
	private int    user_id;
	private String UUID;
	private String email;
	private String facebook;
	private String google;
	private String address_city;
	private String address_street;
	private String address_postcode;
	private float  gps_lat;
	private float  gps_long;
	private String name;
	private String gmap;
	
	protected Profil(String email, String facebook, String google, String address_city, String address_street, String address_postcode, String name, String gmap) {
		super();
		this.email = email;
		this.facebook = facebook;
		this.google = google;
		this.address_city = address_city;
		this.address_street = address_street;
		this.address_postcode = address_postcode;
		this.name = name;
		this.gmap = gmap;
	}
	
	protected Profil() {
		super();
		this.email = null;
		this.facebook = null;
		this.google = null;
		this.address_city = null;
		this.address_street = null;
		this.address_postcode = null;
		this.name = null;
		this.gmap = null;
	}
	
	public boolean SaveDb() {
		boolean ret = false;
		JSONObject json = this.ToJson();
		if (json.length() != 0) {
			new DataBase(SwapActivity.maincontext) {
				private ProgressDialog progressDialog = null;
				
				@Override
				protected void onPostExecute(String result) {
					progressDialog.dismiss();
					if (result != null) {
						JSONObject json= new JSONObject();;
                        try {
	                        json = new JSONObject(result);
                        } catch (JSONException e) {
	                        e.printStackTrace();
                        }
						FromJson(json);
					}
				}
				
				@Override
				protected void onPreExecute() {
					progressDialog = new ProgressDialog(SwapActivity.maincontext);
					progressDialog.setMessage("Kérem várjon...");
					progressDialog.show();
				}
				
			}.execute("webservice_kezelesid.php", "json=" + json);
		}
		return ret;
	}
	
	private JSONObject ToJson() {
		JSONObject json;
		json = new JSONObject();
		try {
			json.put("email", URLEncoder.encode(this.email, "UTF-8"));
			json.put("facebook", URLEncoder.encode(this.facebook, "UTF-8"));
			json.put("google", URLEncoder.encode(this.google, "UTF-8"));
			json.put("address_city", URLEncoder.encode(this.address_city, "UTF-8"));
			json.put("address_street", URLEncoder.encode(this.address_street, "UTF-8"));
			json.put("address_postcode", URLEncoder.encode(this.address_postcode, "UTF-8"));
			json.put("gps_lat", this.gps_lat);
			json.put("gps_long", this.gps_long);
			json.put("name", URLEncoder.encode(this.name, "UTF-8"));
			json.put("gmap", URLEncoder.encode(this.gmap, "UTF-8"));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	private void FromJson(JSONObject json) {
		if ( !json.isNull("id") ) {
			try {
	            this.id = json.getInt("id");
				this.user_id = json.getInt("user_id");
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
				this.gmap = json.getString("gmap");
            } catch (JSONException e) {
	            e.printStackTrace();
            }
		}

	}
	
	private void setId(int value) {
		this.id = value;
	}
	
	public int getId() {
		return id;
	}
	
	public int getORMID() {
		return getId();
	}
	
	public void setUser_id(int value) {
		this.user_id = value;
	}
	
	public int getUser_id() {
		return user_id;
	}
	
	public void setUUID(String value) {
		this.UUID = value;
	}
	
	public String getUUID() {
		return UUID;
	}
	
	public void setEmail(String value) {
		this.email = value;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setFacebook(String value) {
		this.facebook = value;
	}
	
	public String getFacebook() {
		return facebook;
	}
	
	public void setGoogle(String value) {
		this.google = value;
	}
	
	public String getGoogle() {
		return google;
	}
	
	public void setAddress_city(String value) {
		this.address_city = value;
	}
	
	public String getAddress_city() {
		return address_city;
	}
	
	public void setAddress_street(String value) {
		this.address_street = value;
	}
	
	public String getAddress_street() {
		return address_street;
	}
	
	public void setAddress_postcode(String value) {
		this.address_postcode = value;
	}
	
	public String getAddress_postcode() {
		return address_postcode;
	}
	
	public void setGps_lat(float value) {
		this.gps_lat = value;
	}
	
	public float getGps_lat() {
		return gps_lat;
	}
	
	public void setGps_long(float value) {
		this.gps_long = value;
	}
	
	public float getGps_long() {
		return gps_long;
	}
	
	public void setName(String value) {
		this.name = value;
	}
	
	public String getName() {
		return name;
	}
	
	public void setGmap(String value) {
		this.gmap = value;
	}
	
	public String getGmap() {
		return gmap;
	}
	
}
