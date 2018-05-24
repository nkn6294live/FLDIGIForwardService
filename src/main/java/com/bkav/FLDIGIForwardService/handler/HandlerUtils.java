package com.bkav.FLDIGIForwardService.handler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import com.bkav.FLDIGIForwardService.Config;
import com.bkav.FLDIGIForwardService.SystemManager;

public class HandlerUtils {
	
	public static boolean execCommand(String execString) throws IOException {
		Process handlerProcess = SystemManager.runCommand.exec(execString);
		try {
			handlerProcess.waitFor();
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}
	
	public static boolean execCommand(String execPath, String params) throws IOException {
		String execString = String.format("%s %s", execPath, params);
		return execCommand(execString);
	}
	
	public static boolean pushFile(String content, String filePath, String destFolder) throws IOException {
		PrintWriter outputWriter = new PrintWriter(new FileOutputStream(filePath, false));
		outputWriter.print(content);
		outputWriter.flush();
		outputWriter.close();
		String execString = String.format("%s push %s %s", Config.ADB_PATH, filePath, destFolder);///opt/Android/Sdk/platform-tools/adb + " push sos.xml /storage/emulated/0/SOS/"
		return execCommand(execString);
	}
	
}
