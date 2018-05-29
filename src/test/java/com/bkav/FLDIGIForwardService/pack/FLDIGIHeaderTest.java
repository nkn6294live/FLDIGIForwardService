package com.bkav.FLDIGIForwardService.pack;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;

import com.bkav.FLDIGIForwardService.SampleData;
import com.bkav.FLDIGIForwardService.SystemManager;

import junit.framework.TestCase;

public class FLDIGIHeaderTest extends TestCase {

	public void testParserString() {
		String inputs = "";
		FLDIGIHeader header = FLDIGIHeader.parser(inputs);
		assertNull(header);
		inputs = "**#mac_1 type_1";
		header = FLDIGIHeader.parser(inputs);
		assertNotNull(header);
		assertEquals("#mac_1", header.getMacAddress());
		assertEquals("type_1", header.getType());
		assertEquals(0, header.getParams().length);
		
		inputs = "**#mac_2 type_2 param_0 param_1 param_2";
		header = FLDIGIHeader.parser(inputs);
		assertNotNull(header);
		assertEquals("#mac_2", header.getMacAddress());
		assertEquals("type_2", header.getType());
		assertEquals(3, header.getParams().length);

		assertArrayEquals(new String[] {"param_0", "param_1", "param_2"}, header.getParams());
		
		assertEquals("param_0", header.getParam(0));
		assertEquals("param_1", header.getParam(1));
		assertEquals("param_2", header.getParam(2));
		
		inputs = "& e                &          yiÁn\\\"  n  gou ¸z )   ÷****#200 call 0123456789 line1 line2 line3";
		header = FLDIGIHeader.parser(inputs);
		assertNotNull(header);
		assertEquals("200", header.getMacAddress());
		assertEquals("call", header.getType());
		assertEquals(4, header.getParams().length);
		assertTrue(true);
	}

	public void testParserStringArray() {
		String[] inputs = new String[] {};
		FLDIGIHeader header = FLDIGIHeader.parser(inputs);
		assertNull(header);
		inputs = new String[] {"**#mac_1", "type_1"};
		header = FLDIGIHeader.parser(inputs);
		assertNotNull(header);
		assertEquals("#mac_1", header.getMacAddress());
		assertEquals("type_1", header.getType());
		assertEquals(0, header.getParams().length);
		
		inputs = new String[] {"**#mac_2", "type_2", "param_0", "param_1", "param_2"};
		header = FLDIGIHeader.parser(inputs);
		assertNotNull(header);
		assertEquals("#mac_2", header.getMacAddress());
		assertEquals("type_2", header.getType());
		assertEquals(3, header.getParams().length);
		
		assertArrayEquals(new String[] {"param_0", "param_1", "param_2"}, header.getParams());
		
		assertEquals("param_0", header.getParam(0));
		assertEquals("param_1", header.getParam(1));
		assertEquals("param_2", header.getParam(2));
		assertTrue(true);
	}
	
	public void testParse() {
		Arrays.stream(SampleData.Sample_Header)
			.peek(SystemManager.logger::info)
			.map(FLDIGIHeader::parser)
			.map(Object::toString)
			.forEach(SystemManager.logger::info);
		assertTrue(true);
	}

}
