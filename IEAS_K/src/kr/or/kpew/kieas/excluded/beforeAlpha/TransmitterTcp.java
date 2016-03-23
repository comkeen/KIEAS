package kr.or.kpew.kieas.gateway.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.or.kpew.kieas.common.ITransmitter;
import kr.or.kpew.kieas.protocol.Protocol;
import kr.or.kpew.kieas.protocol.Protocol.SenderType;


public class TransmitterTcp implements ITransmitter
{		
	private static final String SERVER_ADDRESS = "localhost";
	private static final int SERVER_PORT = 26750;
	private int port;
	
	private SenderType senderType;
	private List<Socket> serverSockets;
	private List<Socket> clientSockets;

	
	public TransmitterTcp()
	{
		this.senderType = Protocol.SenderType.Gateway;

		init();
	}
	
	public TransmitterTcp(int port)
	{
		this.senderType = Protocol.SenderType.Gateway;
		this.port = port;
		init();
	}
	
	public void init()
	{		
		this.serverSockets = new ArrayList<>();
		
		openConnection();
	}
	
	@Override
	public void openConnection()
	{
		addReceiver(SERVER_ADDRESS + ":" + port);
	}
	
	@Override
	public void closeConnection()
	{
		for (Socket socket : clientSockets)
		{
			try
			{
				socket.close();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		for (Socket socket : serverSockets)
		{
			try
			{
				socket.close();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
		
	@Override
	public void addReceiver(String destination)
	{		
		String[] tokens = destination.split(":");
		String ip = tokens[0];
		System.out.println("ip : " + ip);
		String port = tokens[1];
		System.out.println("port : " + port);
		
		try
		{
			Socket socket = new Socket(ip, Integer.parseInt(port));
			this.serverSockets.add(socket);
			new Receiver(this, socket).run();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void removeReceiver(String target) {}
	
	@Override
	public void sendMessage(String message, String destination) 
	{
		String[] tokens = destination.split(":");
		String ip = tokens[0];
		String port = tokens[1];
		
		try
		{
			if(clientSockets.isEmpty())
			{
				Socket socket = new Socket(ip, Integer.parseInt(port));
				clientSockets.add(socket);
			}
			for (Socket socket : clientSockets)
			{
				try
				{
					socket.getOutputStream().write(stringToByte(message));
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		} 
		catch (NumberFormatException | IOException e) 
		{
			e.printStackTrace();
		}
		
		

	}

	@Override
	public void broadcastMessage(String message, String topic) {}
	
	@Override
	public void accept(byte[] received)
	{
		Protocol protocol = Protocol.decode(received);
		
		System.out.println("received message on tcp ip : \n" + protocol.getData());
	}
	
	private byte[] stringToByte(String s)
	{
		try
		{
			return s.getBytes("utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	
	
	
	private class Receiver implements Runnable
	{
		private ITransmitter transmitter;
		private Socket socket;
		
		private byte[] buf = new byte[70000];
		
		
		public Receiver(ITransmitter transmitter, Socket socket)
		{
			this.transmitter = transmitter;
			this.socket = socket;
		}

		@Override
		public void run()
		{
			System.out.println("Run Receiver");
			while(true)
			{
				try
				{
					byte[] received = Arrays.copyOf(buf, socket.getInputStream().read());
					transmitter.accept(received);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}		
	}
}




