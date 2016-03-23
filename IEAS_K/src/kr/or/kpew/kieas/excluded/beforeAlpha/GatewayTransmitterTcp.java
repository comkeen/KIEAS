package kr.or.kpew.kieas.protocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import kr.or.kpew.kieas.protocol.Protocol.Command;
import kr.or.kpew.kieas.protocol.Protocol.SenderType;

public class GatewayTransmitterTcp extends AbstractTcpTransmitter {

	List<AlertSystemClient> alertSystems = new ArrayList<>();
	ServerSocket welcome;

	public GatewayTransmitterTcp() {
		super(SenderType.Gateway);
	}

	public void initServer() {
		try {
			welcome = new ServerSocket(SERVER_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("server started");

	}

	public void openConnection() {
		byte[] buf;
		while (true) {
			try {
				clientSocket = welcome.accept();
//				initClientSocket();

				// receiveAndSendAck();
				buf = receive(clientSocket);
				Protocol p;
				p = new Protocol(Command.RegistResponse, SenderType.Gateway, null);
				// sendAck();
				send(p.encode());

				p = Protocol.decode(buf);

				Runnable r = null;
				switch (p.getSender()) {
				case Issuer:
					r = new IssuerClient(this, clientSocket);
					break;
				case AlertSystem:
					AlertSystemClient c = new AlertSystemClient(clientSocket);
					alertSystems.add(c);
					r = c;
					break;
				default:
					throw new UnsupportedOperationException();
				}
				if (r != null)
					new Thread(r).start();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (UnsupportedOperationException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendAlert(byte[] alert) {
		for (AlertSystemClient alertSystemClient : alertSystems) {
			alertSystemClient.sendAlert(alert);
		}
	}

	@Override
	public void run() {
		initServer();
		openConnection();
		System.out.println("gateway finished.");
	}

}
