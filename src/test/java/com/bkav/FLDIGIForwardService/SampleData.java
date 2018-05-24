package com.bkav.FLDIGIForwardService;

public class SampleData {
	public static String[] Sample_Header = new String[] {
			"#01:02:03:04:05:06 call 0123456789",
			"#01:02:03:04:05:06 gps 123.456 456.678",
			"#01:02:03:04:05:06 sms 0123456789",
			"#01:02:03:04:05:06 sos",
	};
	
	public static String[] Sample_Package = new String[] {
			"#01:02:03:04:05:06 call 0123456789",
			"#01:02:03:04:05:06 gps 123.456 456.678",
			"#01:02:03:04:05:06 sms 0123456789",
			"#01:02:03:04:05:06 sos",
			"#01:02:03:04:05:06 sms 0123456789\nThis is line_01\nThis is line 02\n***end",
			"#11:12:13:14:15:16 sms 0123456789\nThis is line_11\nThis is line 12\n***end"
	};
	
	public static String[] Sample_SMSPackage = new String[] {
			"#01:02:03:04:05:06 sms 0123456789\nThis is line_01\nThis is line 02\n***end",
			"#11:12:13:14:15:16 sms 0123456789\nThis is line_11\nThis is line 12\n***end"
	};
	
}
