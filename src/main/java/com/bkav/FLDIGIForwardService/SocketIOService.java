package com.bkav.FLDIGIForwardService;

import java.net.URISyntaxException;

import org.json.JSONObject;

import com.bkav.FLDIGIForwardService.pack.FLDIGIPackage;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketIOService {
	public static Socket socket;
	
	static {
		try {
			socket = IO.socket(Config.SOCKET_IO_URL);
		} catch (URISyntaxException e) {
			SystemManager.logger.info(e.getMessage());
		}
	}

	public static void send(FLDIGIPackage pack) throws Exception {
		socket.connect();
		JSONObject json = pack.exportObject;
		String key = pack.key;
		if (socket.connected()) {
			socket.emit(key, json);
			SystemManager.logger.info("Send:" + json);
		} else {
			socket.connect();
			SystemManager.logger.info("Reconnect:" + json.toString());
		}
	}
	
	public static void testSend() throws Exception {
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
}
