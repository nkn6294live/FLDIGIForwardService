package com.bkav.FLDIGIForwardService.pack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.bkav.FLDIGIForwardService.SystemManager;

public class SMSFLDIGIPackage extends FLDIGIPackage {

	public SMSFLDIGIPackage(FLDIGIHeader header, Reader reader) throws IOException {
		super(header, reader);
	}

	@Override
	public JSONObject exportData() throws JSONException {
		super.exportData();
		this.exportObject.put("number", this.number);
		this.exportObject.put("data", this.data);
		return this.exportObject;
	}

	public String number() {
		return this.number;
	}

	public String data() {
		return this.data;
	}
	@Override
	public String toString() {
		return String.format("%s [header=%s, number=%s, data=%s, lines=%s]", 
				this.getClass().getSimpleName(), this.header.toString(), this.number, this.data, Arrays.toString(this.lines));
	}
	@Override
	protected void updateData() {
		super.updateData();
		this.number = this.header.getParam(0);
		this.loadData();
	}
	
	@Override
	protected void loadDataFromReader(Reader reader) throws IOException {
		super.loadDataFromReader(reader);
		BufferedReader bufferReader;
		if (reader instanceof BufferedReader) {
			bufferReader = (BufferedReader) reader;
		} else {
			bufferReader = new BufferedReader(reader);
		}
		List<String> listLine = new ArrayList<>();
		String line;
		while ((line = bufferReader.readLine()) != null) {
			SystemManager.logger.info("Read data:" + line);
			if (line.startsWith("*")) {
				break;
			}
			listLine.add(line);
		}
		this.lines = listLine.toArray(new String[listLine.size()]);
	}

	private String number;
	private String data;
	private String[] lines;

	private final void loadData() {
		StringBuilder builder = new StringBuilder();
//		String[] headerParams = this.header.getParams();
//		for (int smsIndex = 0; smsIndex < headerParams.length; smsIndex++) {
//			builder.append(" ").append(headerParams[smsIndex]);
//		}
		
//		for (int index = 0; index < this.lines.length; index++) {
//			String line = this.lines[index];
//			builder.append(" ").append(line);
//		}
		builder.append(String.join(" ", this.header.getParams()));
		builder.append(" ");
		builder.append(String.join(" ", this.lines));
		this.data = builder.toString().trim();
	}
}
