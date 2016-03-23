package kr.or.kpew.kieas.protocol;

import kr.or.kpew.kieas.protocol.Protocol.Command;
import kr.or.kpew.kieas.protocol.Protocol.SenderType;

public class AlertSystemTransmitterTcp extends AbstractTcpTransmitter {

	public AlertSystemTransmitterTcp() {
		super(SenderType.AlertSystem);
	}

	@Override
	public void run() {
		initClientSocket();

		requestConnection();

		waitForReceive();
		System.out.println("alertsystem finished");
	}

	public byte[] waitForReceive() {
		while (true) {
			byte[] buf = receive(clientSocket);
			sendAck(null);

			System.out.println(Integer.toHexString(this.hashCode()) + ": alert!!!!!!!!!!!!!!!: " + new String(buf));
		}
	}

	public void requestConnection() {
		Protocol p = new Protocol(Command.RegistRequest, SenderType.AlertSystem, null);
		send(p.encode());
		receive(clientSocket);
	}

}
