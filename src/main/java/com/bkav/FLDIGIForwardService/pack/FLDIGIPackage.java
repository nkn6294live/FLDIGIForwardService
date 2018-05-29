package com.bkav.FLDIGIForwardService.pack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.security.InvalidParameterException;

import org.json.JSONException;
import org.json.JSONObject;

import com.bkav.FLDIGIForwardService.SystemManager;

public class FLDIGIPackage {

	public final static String SMS_TYPE = "sms";
	public final static String GPS_TYPE = "gps";
	public final static String CALL_TYPE = "call";
	public final static String SOS_TYPE = "sos";

	public static FLDIGIPackage createPackage(String string) throws IOException {
		return createPackage(new StringReader(string));
	}
	/***
	 * Parse FLDIGIPackage from reader.
	 * @param reader input reader
	 * @return FLDIGIPacage parsed or null if input reader data invalid format or end reader.
	 * @throws IOException read in reader error
	 */
	public static FLDIGIPackage createPackage(Reader reader) throws IOException {
		BufferedReader bufferReader = null;
		if (reader instanceof BufferedReader) {
			bufferReader = (BufferedReader)reader;
		} else {
			bufferReader = new BufferedReader(reader);
		}
		SystemManager.logger.info("READING HEADER...");
		String header = bufferReader.readLine();
		SystemManager.logger.info("HEADER:" + header);
		if (header == null) {
			throw new IOException();
		}
		FLDIGIHeader fldigiHeader = FLDIGIHeader.parser(header);
		if (fldigiHeader == null) {
			SystemManager.logger.info("HEADER_PARSED: NULL");
			return null;
		}
		SystemManager.logger.info("HEADER_PARSED: " + fldigiHeader.toString());
		switch (fldigiHeader.getType()) {
			case SMS_TYPE:
				return new SMSFLDIGIPackage(fldigiHeader, bufferReader);
			case CALL_TYPE:
				return new CallFLDIGIPackage(fldigiHeader);
			case GPS_TYPE:
				return new GPSFLDIGIPackage(fldigiHeader);
			case SOS_TYPE:
				return new SOSFLDIGIPackage(fldigiHeader);
			default:
				return new FLDIGIPackage(fldigiHeader);
		}
	}
	
	/***
	 * Create FLDIGIPackage from <i>FLDIGIHeader</i> without reader.
	 * @param header header of <i>FLDIGIPackage</i>.
	 * @throws <i>InvalidParameterException</i> if header null.
	 */
	protected FLDIGIPackage(FLDIGIHeader header) {
		if (header == null) {
			throw new InvalidParameterException("Header can not null");
		}
		this.exportObject = new JSONObject();
		this.header = header;
		this.updateData();
	}
	/***
	 * Create FLDIGIPackage from <i>FLDIGIHeader</i> and reader.
	 * @param header header of <i>FLDIGIPackage</i>.
	 * @param reader reader import.
	 * @throws <i>IOException</i> if read from reader error or <i>InvalidParameterException</i> if header null.
	 */
	protected FLDIGIPackage(FLDIGIHeader header, Reader reader) throws IOException {
		if (header == null) {
			throw new InvalidParameterException("Header can not null");
		}
		this.exportObject = new JSONObject();
		this.header = header;
		this.loadDataFromReader(reader);
		this.updateData();
	}
	/***
	 * Export data in package to JSONObject.
	 * @return JSONObject that exported this params.
	 * @throws JSONException if export to JSONObject error.
	 */
	public JSONObject exportData() throws JSONException {
		this.exportObject = new JSONObject();
		this.exportObject.put("macaddress", this.header.getMacAddress());
		this.exportObject.put("type", this.header.getType());
		return this.exportObject;
	}
	/***
	 * Get Header of package 
	 */
	public FLDIGIHeader getHeader() {
		return this.header;
	}
	
	@Override
	public String toString() {
		return String.format("%s [header=%s]", this.getClass().getSimpleName(), this.header.toString());
	}

	protected FLDIGIHeader header;
	protected JSONObject exportObject;

	/***
	 * Update param from data loaded header.
	 */
	protected void updateData() {
	}
	/***
	 * Load data from input reader.
	 * @throws IOException Occur error in read in reader.
	 */
	protected void loadDataFromReader(Reader reader) throws IOException {
	}
}
