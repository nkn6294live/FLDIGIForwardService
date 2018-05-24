package com.bkav.FLDIGIForwardService.handler;

import com.bkav.FLDIGIForwardService.Config;
import com.bkav.FLDIGIForwardService.SystemManager;
import com.bkav.FLDIGIForwardService.pack.FLDIGIPackage;
import com.bkav.FLDIGIForwardService.pack.SMSFLDIGIPackage;

public class SMSHandler implements FLDIGIHandler {

	@Override
	public void process(FLDIGIPackage handlerPack) throws Exception {
		if (!(handlerPack instanceof SMSFLDIGIPackage)) {
			return;
		}
		SMSFLDIGIPackage pack = (SMSFLDIGIPackage)handlerPack;
		String message = pack.getHeader().getMacAddress() + " " + pack.data();
		String output = String.format(
				"<commands>\n\t<command type=\"0\"> <!--sms-->\n\t<data number=\"%s\" message=\"%s\"/>\n\t</command>\n</commands>",
				pack.number(), message);
		HandlerUtils.pushFile(output, "sos.xml", "/storage/emulated/0/SOS/");		
		HandlerUtils.execCommand(
				Config.ADB_PATH, "shell am start -n com.bkav.mainsos/com.bkav.mainsos.SOSActivity");
		SystemManager.logger.info("SMS Command");
		SystemManager.logger.info(output);
	}

}
