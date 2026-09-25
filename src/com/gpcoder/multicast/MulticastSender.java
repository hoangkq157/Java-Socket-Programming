package com.gpcoder.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastSender {

	public final static String SERVER_HOSTNAME = "224.0.0.1";
	public final static int PORT = 8888;

	public static void main(String[] args) throws IOException, InterruptedException {
		InetAddress serverAddress = null;
		MulticastSocket socket = null;
		try {
			socket = new MulticastSocket();
			serverAddress = InetAddress.getByName(SERVER_HOSTNAME);
			socket.setTimeToLive(1);
			System.out.println("Multicast Sender running at:" + socket.getLocalSocketAddress());
			while (true) {
				String message = "Hello from multicast sender";
				byte[] buffer = message.getBytes();
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, PORT);
				socket.send(packet);
				System.out.println("Sent: " + message);
				Thread.sleep(1000);
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
