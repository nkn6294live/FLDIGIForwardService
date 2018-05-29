package com.bkav.FLDIGIForwardService.pack;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/***
 * Header of <i>FLDIGIPackage</i> contain macaddress, type, and params.
 */
public class FLDIGIHeader {

	public final static String regexStringPatter = "(.*)(#\\w{3})\\s(\\S+)\\s(.*)";
	public static Pattern headerPatter = Pattern.compile(regexStringPatter);
	/***
	 * Parse FLDIGIHeader from string
	 * @return FLDIGIHeader parsed of null if input invalid format.
	 */
	public static FLDIGIHeader parser(String inputHeader) {
		if (inputHeader == null || inputHeader.isEmpty()) {
			return null;
		}
		try {
			Matcher match = headerPatter.matcher(inputHeader);
			if (!match.find()) {
				return null;
			}
			if (match.groupCount() < 4) {
				return null;
			}
			String macaddress = match.group(2);
			String type = match.group(3);
			String[] params = match.group(4).split("\\s");
			return new FLDIGIHeader(macaddress, type, params);
		} catch (Exception ex) {
			return null;
		}
	}

	/***
	 * Parse FLDIGIHeader from string arrays
	 * @return FLDIGIHeader parsed of null if input invalid format.
	 */
	public static FLDIGIHeader parser(String[] headers) {
		if (headers == null || headers.length < 2) {
			return null;
		}
		String inputMac = headers[0];
		String macaddress = inputMac.substring(inputMac.indexOf('#'));
		String type = headers[1];
		String[] params = new String[headers.length - 2];
		for (int index = 2; index < headers.length; index++) {
			params[index - 2] = headers[index];
		}
		return new FLDIGIHeader(macaddress, type, params);
	}

	public FLDIGIHeader(String macaddress, String type) {
		this(macaddress, type, new String[] {});
	}

	public FLDIGIHeader() {
		this(null, null, new String[] {});
	}
	
	public String getMacAddress() {
		return this.macaddress;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String[] getParams() {
		return this.params;
	}
	/***
	 * Get param at index
	 * @param index need get value.
	 * @return value at index or null if index invalid.
	 */
	public String getParam(int index) {
		if (index < 0 || index >= this.params.length ) {
			return null;
		}
		return this.params[index];
	}
	
	@Override
	public String toString() {
		return String.format("%s [macaddress=%s, type=%s, params=%s]", 
				this.getClass().getSimpleName(), this.macaddress, this.type, 
				Arrays.toString(this.params));
	}

	private String macaddress;
	private String type;
	private String[] params;
	private FLDIGIHeader(String macaddress, String type, String[] params) {
		this.macaddress = macaddress;
		this.type = type;
		this.params = params;
	}
}