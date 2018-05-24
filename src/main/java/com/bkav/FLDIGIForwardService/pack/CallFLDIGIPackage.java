package com.bkav.FLDIGIForwardService.pack;

import org.json.JSONException;
import org.json.JSONObject;

public class CallFLDIGIPackage extends FLDIGIPackage {

	public CallFLDIGIPackage(FLDIGIHeader header) {
		super(header);
	}
	@Override
	public JSONObject exportData() throws JSONException {
		super.exportData();
		this.exportObject.put("number", this.number);
		return this.exportObject;
	}

	public String number() {
		return this.number;
	}
	
	public void number(String number) {
		this.number = number;
	}
	
	@Override
	public String toString() {
		return String.format("%s [header=%s, number=%s]", this.getClass().getSimpleName(),
				this.header.toString(), this.number);
	}
	
	@Override
	protected void updateData() {
		super.updateData();
		this.number = this.header.getParam(0);
	}
	
	private String number;
}