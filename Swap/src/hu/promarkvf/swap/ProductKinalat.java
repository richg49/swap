package hu.promarkvf.swap;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductKinalat {
	private int    profil_product_id;
	private int    product_id;
	private int    count;
	private String email;
	private String name;
	private String rname;
	private int    profil_id;
	
	public final int getProfil_product_id() {
		return profil_product_id;
	}
	
	public final void setProfil_product_id(int profil_product_id) {
		this.profil_product_id = profil_product_id;
	}
	
	public final int getDarab() {
		return count;
	}
	
	public final void setDarab(int count) {
		this.count = count;
	}
	
	public final String getEmail() {
		return email;
	}
	
	public final void setEmail(String email) {
		this.email = email;
	}
	
	public final int getProfil_id() {
		return profil_id;
	}
	
	public final void setProfil_id(int profil_id) {
		this.profil_id = profil_id;
	}
	
	public ProductKinalat(int profil_product_id, int count, String email, String name, String rname, int profil_id) {
		super();
		this.profil_product_id = profil_product_id;
		this.count = count;
		this.email = email;
		this.name = name;
		this.rname = rname;
		this.profil_id = profil_id;
		this.product_id = 0;
	}
	
	public ProductKinalat() {
		super();
		this.profil_product_id = 0;
		this.count = 0;
		this.email = "";
		this.name = "";
		this.rname = "";
		this.profil_id = 0;
		this.product_id = 0;
	}
	
	public ProductKinalat(JSONObject json) {
		super();
		this.profil_product_id = 0;
		this.product_id = 0;
		this.count = 0;
		this.email = "";
		this.profil_id = 0;
		this.name = "";
		this.rname = "";
		if (!json.isNull("profil")) {
			try {
				JSONObject json_t = json.getJSONObject("profil");
				if (!json_t.isNull("profil_product_id")) {
					this.profil_product_id = json_t.getInt("id");
				}
				if (!json_t.isNull("count")) {
					this.count = json_t.getInt("count");
				}
				if (!json_t.isNull("email")) {
					this.email = json_t.getString("email");
				}
				if (!json_t.isNull("name")) {
					this.name = json_t.getString("name");
				}
				if (!json_t.isNull("rname")) {
					this.rname = json_t.getString("rname");
				}
				if (!json_t.isNull("profil_id")) {
					this.profil_id = json_t.getInt("profil_id");
				}
				if (!json_t.isNull("product_id")) {
					this.product_id = json_t.getInt("product_id");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public final String getName() {
		return name;
	}
	
	public final void setName(String name) {
		this.name = name;
	}
	
	public final String getRname() {
		return rname;
	}
	
	public final void setRname(String rname) {
		this.rname = rname;
	}
	
	public final int getProduct_id() {
		return product_id;
	}
	
	public final void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	
}
