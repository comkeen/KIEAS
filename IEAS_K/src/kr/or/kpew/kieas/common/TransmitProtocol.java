package kr.or.kpew.kieas.common;

/**
 * 통합경보 전송프로토콜(v2.0_20141027)을 구현한 클래스 
 * @author raychani
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class TransmitProtocol{
	
	
	/**
	 * 명령어 코드(CMD)를 표현하기 위한 열거형
	 * @author raychani
	 *
	 */
	public enum Command {
		Issue((byte)0x01),		// 발령
		Response((byte)0x02),		// 발령에 대한 응답
		Request((byte)0x03),	// 접속 인증 요청
		Result((byte)0x04),		// 접속 인증 결과
		Polling((byte)0xff);	// 폴링
		
		byte value;
        private Command(byte value) {
            this.value = value;
        }
	}
	
	/**
	 * 명령을 최초 전송한 시스템의 종류를 표현하기 위한 열거형
	 * @author raychani
	 *
	 */
	public enum Sender {
		Issuer((byte)0x01),			// 표준발령대
		Gateway((byte)0x02),		// 통합게이트웨이
		AlertSystem((byte)0x03),	// 표준경보시스템
		AlertTerminal((byte)0x04);	// 경보단말
		
		byte value;
        private Sender(byte value) {
            this.value = value;
        }
	}
	
	private static byte[] polling;

	
	static final byte[] header = {'K', 'C', 'A', 'P'}; 
	//int length;
	private Command command;
	private Sender sender;
	private byte[] data;
	
	public TransmitProtocol() {
	}
	
	public TransmitProtocol(Command cmd, Sender type, byte[] data) {
		setCommand(cmd);
		setSender(type);
		setData(data);
	}
	
	/**
	 * 5초에 한번 전송하고, 내용이 변하지 않는 폴링 메시지를 미리 생성하 두기 위한 변수
	 * @author raychani 
	 */
	public static byte[] GetDefaultPollingMessage() {
		if(polling != null)
			return polling;
		
		TransmitProtocol p = new TransmitProtocol(Command.Polling, Sender.Issuer, null);
		polling = p.encode();
		return polling;			
	}
	
	/**
	 * 객체를 메시지(바이트 배열)로 변환하기 위한 메소드
	 * @return 변환한 메시지
	 */
	public byte[] encode() {		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		byte[] b = new byte[2];
		
		int length;
		if(data == null)
			length = 0;
		else
			length = data.length;
		
		try {
			stream.write(header);
			
			b[0] = (byte)(length & 0xff);
			b[1] = (byte)(length >> 8);
			stream.write(b);
			
			stream.write(getCommand().value);
			stream.write(getSender().value);
			if(length > 0)
				stream.write(getData());
			
			Arrays.fill(b, (byte)0);
			stream.write(b);
			
			stream.close();
			
			return stream.toByteArray();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 수신한 메시지를 객체로 변환하기 위한 메소드
	 * @param buf: 수신한 메시지
	 * @return : 변환한 객체
	 */
	public static TransmitProtocol decode(byte[] buf) {
		ByteArrayInputStream stream = new ByteArrayInputStream(buf);
		TransmitProtocol p = new TransmitProtocol();
		
		byte[] b;
		int length;
		
		try {
			b = new byte[4];
			if(4 != stream.read(b))
				return null;
			if(!header.equals(b))
				return null;
			
			b = new byte[2];
			if(2 != stream.read(b))
				return null;
			length = b[0] | b[1];
	
			b = new byte[1];
			if(1 != stream.read(b))
				return null;
			p.setCommand(b[0]);
			
			b = new byte[1];
			if(1 != stream.read(b))
				return null;
			p.setSender(b[0]);
			
			p.setData(new byte[length]);
			if(length != stream.read(p.getData()))
				return null;
			
			b = new byte[2];
			if(2 != stream.read(b))
				return null;
			
			if(0 != stream.available())
				return null;
			
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return p;
	}


	
	public Command getCommand() {
		return command;
	}


	public void setCommand(Command command) {
		this.command = command;
	}

	public void setCommand(byte value) {
		switch (value) {
		case 0x01:
			command = Command.Issue;
			break;
		case 0x02:
			command = Command.Response;
			break;
		case 0x03:
			command = Command.Request;
			break;
		case 0x04:
			command = Command.Request;
			break;
		case (byte) 0xff:
			command = Command.Polling;
			break;
		default:
			throw new IllegalArgumentException("정의되지 않은 명령어 코드(CMD): " + value);
		}
		
	}



	public Sender getSender() {
		return sender;
	}



	public void setSender(Sender sender) {
		this.sender = sender;
	}
	
	public void setSender(byte value) {
		switch (value) {
		case 0x01:
			sender = Sender.Issuer;
			break;
		case 0x02:
			sender = Sender.Gateway;
			break;
		case 0x03:
			sender = Sender.AlertSystem;
			break;
		case 0x04:
			sender = Sender.AlertTerminal;
			break;
		default:
			throw new IllegalArgumentException("정의되지 않은 SENDER TYPE: " + value);
		}
	}



	public byte[] getData() {
		return data;
	}



	public void setData(byte[] data) {
		this.data = data;
	}
	
	public void print() {
		System.out.println(header);
		System.out.println(command);
		System.out.println(sender);
		System.out.println(data);
	}
}
