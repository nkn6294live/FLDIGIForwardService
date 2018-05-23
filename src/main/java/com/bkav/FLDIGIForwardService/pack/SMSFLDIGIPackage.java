package com.bkav.FLDIGIForwardService.pack;

import java.io.Writer;

import org.json.JSONObject;

public class SMSFLDIGIPackage extends FLDIGIPackage {
	public String number;
	public String data;

	public SMSFLDIGIPackage(String macaddress, String[] headers, String[] lines) {
		super(macaddress, headers, lines);
		this.type = SMS_TYPE;
	}

	@Override
	public JSONObject exportData(Writer writer) throws Exception {
		super.exportData(writer);
		this.exportObject.put("number", this.number);
		this.exportObject.put("data", this.data);
		return this.exportObject;
	}

	protected final void loadData() {
		StringBuilder builder = new StringBuilder();
		for (int smsIndex = 3; smsIndex < this.headers.length; smsIndex++) {
			builder.append(" ").append(this.headers[smsIndex]);
		}
		for (int index = 1; index < this.lines.length; index++) {
			String line = this.lines[index];// line.split(" ");
			builder.append(" ").append(line);
		}
		this.data = builder.toString();
	}

	@Override
	protected void updateData() {
		super.updateData();
		this.key = "sms";
		this.number = this.headers[2];
		this.loadData();
	}
}
