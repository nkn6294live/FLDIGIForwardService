package com.bkav.FLDIGIForwardService.pack;

import java.io.Writer;

import org.json.JSONObject;

public class GPSFLDIGIPackage extends FLDIGIPackage {
	public double lat;
	public double lng;

	public GPSFLDIGIPackage(String macaddress, String[] headers, String[] lines) {
		super(macaddress, headers, lines);
		this.type = GPS_TYPE;
		this.key = "sentdata";
	}

	@Override
	public JSONObject exportData(Writer writer) throws Exception {
		super.exportData(writer);
		this.exportObject.put("lat", new Double(this.lat));
		this.exportObject.put("lng", new Double(this.lng));
		return this.exportObject;
	}

	@Override
	protected void updateData() {
		super.updateData();
		this.lat = Double.valueOf(this.headers[2]);
		this.lng = Double.valueOf(this.headers[3]);
	}
}