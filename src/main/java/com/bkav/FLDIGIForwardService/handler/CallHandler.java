package com.bkav.FLDIGIForwardService.handler;

import java.io.FileOutputStream;
import java.io.PrintWriter;

import com.bkav.FLDIGIForwardService.Config;
import com.bkav.FLDIGIForwardService.SystemManager;
import com.bkav.FLDIGIForwardService.pack.CallFLDIGIPackage;
import com.bkav.FLDIGIForwardService.pack.FLDIGIPackage;

public class CallHandler implements FLDIGIHandler {

	@Override
	public void process(FLDIGIPackage handlerPack) throws Exception {
		if (!(handlerPack instanceof CallFLDIGIPackage)) {
			return;
		}
		CallFLDIGIPackage pack = (CallFLDIGIPackage)handlerPack;
		if ("end".equals(pack.number)) {
			return;
		}
		String output = String.format(
				"<commands>\n\t<command type=\"1\"> <!--sms-->\n\t<data number=\"%s\" message=\"\"/>\n\t</command>\n" + "</commands>",
				pack.number);
		PrintWriter outputWriter = new PrintWriter(new FileOutputStream("sos.xml", false));
		outputWriter.print(output);
		outputWriter.flush();
		outputWriter.close();
		Process pushFileProcess = SystemManager.runCommand.exec(Config.ADB_PATH + " push sos.xml /storage/emulated/0/SOS/");
		try {
			pushFileProcess.waitFor();
		} catch (InterruptedException e) {
		}
		Process startHandlerProcess = SystemManager.runCommand.exec(
				Config.ADB_PATH + " shell am start -n com.bkav.mainsos/com.bkav.mainsos.SOSActivity");
		try {
			startHandlerProcess.waitFor();
		} catch (InterruptedException e) {
		}
		SystemManager.logger.info("Call Command");
	}

}
