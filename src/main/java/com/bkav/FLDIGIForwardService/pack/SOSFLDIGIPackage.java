package com.bkav.FLDIGIForwardService.pack;

import org.json.JSONException;
import org.json.JSONObject;

import com.bkav.FLDIGIForwardService.Config;

public class SOSFLDIGIPackage extends FLDIGIPackage {

	public SOSFLDIGIPackage(FLDIGIHeader headers) {
		super(headers);
	}

	@Override
	public JSONObject exportData() throws JSONException {
		super.exportData();
		this.exportObject.put("number", this.number);
		return this.exportObject;
	}

	public void number(String number) {
		this.number = number;
	}

	public String number() {
		return this.number;
	}

	@Override
	protected void updateData() {
		super.updateData();
		this.number = Config.SOS_SAMPLE_PHONE_NUMBER;
	}

	private String number;
}
