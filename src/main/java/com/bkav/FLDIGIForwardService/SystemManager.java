package com.bkav.FLDIGIForwardService;

import java.util.logging.Logger;

public class SystemManager {
	
	public final static Logger logger = Logger.getLogger(SystemManager.class.getSimpleName());
	public final static Runtime runCommand;
	static {
		runCommand = Runtime.getRuntime();
	}
//	class Logger {
//		public void info(String message) {
//			System.out.println(message);
//		}
//	}
}
