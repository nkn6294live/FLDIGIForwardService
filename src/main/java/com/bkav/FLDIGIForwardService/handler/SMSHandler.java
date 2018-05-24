package com.bkav.FLDIGIForwardService.handler;

import java.io.FileOutputStream;
import java.io.PrintWriter;

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
		String outputWriter = String.format(
				"<commands>\n\t<command type=\"0\"> <!--sms-->\n\t<data number=\"%s\" message=\"%s\"/>\n\t</command>\n</commands>",
				pack.number(), message);
		SystemManager.logger.info(outputWriter);
		PrintWriter out = new PrintWriter(new FileOutputStream("sos.xml", false));
		out.print(outputWriter);
		out.flush();
		out.close();
		Process pushFileProcess = SystemManager.runCommand.exec(Config.ADB_PATH + " push sos.xml /storage/emulated/0/SOS/");
		try {
			pushFileProcess.waitFor();
		} catch (InterruptedException e) {
		}
		Process handlerProcess = SystemManager.runCommand.exec(
				Config.ADB_PATH + " shell am start -n com.bkav.mainsos/com.bkav.mainsos.SOSActivity");
		try {
			handlerProcess.waitFor();
		} catch (InterruptedException e) {
		}
		SystemManager.logger.info("SMS Command");
	}

}
