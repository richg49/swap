package hu.promarkvf.swap;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

public class Product {
	private int     id;
	private int     profil_id;
	private int     group_id;
	private String  description;
	private String  expire;
	private String  create_date;
	private String  modify;
	private String  name;
	private String  image_smal;
	private String  image_large;
	private Bitmap  image_s;
	private Bitmap  image_l;
	private boolean search;
	private boolean offer;
	private int     keszlet;
	private int     count;
	private String  message;
	
	public Product(int id, int profil_id, int group_id, String description, String expiry, String create_date, String modify, String name, String image_smal, String image_large) {
		super();
		this.id = id;
		this.profil_id = profil_id;
		this.group_id = group_id;
		this.description = description;
		this.expire = expiry;
		this.create_date = create_date;
		this.modify = modify;
		this.name = name;
		this.image_smal = image_smal;
		this.image_large = image_large;
		this.search = false;
		this.offer = false;
		this.count = 0;
		this.keszlet = 0;
		this.message = "";
	}
	
	public Product() {
		super();
		this.id = 0;
		this.profil_id = 0;
		this.group_id = 0;
		this.description = "";
		this.expire = "";
		this.create_date = "";
		this.modify = "";
		this.name = "";
		this.image_smal = "";
		this.image_large = "";
		this.search = false;
		this.offer = false;
		this.count = 0;
		this.keszlet = 0;
		this.message = "";
	}
	
	public void FromJson(String jsonstr) {
		JSONObject json;
		try {
			json = new JSONObject(jsonstr);
			this.FromJson(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void FromJson(JSONObject json) {
		if (!json.isNull("product")) {
			try {
				JSONObject jsonpg = json.getJSONObject("product");
				this.id = jsonpg.getInt("id");
				this.profil_id = jsonpg.getInt("profil_id");
				this.group_id = jsonpg.getInt("group_id");
				this.description = jsonpg.getString("description");
				this.expire = jsonpg.getString("expire");
				this.create_date = jsonpg.getString("create_date");
				this.modify = jsonpg.getString("modify");
				this.name = jsonpg.getString("name");
				this.image_smal = jsonpg.getString("image_smal");
				this.image_large = jsonpg.getString("image_large");
				if (!jsonpg.isNull("count")) {
					this.count = jsonpg.getInt("count");
				}
				if (!jsonpg.isNull("search")) {
					this.search = jsonpg.getInt("search") == 0 ? Boolean.FALSE : Boolean.TRUE;
				}
				if (!jsonpg.isNull("offer")) {
					this.offer = jsonpg.getInt("offer") == 0 ? Boolean.FALSE : Boolean.TRUE;
				}
				if (!jsonpg.isNull("message")) {
					this.message = jsonpg.getString("message");
				}
				if (!jsonpg.isNull("db")) {
					this.keszlet = jsonpg.getInt("db");
				}
				new DownloadImageTask() {
					@Override
					protected void onPostExecute(Bitmap result) {
						if (result != null) {
							image_s = Bitmap.createBitmap(result);
						}
					}
					
				}.execute(SwapActivity.WEB_HOST + this.image_smal);
				new DownloadImageTask() {
					@Override
					protected void onPostExecute(Bitmap result) {
						if (result != null) {
							image_l = Bitmap.createBitmap(result);
						}
					}
					
				}.execute(SwapActivity.WEB_HOST + this.image_large);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public final int getId() {
		return id;
	}
	
	public String getIdStr() {
		return String.valueOf(id);
	}
	
	public final void setId(int id) {
		this.id = id;
	}
	
	public int getProfil_id() {
		return profil_id;
	}
	
	public void setProfil_id(int profil_id) {
		this.profil_id = profil_id;
	}
	
	public int getGroup_id() {
		return group_id;
	}
	
	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getExpiry() {
		return expire;
	}
	
	public void setExpiry(String expire) {
		this.expire = expire;
	}
	
	public String getCreate_date() {
		return create_date;
	}
	
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	
	public final String getModify() {
		return modify;
	}
	
	public void setModify(String modify) {
		this.modify = modify;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getImage_smal() {
		return image_smal;
	}
	
	public void setImage_smal(String image_smal) {
		this.image_smal = image_smal;
	}
	
	public String getImage_large() {
		return image_large;
	}
	
	public void setImage_large(String image_large) {
		this.image_large = image_large;
	}
	
	public Bitmap getImage_s() {
		return image_s;
	}
	
	public void setImage_s(Bitmap image_s) {
		this.image_s = image_s;
	}
	
	public Bitmap getImage_l() {
		return image_l;
	}
	
	public void setImage_l(Bitmap image_l) {
		this.image_l = image_l;
	}
	
	public String getExpire() {
		return expire;
	}
	
	public void setExpire(String expire) {
		this.expire = expire;
	}
	
	public final boolean isSearch() {
		return search;
	}
	
	public void setSearch(boolean search) {
		this.search = search;
	}
	
	public boolean isOffer() {
		return offer;
	}
	
	public void setOffer(boolean offer) {
		this.offer = offer;
	}
	
	public final int getKeszlet() {
		return keszlet;
	}
	
	public final String getKeszletStr() {
		return String.valueOf(keszlet);
	}
	
	public final void setKeszlet(int keszlet) {
		this.keszlet = keszlet;
	}
	
	public final int getCount() {
		return count;
	}
	
	public final void setCount(int keszlet_kinal) {
		this.count = keszlet_kinal;
	}
	
	public final String getMessage() {
		return message;
	}
	
	public final void setMessage(String message) {
		this.message = message;
	}
	
}
