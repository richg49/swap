package hu.promarkvf.swap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Message {
	public static final String MESSAGE_STATUS_IR_ULR = SwapActivity.WEB_SERVICE_ULR + "message_status_ir.php"; ;
	public static final String MESSAGE_IR_ULR        = SwapActivity.WEB_SERVICE_ULR + "message_ir.php";
	public final static int    MESSAGE_NEW           = 1;
	public final static int    MESSAGE_DEL           = 2;
	public final static int    MESSAGE_READ          = 0;
	protected int              id;
	protected int              parent_id;
	protected int              user_from;
	protected String           user_from_name;
	protected int              user_to;
	protected String           user_to_name;
	protected String           date;
	protected int              product_id;
	protected String           message;
	protected int              status_from;
	protected int              status_to;
	
	public Message() {
		super();
		this.id = 0;
		this.parent_id = 0;
		this.user_from = 0;
		this.user_to = 0;
		this.date = "";
		this.product_id = 0;
		this.message = "";
		this.user_from_name = "";
		this.user_to_name = "";
		this.status_from = 0;
		this.status_to = 0;
	}
	
	public Message(int id, int parent_id, int user_from, int user_to, String date, int roduct_id, String message) {
		super();
		this.id = id;
		this.parent_id = parent_id;
		this.user_from = user_from;
		this.user_to = user_to;
		this.date = date;
		this.product_id = roduct_id;
		this.message = message;
		this.user_from_name = "";
		this.user_to_name = "";
		this.status_from = 0;
		this.status_to = 0;
	}
	
	public Message(JSONObject json) {
		this();
		try {
			if (!json.isNull("id")) {
				this.id = json.getInt("id");
			}
			if (!json.isNull("date")) {
				this.date = json.getString("date");
			}
			if (!json.isNull("parent_id")) {
				this.parent_id = json.getInt("parent_id");
			}
			if (!json.isNull("user_from")) {
				this.user_from = json.getInt("user_from");
			}
			if (!json.isNull("user_from_name")) {
				this.user_from_name = json.getString("user_from_name");
			}
			if (!json.isNull("user_to")) {
				this.user_to = json.getInt("user_to");
			}
			if (!json.isNull("user_to_name")) {
				this.user_to_name = json.getString("user_to_name");
			}
			if (!json.isNull("message")) {
				this.message = json.getString("message");
			}
			if (!json.isNull("product_id")) {
				this.product_id = json.getInt("product_id");
			}
			if (!json.isNull("status_from")) {
				this.status_from = json.getInt("status_from");
			}
			if (!json.isNull("status_to")) {
				this.status_to = json.getInt("status_to");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public final int getId() {
		return id;
	}
	
	public final void setId(int id) {
		this.id = id;
	}
	
	public final int getParent_id() {
		return parent_id;
	}
	
	public final void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}
	
	public final int getUser_from() {
		return user_from;
	}
	
	public final void setUser_from(int user_from) {
		this.user_from = user_from;
	}
	
	public final int getUser_to() {
		return user_to;
	}
	
	public final void setUser_to(int user_to) {
		this.user_to = user_to;
	}
	
	public final String getDate() {
		return date;
	}
	
	public final Date getDateDate() {
		Date ret = null;
		try {
			ret = SwapActivity.dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public final void setDate(String date) {
		this.date = date;
	}
	
	public final int getProduct_id() {
		return product_id;
	}
	
	public final void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	
	public final String getMessage() {
		return message;
	}
	
	public final void setMessage(String messageRead) {
		this.message = messageRead;
	}
	
	public JSONObject toJson() {
		JSONObject json;
		json = new JSONObject();
		try {
			json.put("id", this.id);
			json.put("parent_id", this.parent_id);
			json.put("user_from", this.user_from);
			json.put("user_to", this.user_to);
			json.put("date", this.date);
			json.put("product_id", this.product_id);
			json.put("message", URLEncoder.encode(this.message, "UTF-8"));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public final String getUser_from_name() {
		return user_from_name;
	}
	
	public final void setUser_from_name(String user_from_name) {
		this.user_from_name = user_from_name;
	}
	
	public final String getUser_to_name() {
		return user_to_name;
	}
	
	public final void setUser_to_name(String user_to_name) {
		this.user_to_name = user_to_name;
	}
	
	@SuppressWarnings("static-access")
	public final Boolean isNew_message_from() {
		if (this.status_from == this.MESSAGE_NEW) {
			return true;
		} else {
			return false;
		}
	}
	
	@SuppressWarnings("static-access")
	public final Boolean isDel_message_from() {
		if (this.status_from == this.MESSAGE_DEL) {
			return true;
		} else {
			return false;
		}
	}
	
	@SuppressWarnings("static-access")
	public final Boolean isRead_message_from() {
		if (this.status_from == this.MESSAGE_READ) {
			return true;
		} else {
			return false;
		}
	}
	
	public final void setStatus_from(int status) {
		this.status_from = status;
	}
	
	@SuppressWarnings("static-access")
	public final Boolean isNew_message_to() {
		if (this.status_to == this.MESSAGE_NEW) {
			return true;
		} else {
			return false;
		}
	}
	
	@SuppressWarnings("static-access")
	public final Boolean isDel_message_to() {
		if (this.status_to == this.MESSAGE_DEL) {
			return true;
		} else {
			return false;
		}
	}
	
	@SuppressWarnings("static-access")
	public final Boolean isRead_message_to() {
		if (this.status_to == this.MESSAGE_READ) {
			return true;
		} else {
			return false;
		}
	}
	
	public final void setStatus_to(int status) {
		this.status_to = status;
	}
}
