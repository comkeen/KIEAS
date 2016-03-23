package kr.or.kpew.kieas.protocol;

import kr.or.kpew.kieas.protocol.Protocol.Command;
import kr.or.kpew.kieas.protocol.Protocol.SenderType;

public class IssuerTransmitterTcp extends AbstractTcpTransmitter {

	public IssuerTransmitterTcp() {
		super(SenderType.Issuer);
	}

	@Override
	public void run() {
		initClientSocket();

		requestConnection();

		sendAlert();
		// sendAndReceiveAck("alert");

		System.out.println("issuer finished.");
	}

	public void requestConnection() {
		Protocol p = new Protocol(Command.RegistRequest, type, null);
		send(p.encode());
		receiveAck();
	}

	public void sendAlert() {
		Protocol p = new Protocol(Command.Issue, type, "Alert".getBytes());
		send(p.encode());
		receiveAck();
	}

}
