package kr.or.kpew.kieas.network.xmpp;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import kr.or.kpew.kieas.common.IOnMessageHandler;
import kr.or.kpew.kieas.network.ITransmitter;

public class XmppTransmitter implements ITransmitter {

	protected String server = "maingateway";
	protected StanzaListener listener;
	protected AbstractXMPPConnection connection;
	
	protected IOnMessageHandler handler;

	@Override
	public void init(String destination) {
		System.out.println("destination : " + destination);
		
		
		XMPPTCPConnectionConfiguration configuration;
		try {
			configuration = XMPPTCPConnectionConfiguration.builder()
					.setUsernameAndPassword(destination, "mmlab")
					.setSecurityMode(SecurityMode.disabled)
					.setCustomSSLContext(SSLContext.getDefault())
					.setServiceName("xmpp.raychani.net")
					.setHost("192.168.0.128").build();
			
			connection = new XMPPTCPConnection(configuration);
			connection.connect();
			connection.login();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SmackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		addReceiver(destination);
	}

	@Override
	public void sendTo(String target, String message) {
		// TODO Auto-generated method stub
		//System.out.println("Send to " + target + " : " + message);
		
		Message m = new Message();
		m.addExtension(new PlatformMessage("aaa", message, "bbb"));
		Chat chat = ChatManager.getInstanceFor(connection).createChat(target+"@xmpp.raychani.net");
		try {
			
			chat.sendMessage(m);
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void addReceiver(String destination) {
	       ChatManager manager = ChatManager.getInstanceFor(this.connection);
	       manager.addChatListener(new ChatManagerListener() {
			
			@Override
			public void chatCreated(Chat chat, boolean createdLocally) {
				// TODO Auto-generated method stub
				chat.addMessageListener(new ChatMessageListener() {
					
					@Override
					public void processMessage(Chat chat, Message message) {
						// TODO Auto-generated method stub
						
						handler.onMessage(message.getBody());
					}
				});
			}
		});

		
	}

	@Override
	public void setOnMessageHandler(IOnMessageHandler handler) {
		// TODO Auto-generated method stub
		this.handler = handler;
	}

	@Override
	public void open() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
