package com.bkav.FLDIGIForwardService;

//import java.util.logging.Logger;

public class SystemManager {
	
	public final static Logger logger;
	public final static Runtime runCommand;
	static {
		runCommand = Runtime.getRuntime();
		logger = Logger.getLogger(SystemManager.class.getSimpleName());
	}
	public static class Logger {
		public static Logger getLogger(String prefix) {
			return new Logger(prefix);
		}
		
		public void info(String message) {
			System.out.println(String.format("%s>%s", this.prefix, message));
		}
		
		public void warning(String message) {
			System.err.println(String.format("%s>%s", this.prefix, message));
		}
		
		protected Logger(String prefix) {
			this.prefix = prefix;
		}
		private String prefix;
	}
}
