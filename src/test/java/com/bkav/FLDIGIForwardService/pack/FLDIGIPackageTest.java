package com.bkav.FLDIGIForwardService.pack;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import org.json.JSONException;

import com.bkav.FLDIGIForwardService.SampleData;
import com.bkav.FLDIGIForwardService.SystemManager;

import junit.framework.TestCase;

public class FLDIGIPackageTest extends TestCase {

	public void testCreatePackageString() {
		long count = Arrays.stream(SampleData.Sample_Package)
		.peek(SystemManager.logger::info)
		.map(reader -> {
			try {
				return FLDIGIPackage.createPackage(reader);
			} catch (IOException e) {
				return null;
			}
		})
		.filter(pack -> pack != null)
		.peek(pack -> {
			SystemManager.logger.info(pack.toString());
		})
		.map(pack -> {
			try {
				return pack.exportData();
			} catch (JSONException e) {
				return null;
			}
		})
		.filter(json -> json != null)
		.map(Object::toString)
		.peek(SystemManager.logger::info)
		.count();
	assertEquals(SampleData.Sample_Package.length, count);
	}
	
	public void testExportData() {
		assertTrue(true);//TODO testExportData
	}
	public void testCreatePackageReader() {
		long count = Arrays.stream(SampleData.Sample_Package)
			.peek(SystemManager.logger::info)
			.map(StringReader::new)
			.map(reader -> {
				try {
					return FLDIGIPackage.createPackage(reader);
				} catch (IOException e) {
					return null;
				}
			})
			.peek(pack -> {
				SystemManager.logger.info(pack.toString());
			})
			.map(pack -> {
				try {
					return pack.exportData();
				} catch (JSONException e) {
					return null;
				}
			})
			.filter(json -> json != null)
			.map(Object::toString)
			.peek(SystemManager.logger::info)
			.count();
		assertEquals(SampleData.Sample_Package.length, count);
	}
}
