package kr.or.kpew.kieas.protocol;

import java.net.Socket;

import kr.or.kpew.kieas.protocol.Protocol.SenderType;

public class IssuerClient extends AbstractTcpTransmitter implements Runnable {
	GatewayTransmitterTcp gateway;

	public IssuerClient(GatewayTransmitterTcp gateway, Socket client) {
		super(SenderType.Gateway);
		this.gateway = gateway;
		this.clientSocket = client;
	}

	public boolean isAllowed(String ip) {
		return true;
	}

	@Override
	public void run() {
		initClientSocket();
		System.out.println("issuer connected: " + clientSocket.getInetAddress().getHostAddress());

		if (!isAllowed(clientSocket.getInetAddress().getHostAddress()))
			return;
		byte[] buf;

		while (true) {

			buf = receive(clientSocket);
			sendAck(null);

			gateway.sendAlert(buf);

		}

		// System.out.println("client finished.");

	}
}
