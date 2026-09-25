package com.gpcoder.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReceiver {

	public final static String SERVER_HOSTNAME = "224.0.0.1";
	public final static int PORT = 8888;

	public static void main(String[] args) throws IOException {
		MulticastSocket socket = null;
		try {
			socket = new MulticastSocket(PORT);
			InetAddress serverAddress = InetAddress.getByName(SERVER_HOSTNAME);
			socket.joinGroup(serverAddress);
			System.out.println("Multicast Receiver running at:" + socket.getLocalSocketAddress());
			while (true) {
				byte[] buffer = new byte[1000];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				String message = new String(packet.getData(), 0, packet.getLength());
				System.out.println("Received: " + message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				socket.close();
			}
		}
	}
}
