package com.bkav.FLDIGIForwardService;

import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;

import com.bkav.FLDIGIForwardService.pack.FLDIGIPackage;

public class SocketIOService {

	static {
		try {
			socket = IO.socket(Config.SOCKET_IO_URL);
		} catch (URISyntaxException e) {
			SystemManager.logger.info(e.getMessage());
		}
	}

	public static String getKeyFromPackageType(FLDIGIPackage pack) {
		switch (pack.getHeader().getType()) {
			case FLDIGIPackage.CALL_TYPE:
				return "call";
			case FLDIGIPackage.GPS_TYPE:
				return "sendata";
			case FLDIGIPackage.SMS_TYPE:
				return "sms";
			case FLDIGIPackage.SOS_TYPE:
				return "sos";
			default:
				return "";
		}
	}

	public static void send(FLDIGIPackage pack) throws Exception {
		JSONObject json = pack.exportData();
		String key = getKeyFromPackageType(pack);
		if (!socket.connected()) {
			socket.connect();
			SystemManager.logger.info("Connect to Socket.IO service:" + json.toString());
		}
		socket.emit(key, json);
		SystemManager.logger.info("Send to Socket.IO service:" + json);
	}

	public static void testSend() throws JSONException {
		int count = 0;
		JSONObject json;
		String key;
		if (count++ >= 3) {
			json = new JSONObject();
			json.put("macaddress", Config.SAMPLE_SOCKET_IO_SERVICE_MAC);
			json.put("type", "call");
			json.put("number", Config.SAMPLE_SOCKET_IO_SERVICE_NUMBER);
			key = "call";
			SystemManager.logger.info("Send:" + json);
			socket.emit(key, json);

			json = new JSONObject();
			json.put("macaddress", Config.SAMPLE_SOCKET_IO_SERVICE_MAC);
			json.put("type", "call");
			json.put("number", "end");
			key = "call";
			SystemManager.logger.info("Send:" + json);
			socket.emit(key, json);

			json = new JSONObject();
			json.put("macaddress", Config.SAMPLE_SOCKET_IO_SERVICE_MAC);
			json.put("type", "sos");
			json.put("number", Config.SAMPLE_SOCKET_IO_SERVICE_NUMBER);
			key = "sos";
			SystemManager.logger.info("Send:" + json);
			socket.emit(key, json);
			count = 0;
		}
	}

	private static Socket socket;
}
