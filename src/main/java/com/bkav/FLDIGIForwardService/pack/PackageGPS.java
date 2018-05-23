package com.bkav.FLDIGIForwardService.pack;

public class PackageGPS extends PacketData {
	public PackageGPS(String raw) {
		super(raw);
	}
	
	public String getLat() {
		return super.raws[1];
	}
	
	public String getLng() {
		return super.raws[2];
	}
}
