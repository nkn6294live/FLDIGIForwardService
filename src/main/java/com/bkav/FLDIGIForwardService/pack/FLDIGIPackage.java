package com.bkav.FLDIGIForwardService.pack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.bkav.FLDIGIForwardService.SystemManager;

public class FLDIGIPackage {

	public final static String SMS_TYPE = "sms";
	public final static String GPS_TYPE = "gps";
	public final static String CALL_TYPE = "call";
	public final static String SOS_TYPE = "sos";

	public static FLDIGIPackage parse(String string) throws Exception {
		return parse(new StringReader(string));
	}

	public static FLDIGIPackage parse(Reader reader) throws IOException {
		BufferedReader bufferReader = new BufferedReader(reader);
		List<String> listLine = new ArrayList<>();
		String line;
		while ((line = bufferReader.readLine()) != null) {
			SystemManager.logger.info("Read data:" + line);
			if (line.startsWith("*")) {
				break;
			}
			listLine.add(line);
			line = bufferReader.readLine();
		}
		String[] lines = listLine.toArray(new String[listLine.size()]);
		if (lines.length == 0) {
			return new FLDIGIPackage(null, new String[] {}, lines);
		}
		String header = lines[0];
		int indexDelimeter = header.indexOf('#');
		header = header.substring(indexDelimeter);
		String[] headers = header.split(" ");
		String mac = headers[0];
		indexDelimeter = mac.indexOf('#');
		String macaddress = mac.substring(indexDelimeter);
		String type = headers[1];
		switch (type) {
		case SMS_TYPE:
			return new SMSFLDIGIPackage(macaddress, headers, lines);
		case CALL_TYPE:
			return new CallFLDIGIPackage(macaddress, headers, lines);
		case GPS_TYPE:
			return new GPSFLDIGIPackage(macaddress, headers, lines);
		case SOS_TYPE:
			return new SOSFLDIGIPackage(macaddress, headers, lines);
		default:
			return new FLDIGIPackage();
		}
	}

	public String macaddress;
	public String type;
	public JSONObject exportObject;
	public String key;

	protected FLDIGIPackage() {
		this(null, new String[] {}, new String[] {});
	}

	protected FLDIGIPackage(String macaddress, String[] headers, String[] lines) {
		this.exportObject = new JSONObject();
		this.macaddress = macaddress;
		this.headers = headers;
		this.lines = lines;
		this.updateData();
	}

	public JSONObject exportData(Writer writer) throws Exception {
		this.exportObject = new JSONObject();
		this.exportObject.put("macaddress", this.macaddress);
		this.exportObject.put("type", this.type);
		return this.exportObject;
	}

	protected String[] lines;
	protected String[] headers;

	protected void updateData() {
	}

	protected void validatePhoneNumber(String number) {
		if (number == null) {
			throw new RuntimeException();
		}
	}
}
