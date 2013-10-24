package hu.promarkvf.swap;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductGroup {
	private int                id;
	private String             description;
	private int                owner;
	private String             create_date;
	private float              price;
	private String             currency;
	private String             expire;
	private boolean            want;
	private String             message;
	private ArrayList<Product> products;
	
	public ProductGroup(int id, String description, int owner, String create_date, float price, String currency, String expire, boolean want, String message) {
		super();
		this.setId(id);
		this.description = description;
		this.owner = owner;
		this.create_date = create_date;
		this.price = price;
		this.currency = currency;
		this.expire = expire;
		this.want = want;
		this.setMessage(message);
		this.products = new ArrayList<Product>();
	}
	
	public ProductGroup() {
		super();
		this.setId(0);
		this.description = "";
		this.owner = 0;
		this.create_date = "";
		this.price = 0f;
		this.currency = "";
		this.expire = "";
		this.want = false;
		this.setMessage("");
		this.products = new ArrayList<Product>();
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
		if (!json.isNull("productgroup")) {
			try {
				JSONObject jsonpg = json.getJSONObject("productgroup");
				this.setId(jsonpg.getInt("id"));
				this.description = jsonpg.getString("description");
				this.owner = jsonpg.getInt("owner");
				this.create_date = jsonpg.getString("create_date");
				this.price = jsonpg.getLong("price");
				this.currency = jsonpg.getString("currency");
				this.expire = jsonpg.isNull("expire") ? "" : jsonpg.getString("expire");
				// -- üress-e a root
				if (!jsonpg.isNull("product")) {
					JSONArray jsonp = jsonpg.getJSONArray("product");
					int db = jsonp.length();
					this.products.clear();
					// --- JSON feldolgozás
					for (int i = 0; i < db; i++) {
						JSONObject jsonprod = jsonp.getJSONObject(i);
						Product product = new Product();
						product.FromJson(jsonprod);
						this.products.add(product);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public final String getDescription() {
		return this.description;
	}
	
	public final void setDescription(String description) {
		this.description = description;
	}
	
	public final int getOwner() {
		return this.owner;
	}
	
	public final void setOwner(int owner) {
		this.owner = owner;
	}
	
	public final String getCreate_date() {
		return this.create_date;
	}
	
	public final void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	
	public final float getPrice() {
		return this.price;
	}
	
	public final void setPrice(float price) {
		this.price = price;
	}
	
	public final String getCurrency() {
		return this.currency;
	}
	
	public final void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getExpire() {
		return this.expire;
	}
	
	public void setExpire(String expire) {
		this.expire = expire;
	}
	
	public boolean isWant() {
		return this.want;
	}
	
	public void setWant(boolean want) {
		this.want = want;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public ArrayList<Product> getProduct() {
		return products;
	}
	
	public void setProduct(ArrayList<Product> product) {
		this.products = product;
	}
	
	public void AddProduct(Product product) {
		this.products.add(product);
	}
	
	public Product getProduct(int i) {
		Product ret = null;
		if (i >= 0 && i < this.products.size()) {
			ret = products.get(i);
		}
		return ret;
	}
	
	public ArrayList<Product> getProductList() {
		return products;
	}
	
	public int productsSize() {
		return this.products.size();
	}
	
}
