package com.gpcoder.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class EchoClient {

	public final static String SERVER_HOSTNAME = "127.0.0.1";
	public final static int SERVER_PORT = 7;

	public static void main(String[] args) throws IOException {
		DatagramSocket ds = null;
		System.out.println("Nhập tin nhắn: ");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				String theString = br.readLine();
				byte[] sendData = new byte[1000];
				sendData = theString.getBytes();
				InetAddress serverAddress = InetAddress.getByName(SERVER_HOSTNAME);
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, SERVER_PORT);
				ds = new DatagramSocket();
				ds.send(sendPacket);

				byte[] receiveData = new byte[1000];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				ds.receive(receivePacket);
				String str = new String(receivePacket.getData());
				System.out.println("Received: " + str);
				System.out.println("Nhập tin nhắn: ");
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
