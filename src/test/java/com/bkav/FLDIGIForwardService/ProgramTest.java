package com.bkav.FLDIGIForwardService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class ProgramTest extends TestCase {

	public void testMain() {
		String patternString = "(.*)#([\\d|:]+)\\s(\\S+)\\s(.*)";
		patternString = "(.*)(#\\w{3})\\s(\\S+)\\s(.*)";
		Pattern pattern = Pattern.compile(patternString);
		String testString = "& e                &          yiÁn\"  n  gou ¸z )   ÷****#200 call 0123456789";
		testString = "& e                &          yiÁn\"  n  gou ¸z )   ÷****#200 call 0123456789 line1 line2 line3";
		testString = "e                          eo :iX -( ****#200 sms 0123456789 line1";
//		testString = "ads#123";
		Matcher matcher = pattern.matcher(testString);
		if (matcher.find()) {
			System.out.println("FOUND");
			for (int index = 1; index <= matcher.groupCount(); index++) {
				System.out.println(matcher.group(index));
			}
		} else {
			System.out.println("NOT_FOUND");
		}
		assertTrue(true);//TODO test Main.
	}

}
