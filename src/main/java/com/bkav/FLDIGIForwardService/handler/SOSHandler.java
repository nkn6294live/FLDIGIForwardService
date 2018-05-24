package com.bkav.FLDIGIForwardService.handler;

import com.bkav.FLDIGIForwardService.Config;
import com.bkav.FLDIGIForwardService.SystemManager;
import com.bkav.FLDIGIForwardService.pack.FLDIGIPackage;
import com.bkav.FLDIGIForwardService.pack.SOSFLDIGIPackage;

public class SOSHandler implements FLDIGIHandler {

	@Override
	public void process(FLDIGIPackage handlerPack) throws Exception {
		if (!(handlerPack instanceof SOSFLDIGIPackage)) {
			return;
		}
		SOSFLDIGIPackage pack = (SOSFLDIGIPackage) handlerPack;
		if ("end".equals(pack.number())) {
			return;
		}
		String output = String.format(
				"<commands>\n\t<command type=\"1\"> <!--sms-->\n\t<data number=\"%s\" message=\"\"/>\n\t</command>\n</commands>",
				Config.SOS_SAMPLE_PHONE_NUMBER_HANDLER);
		HandlerUtils.pushFile(output, "sos.xml", "/storage/emulated/0/SOS/");
		HandlerUtils.execCommand(Config.ADB_PATH ,"shell am start -n com.bkav.mainsos/com.bkav.mainsos.SOSActivity");
		SystemManager.logger.info("SOS Command");
		SystemManager.logger.info(output);
	}

}
