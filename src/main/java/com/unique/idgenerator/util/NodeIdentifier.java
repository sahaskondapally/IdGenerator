package com.unique.idgenerator.util;

import java.net.NetworkInterface;
import java.util.Enumeration;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class NodeIdentifier {

	private static final int NODE_ID_BITS = 30;
	private static final int maxNodeId = (int) (Math.pow(2, NODE_ID_BITS) - 1);

	public static int generateNodeId() {
		try {
			String hostname = java.net.InetAddress.getLocalHost().getHostName();
			String ip = java.net.InetAddress.getLocalHost().getHostAddress();
			String mac = getMacAddress();
			String nodeInfo = hostname + ip + mac;
			log.info("Node Info: " + nodeInfo);
			return nodeInfo.hashCode() & maxNodeId;
		} catch (Exception ex) {
			return new java.security.SecureRandom().nextInt(maxNodeId + 1);
		}
	}

	private static String getMacAddress() throws Exception {
		Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
		while (networkInterfaces.hasMoreElements()) {
			NetworkInterface networkInterface = networkInterfaces.nextElement();
			byte[] macBytes = networkInterface.getHardwareAddress();
			if (macBytes != null) {
				StringBuilder sb = new StringBuilder();
				for (byte b : macBytes) {
					sb.append(String.format("%02X", b));
				}
				return sb.toString();
			}
		}
		return "";
	}
}
