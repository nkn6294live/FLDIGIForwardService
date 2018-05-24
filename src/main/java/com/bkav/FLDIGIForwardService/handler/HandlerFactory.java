package com.bkav.FLDIGIForwardService.handler;

import com.bkav.FLDIGIForwardService.pack.FLDIGIPackage;

public class HandlerFactory {
	public static void handlerPackage(FLDIGIPackage pack) throws Exception  {
		FLDIGIHandler handler = null;
		switch (pack.getHeader().getType()) {
		case FLDIGIPackage.CALL_TYPE:
			handler = new CallHandler();
			break;
		case FLDIGIPackage.GPS_TYPE:
			handler = new GPSHandler();
			break;
		case FLDIGIPackage.SMS_TYPE:
			handler = new SMSHandler();
			break;
		case FLDIGIPackage.SOS_TYPE:
			handler = new SOSHandler();
			break;
		}
		if (handler != null) {
			handler.process(pack);
		}
	}
}
