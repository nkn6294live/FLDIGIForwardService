package com.bkav.FLDIGIForwardService.handler;

import com.bkav.FLDIGIForwardService.pack.FLDIGIPackage;
import com.bkav.FLDIGIForwardService.pack.GPSFLDIGIPackage;

public class GPSHandler implements FLDIGIHandler {

	@Override
	public void process(FLDIGIPackage handlerPack) throws Exception {
		if (!(handlerPack instanceof GPSFLDIGIPackage)) {
			return;
		}
		// GPSFLDIGIPackage pack = (GPSFLDIGIPackage)handlerPack;
		// TODO GPSHandler.
	}

}
