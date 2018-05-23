package com.bkav.FLDIGIForwardService.pack;

public class PacketData {
	public PacketData(String raw) {
		this.raws = raw.split(" ");
	}	
	public String getType() {
		return this.raws[0];
	}
	
	protected String[] raws;
}
