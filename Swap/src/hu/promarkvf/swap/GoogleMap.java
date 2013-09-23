package hu.promarkvf.swap;

public class GoogleMap {
	private float lon;
	private float lat;
	private int zoom;
	private String address;

	public GoogleMap() {
		// TODO Auto-generated constructor stub
	}

	public GoogleMap(float lon, float lat, int zoom) {
		super();
		this.lon = lon;
		this.lat = lat;
		this.zoom = zoom;
		this.address = null;
	}

	public GoogleMap(String pos) {
		super();
		String[] sp = pos.split("[(),:]");
		if (sp.length == 5) {
			this.lon = Float.valueOf(sp[1]);
			this.lat = Float.valueOf(sp[3]);
			this.zoom = Integer.valueOf(sp[5]);
		}
		this.address = null;
	}

	public final float getLon() {
		return lon;
	}

	public final float getLat() {
		return lat;
	}

	public final String getAddress() {
		return address;
	}

}
