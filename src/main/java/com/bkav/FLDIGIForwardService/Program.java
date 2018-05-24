package com.bkav.FLDIGIForwardService;

import java.io.InputStreamReader;
import java.io.Reader;

import com.bkav.FLDIGIForwardService.handler.HandlerFactory;
import com.bkav.FLDIGIForwardService.pack.FLDIGIPackage;

//{"macaddress":"02:00:00:00:00:00","lat":15.480129811912775,"lng":109.2829299904406};

public class Program {
	public static void main(String[] args) {
		java.net.Socket socketFldigi = null;
		while (true) {
			try {
				socketFldigi = new java.net.Socket(Config.FLDIGI_HOST, Config.FLDIGI_PORT);
				Reader reader = new InputStreamReader(socketFldigi.getInputStream());
				SystemManager.logger.info("Start....");
				while (true) {
					try {
						FLDIGIPackage pack = FLDIGIPackage.createPackage(reader);
						HandlerFactory.handlerPackage(pack);
						SocketIOService.send(pack);						 
					} catch (Exception ex) {
						try {
							Thread.sleep(100);							
						} catch (InterruptedException e) {}
					}
				}
			} catch (Exception ex) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
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
