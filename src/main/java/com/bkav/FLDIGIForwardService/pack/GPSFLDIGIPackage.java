package com.bkav.FLDIGIForwardService.pack;

import org.json.JSONException;
import org.json.JSONObject;

public class GPSFLDIGIPackage extends FLDIGIPackage {

	public GPSFLDIGIPackage(FLDIGIHeader header) {
		super(header);
	}

	public double getLAT() {
		return this.lat;
	}
	
	public double getLNG() {
		return this.lng;
	}
	
	@Override
	public JSONObject exportData() throws JSONException {
		super.exportData();
		this.exportObject.put("lat", new Double(this.lat));
		this.exportObject.put("lng", new Double(this.lng));
		return this.exportObject;
	}

	@Override
	protected void updateData() {
		super.updateData();
		this.lat = Double.valueOf(this.header.getParam(0));
		this.lng = Double.valueOf(this.header.getParam(1));
	}
	
	private double lat;
	private double lng;
}