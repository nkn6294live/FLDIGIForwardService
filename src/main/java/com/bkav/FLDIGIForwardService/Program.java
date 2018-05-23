package com.bkav.FLDIGIForwardService;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ConnectException;

import com.bkav.FLDIGIForwardService.pack.FLDIGIPackage;

//{"macaddress":"02:00:00:00:00:00","lat":15.480129811912775,"lng":109.2829299904406};

public class Program {
	public static void main(String[] args) throws Exception {
		java.net.Socket socketFldigi = null;
		while (true) {
			try {
				socketFldigi = new java.net.Socket(Config.FLDIGI_HOST, Config.FLDIGI_PORT);
				Reader reader = new InputStreamReader(socketFldigi.getInputStream());
				SystemManager.logger.info("Start....");
				while (true) {
					FLDIGIPackage pack = FLDIGIPackage.parse(reader);
					SystemManager.logger.info("Command:");
					Handler.handlerPackage(pack);
					SocketIOService.send(pack);
				}
			} catch (ConnectException ex) {
				Thread.sleep(1000);
				SystemManager.logger.info("Wait Connect....");
				continue;
			} finally {
				if (socketFldigi != null) {
					try {
						socketFldigi.close();
					} catch (Exception ex) {
					}
				}
			}
		}
	}
}
