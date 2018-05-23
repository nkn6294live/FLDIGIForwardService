package com.bkav.FLDIGIForwardService.pack;

import java.io.Writer;

import org.json.JSONObject;

public class CallFLDIGIPackage extends FLDIGIPackage {
	public String number;

	public CallFLDIGIPackage(String macaddress, String[] headers, String[] lines) {
		super(macaddress, headers, lines);
		this.type = CALL_TYPE;
	}

	@Override
	public JSONObject exportData(Writer writer) throws Exception {
		super.exportData(writer);
		this.exportObject.put("number", this.number);
		return this.exportObject;
	}

	@Override
	protected void updateData() {
		super.updateData();
		this.key = "call";
		this.number = this.headers[2];
	}
}