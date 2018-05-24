package com.bkav.FLDIGIForwardService.pack;

import java.util.Arrays;

/***
 * Header of <i>FLDIGIPackage</i> contain macaddress, type, and params.
 */
public class FLDIGIHeader {

	/***
	 * Parse FLDIGIHeader from string
	 * @return FLDIGIHeader parsed of null if input invalid format.
	 */
	public static FLDIGIHeader parser(String inputHeader) {
		if (inputHeader == null || inputHeader.isEmpty()) {
			return null;
		}
		try {
			String header = inputHeader.substring(inputHeader.indexOf('#'));
			String[] headers = header.split(" ");
			return parser(headers);
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