package com.bkav.FLDIGIForwardService.handler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import com.bkav.FLDIGIForwardService.Config;
import com.bkav.FLDIGIForwardService.SystemManager;

public class HandlerUtils {
	
	/***
	 * Run command from string.
	 * @param execString
	 * @return result after run command or -1 if error other: invalid input, interrupt thread of run.
	 * @throws IOException run error.
	 */
	public static int execCommand(String execString) throws IOException {
		if (execString == null || execString.isEmpty()) {
			return -1;
		}
		Process handlerProcess = SystemManager.runCommand.exec(execString);
		try {
			return handlerProcess.waitFor();
		} catch (InterruptedException e) {
			return -1;
		}
	}
	/***
	 * Run command from execFile and param.
	 * @param execString
	 * @return result after run command or -1 if error other: invalid input, interrupt thread of run.
	 * @throws IOException run error.
	 */
	public static int execCommand(String execPath, String params) throws IOException {
		String execString = String.format("%s %s", execPath, params);
		return execCommand(execString);
	}
	/***
	 * Write content to localFilePath and push to remoteDestFolde by ADB.
	 * @param content content need push.
	 * @param localFilePath
	 * @param remoteDestFolder
	 * @return result after run push file.
	 * @throws IOException error exec.
	 */
	public static int pushFile(String content, String localFilePath, String remoteDestFolder) throws IOException {
		PrintWriter outputWriter = new PrintWriter(new FileOutputStream(localFilePath, false));
		outputWriter.print(content);
		outputWriter.flush();
		outputWriter.close();
		String execString = String.format("%s push %s %s", Config.ADB_PATH, localFilePath, remoteDestFolder);///opt/Android/Sdk/platform-tools/adb + " push sos.xml /storage/emulated/0/SOS/"
		return execCommand(execString);
	}
	
}
