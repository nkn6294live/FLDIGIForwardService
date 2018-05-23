package com.bkav.FLDIGIForwardService.pack;

import java.io.Writer;

import org.json.JSONObject;

import com.bkav.FLDIGIForwardService.Config;

public class SOSFLDIGIPackage extends FLDIGIPackage {
	public String number;

	public SOSFLDIGIPackage(String macaddress, String[] headers, String[] lines) {
		super(macaddress, headers, lines);
		this.type = SOS_TYPE;
	}

	@Override
	public JSONObject exportData(Writer writer) throws Exception {
		super.exportData(writer);
		this.exportObject.put("number", this.number);
		return this.exportObject;
	}

	public void setNumber(String number) {
		this.validatePhoneNumber(number);
		this.number = number;
	}

	@Override
	protected void updateData() {
		super.updateData();
		this.key = "sos";
		this.number = Config.SOS_SAMPLE_PHONE_NUMBER;
	}
}
