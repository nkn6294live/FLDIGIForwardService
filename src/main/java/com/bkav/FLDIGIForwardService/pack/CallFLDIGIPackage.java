package com.bkav.FLDIGIForwardService.pack;

import org.json.JSONException;
import org.json.JSONObject;

public class CallFLDIGIPackage extends FLDIGIPackage {
	public String number;

	public CallFLDIGIPackage(FLDIGIHeader header) {
		super(header);
	}
	@Override
	public JSONObject exportData() throws JSONException {
		super.exportData();
		this.exportObject.put("number", this.number);
		return this.exportObject;
	}

	@Override
	protected void updateData() {
		super.updateData();
		this.number = this.header.getParam(0);
	}
}