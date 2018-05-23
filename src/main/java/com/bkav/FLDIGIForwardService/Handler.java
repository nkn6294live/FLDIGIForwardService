package com.bkav.FLDIGIForwardService;

import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.json.JSONObject;

import com.bkav.FLDIGIForwardService.pack.FLDIGIPackage;
import com.bkav.FLDIGIForwardService.pack.FLDIGIPackage.CallFLDIGIPackage;
import com.bkav.FLDIGIForwardService.pack.FLDIGIPackage.GPSFLDIGIPackage;
import com.bkav.FLDIGIForwardService.pack.FLDIGIPackage.SMSFLDIGIPackage;
import com.bkav.FLDIGIForwardService.pack.FLDIGIPackage.SOSFLDIGIPackage;

public class Handler {

	public static void handlerPackage(FLDIGIPackage pack) throws Exception {
		switch (pack.type) {
		case FLDIGIPackage.CALL_TYPE:
			CallHandler(pack);
			break;
		case FLDIGIPackage.GPS_TYPE:
			GPSHandler(pack);
			break;
		case FLDIGIPackage.SMS_TYPE:
			SMSHandler(pack);
			break;
		case FLDIGIPackage.SOS_TYPE:
			SOSHandler(pack);
			break;
		default:
			break;
		}
	}

	public static void SMSHandler(FLDIGIPackage handlerPack) throws Exception {
		if (!(handlerPack instanceof SMSFLDIGIPackage)) {
			return;
		}
		SMSFLDIGIPackage pack = (SMSFLDIGIPackage)handlerPack;
		String message = pack.macaddress + " " + pack.data;
		String outputWriter = String.format(
				"<commands>\n\t<command type=\"0\"> <!--sms-->\n\t<data number=\"%s\" message=\"%s\"/>\n\t</command>\n</commands>",
				pack.number, message);
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

	public static void CallHandler(FLDIGIPackage hanlderPack) throws Exception {
		if (!(hanlderPack instanceof CallFLDIGIPackage)) {
			return;
		}
		CallFLDIGIPackage pack = (CallFLDIGIPackage)hanlderPack;
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

	public static void SOSHandler(FLDIGIPackage handlerPack) throws Exception {
		if (!(handlerPack instanceof SOSFLDIGIPackage)) {
			return;
		}
		SOSFLDIGIPackage pack = (SOSFLDIGIPackage)handlerPack;
		JSONObject json = pack.exportObject;
		if (json.getString("number").equals("end")) {
			return;
		}
		String output = String.format(
				"<commands>\n\t<command type=\"1\"> <!--sms-->\n\t<data number=\"%s\" message=\"\"/>\n\t</command>\n</commands>",
				Config.SOS_SAMPLE_PHONE_NUMBER_HANDLER);
		PrintWriter outputWriter = new PrintWriter(new FileOutputStream("sos.xml", false));
		outputWriter.print(output);
		outputWriter.flush();
		outputWriter.close();
		Process pushFileProcess = SystemManager.runCommand.exec(Config.ADB_PATH + " push sos.xml /storage/emulated/0/SOS/");
		try {
			pushFileProcess.waitFor();
		} catch (InterruptedException e) {
		}
		Process handlerProcess = SystemManager.runCommand
				.exec(Config.ADB_PATH + " shell am start -n com.bkav.mainsos/com.bkav.mainsos.SOSActivity");
		try {
			handlerProcess.waitFor();
		} catch (InterruptedException e) {
		}
		SystemManager.logger.info("SOS Command");
	}

	public static void GPSHandler(FLDIGIPackage handlerPack) throws Exception {
		if (!(handlerPack instanceof GPSFLDIGIPackage)) {
			return;
		}
//		GPSFLDIGIPackage pack = (GPSFLDIGIPackage)handlerPack;
		//TODO GPSHandler.
	}
}
