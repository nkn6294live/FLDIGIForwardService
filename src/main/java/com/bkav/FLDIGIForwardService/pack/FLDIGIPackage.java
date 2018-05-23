package com.bkav.FLDIGIForwardService.pack;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.bkav.FLDIGIForwardService.Config;
import com.bkav.FLDIGIForwardService.SystemManager;

public class FLDIGIPackage {

	public final static String SMS_TYPE = "sms";
	public final static String GPS_TYPE = "gps";
	public final static String CALL_TYPE = "call";
	public final static String SOS_TYPE = "sos";
	
	public static FLDIGIPackage parse(String string) throws Exception {
		return parse(new StringReader(string));
	}

	public static FLDIGIPackage parse(Reader reader) throws Exception {
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
		switch(type) {
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

	public static class GPSFLDIGIPackage extends FLDIGIPackage {
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

	public static class CallFLDIGIPackage extends FLDIGIPackage {
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

	public static class SOSFLDIGIPackage extends FLDIGIPackage {
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

	public static class SMSFLDIGIPackage extends FLDIGIPackage {
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
				String line = this.lines[index];//line.split(" ");
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

	public FLDIGIPackage importData(Reader reader) throws Exception {
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
		this.lines = listLine.toArray(new String[listLine.size()]);
		if (this.lines.length == 0) {
			return this;
		}
		String header = this.lines[0];
		int indexDelimeter = header.indexOf('#');
		header = header.substring(indexDelimeter);
		this.headers = header.split(" ");
		String mac = this.headers[0];
		indexDelimeter = mac.indexOf('#');
		this.macaddress = mac.substring(indexDelimeter);
		this.type = this.headers[1];
		this.updateData();
		return this;
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
