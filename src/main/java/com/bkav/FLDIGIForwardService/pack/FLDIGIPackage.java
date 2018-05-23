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
		return new FLDIGIPackage().importData(reader);
	}

	public String macaddress;
	public String type;
	public JSONObject exportObject;
	public String key;

	public class GPSFLDIGIPackage extends FLDIGIPackage {
		public double lat;
		public double lng;

		public GPSFLDIGIPackage() {
			super();
			this.type = GPS_TYPE;
			this.key = "sentdata";
			this.lat = Double.valueOf(this.headers[2]);
			this.lng = Double.valueOf(this.headers[3]);
		}

		@Override
		public JSONObject exportData(Writer writer) throws Exception {
			super.exportData(writer);
			this.exportObject.put("lat", new Double(this.lat));
			this.exportObject.put("lng", new Double(this.lng));
			return this.exportObject;
		}
	}

	public class CallFLDIGIPackage extends FLDIGIPackage {
		public String number;

		public CallFLDIGIPackage() {
			super();
			this.type = CALL_TYPE;
			this.key = "call";
			this.number = this.headers[2];
		}

		@Override
		public JSONObject exportData(Writer writer) throws Exception {
			super.exportData(writer);
			this.exportObject.put("number", this.number);
			return this.exportObject;
		}
	}

	public class SOSFLDIGIPackage extends FLDIGIPackage {
		public String number;

		public SOSFLDIGIPackage() {
			super();
			this.type = SOS_TYPE;
			this.key = "sos";
			this.number = Config.SOS_SAMPLE_PHONE_NUMBER;
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

	}

	public class SMSFLDIGIPackage extends FLDIGIPackage {
		public String number;
		public String data;

		public SMSFLDIGIPackage() {
			super();
			this.type = SMS_TYPE;
			this.key = "sms";
			this.number = this.headers[2];
			this.loadData();
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
	}

	public FLDIGIPackage() {
		this.exportObject = new JSONObject();
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

	protected void validatePhoneNumber(String number) {
		if (number == null) {
			throw new RuntimeException();
		}
	}
}
