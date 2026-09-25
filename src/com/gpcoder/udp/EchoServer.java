package com.gpcoder.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class EchoServer {

	public final static int SERVER_PORT = 7;

	public static void main(String[] args) throws IOException {
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket(SERVER_PORT);
			System.out.println("Server created on port " + ds.getLocalPort());
			System.out.println("Waiting for messages from client ...");
			while (true) {
				byte[] buffer = new byte[1000];
				DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
				ds.receive(incoming);
				String message = new String(incoming.getData(), 0, incoming.getLength());
				System.out.println("Received: " + message);

				String messageToSend = new StringBuilder(message).reverse().toString().toUpperCase();
				byte[] sendData = messageToSend.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, incoming.getAddress(),
						incoming.getPort());
				ds.send(sendPacket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ds != null) {
				ds.close();
			}
		}
	}
}
