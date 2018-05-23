package com.bkav.FLDIGIForwardService;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;

//{"macaddress":"02:00:00:00:00:00","lat":15.480129811912775,"lng":109.2829299904406};

public class MainProgram {
	public static void test(String[] args) throws URISyntaxException, UnknownHostException, IOException, JSONException, InterruptedException {
		while (true) {
			java.net.Socket socketFldigi;
			try {
				socketFldigi = new java.net.Socket("127.0.0.1", 7322);
			} catch (ConnectException ex) {
				Thread.sleep(1000);
				System.out.println("Wait Connect");
				continue;
			}
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socketFldigi.getInputStream()));
			Socket socket = IO.socket("http://192.168.1.171:4333");
			// Socket socket = IO.socket("http://127.0.0.1:3000");
			socket.connect();
			boolean flag = false;
			String key;
			JSONObject json = new JSONObject();
			System.out.println("Start");
			String mac;
			String sms = "";
			String number;
			final Runtime runCommand = Runtime.getRuntime();

			int count = 0;

			while (true) {
				String line = bufferedReader.readLine();
				if (line == null) {
					break;
				}
				int index = line.indexOf('#');
				try {
					line = line.substring(index);
				} catch (StringIndexOutOfBoundsException ex) {
					continue;
				}
				String[] tmp = line.split(" ");
				System.out.println("Read data " + line);
				if (tmp.length <= 0) {
					continue;
				}
				mac = tmp[0];
				index = mac.indexOf('#');
				try {
					mac = mac.substring(index);
				} catch (StringIndexOutOfBoundsException ex) {
					continue;
				}
				System.out.println("Mac " + tmp[0]);
				System.out.println("Mac " + line);
				switch (tmp[1]) {
				case "gps":
					double lat, lng;
					try {
						lat = Double.valueOf(tmp[2]);
						lng = Double.valueOf(tmp[3]);
					} catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
						continue;
					}
					json = new JSONObject();
					json.put("macaddress", mac);
					json.put("type", "gps");
					json.put("lat", new Double(lat));
					json.put("lng", new Double(lng));
					key = "sendata";
					break;
				case "call":
					json = new JSONObject();
					json.put("macaddress", mac);
					json.put("type", "call");
					json.put("number", tmp[2]);
					number = tmp[2];
					key = "call";
					break;
				case "sos":
					json = new JSONObject();
					json.put("macaddress", mac);
					json.put("type", "sos");
					json.put("number", "0973952263");
					key = "sos";
					break;
				case "sms":
					sms = "";
					json = new JSONObject();
					json.put("macaddress", mac);
					json.put("type", "sms");
					json.put("number", tmp[2]);
					number = tmp[2];
					for (int smsIndex = 3; smsIndex < tmp.length; smsIndex++) {
						sms += " " + tmp[smsIndex];
					}
					do {
						line = bufferedReader.readLine();
						line.split(" ");
						sms += " " + line;
					} while (!line.contains("***"));
					key = "sms";
					json.put("data", sms);
					break;
				default:
					continue;
				}

				System.out.println("Commanb");
				switch (json.getString("type")) {
				case "sms":
					PrintWriter out = new PrintWriter(new FileOutputStream("sos.xml", false));
					System.out.println(String.format("<commands>\n" + "    <command type=\"0\"> <!--sms-->\n"
							+ "        <data number=\"%s\" message=\"%s\"/>\n" + "    </command>\n" + "</commands>",
							json.getString("number"), json.get("macaddress") + " " + json.getString("data")));
					String message = json.get("macaddress") + " " + json.getString("data");
					System.out.println(message);
					out.print(String.format("<commands>\n" + "    <command type=\"0\"> <!--sms-->\n"
							+ "        <data number=\"%s\" message=\"Tau : %s\"/>\n" + "    </command>\n"
							+ "</commands>", json.getString("number"), message));
					out.flush();
					out.close();
					Process p = runCommand
							.exec("/opt/Android/Sdk/platform-tools/adb push sos.xml /storage/emulated/0/SOS/");
					try {
						p.waitFor();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					p = runCommand.exec(
							"/opt/Android/Sdk/platform-tools/adb shell am start -n com.bkav.mainsos/com.bkav.mainsos.SOSActivity");
					try {
						p.waitFor();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("SMS Comand");
					break;
				case "call":
					if (json.getString("number").equals("end")) {
						break;
					}
					PrintWriter out1 = new PrintWriter(new FileOutputStream("sos.xml", false));
					out1.print(String.format("<commands>\n" + "    <command type=\"1\"> <!--sms-->\n"
							+ "        <data number=\"%s\" message=\"\"/>\n" + "    </command>\n" + "</commands>",
							json.getString("number")));
					out1.flush();
					out1.close();
					Process p1 = runCommand
							.exec("/opt/Android/Sdk/platform-tools/adb push sos.xml /storage/emulated/0/SOS/");
					try {
						p1.waitFor();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					p1 = runCommand.exec(
							"/opt/Android/Sdk/platform-tools/adb shell am start -n com.bkav.mainsos/com.bkav.mainsos.SOSActivity");
					try {
						p1.waitFor();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Call Comand");
					break;
				case "sos":
					if (json.getString("number").equals("end")) {
						break;
					}
					PrintWriter out2 = new PrintWriter(new FileOutputStream("sos.xml", false));
					out2.print(String.format("<commands>\n" + "    <command type=\"1\"> <!--sms-->\n"
							+ "        <data number=\"%s\" message=\"\"/>\n" + "    </command>\n" + "</commands>",
							"+84981211231"));
					out2.flush();
					out2.close();
					Process p2 = runCommand
							.exec("/opt/Android/Sdk/platform-tools/adb push sos.xml /storage/emulated/0/SOS/");
					try {
						p2.waitFor();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					p1 = runCommand.exec(
							"/opt/Android/Sdk/platform-tools/adb shell am start -n com.bkav.mainsos/com.bkav.mainsos.SOSActivity");
					try {
						p1.waitFor();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Call Comand");
					break;
				default:
					break;
				}

				if (socket.connected()) {
					socket.emit(key, json);
					System.out.println("Send " + json);
					if (count++ >= 3) {
						json = new JSONObject();
						json.put("macaddress", "02:00:00:00:00:00");
						json.put("type", "call");
						json.put("number", "+84672634726342");
						key = "call";
						System.out.println("Send " + json);
						socket.emit(key, json);

						json = new JSONObject();
						json.put("macaddress", "02:00:00:00:00:00");
						json.put("type", "call");
						json.put("number", "end");
						key = "call";
						System.out.println("Send " + json);
						socket.emit(key, json);

						json = new JSONObject();
						json.put("macaddress", "02:00:00:00:00:00");
						json.put("type", "sos");
						json.put("number", "+84672634726342");
						key = "sos";
						System.out.println("Send " + json);
						socket.emit(key, json);

						count = 0;
					}
				} else {
					socket.connect();
					System.out.println("Reconnect " + json.toString());
				}
			}
		}
	}

	private static final float PREFIX = 10000000.0f;
}
