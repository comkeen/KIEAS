package kr.or.kpew.kieas.network.xmpp;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.Manager;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.DefaultExtensionElement;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.builder.XmlDocument;
import org.xmlpull.v1.builder.impl.XmlDocumentImpl;
import org.xmlpull.v1.builder.impl.XmlElementImpl;

import kr.or.kpew.kieas.common.IOnMessageHandler;
import kr.or.kpew.kieas.network.ITransmitter;

public class XmppTransmitter implements ITransmitter {

	protected AbstractXMPPConnection connection;
	protected String server;
	
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
		//ProviderManager.addExtensionProvider(PlatformMessage.ROOT_ELEMENT_NAME, PlatformMessage.NAMESPACE_URI, new PlatformMessage.Provider());

	}

	@Override
	public void sendTo(String target, String message) {
		// TODO Auto-generated method stub
		//System.out.println("Send to " + target + " : " + message);
		
		System.out.println(message);
		Message m = new Message(target+"@xmpp.raychani.net", Type.chat);
		m.setFrom(this.connection.getUser());
		String xml = message.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n", "");
		
		PlatformMessage pm = new PlatformMessage(PlatformMessage.Type.AlertBroadcastRequest, xml);
		m.addExtension(pm);
		
		//System.out.println(pm.toXML());
		
		Chat chat = ChatManager.getInstanceFor(connection).createChat(target+"@xmpp.raychani.net");
		
		try {
			chat.sendMessage(m);
			//connection.sendStanza(m);
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void addReceiver(String destination) {
	       ChatManager manager = ChatManager.getInstanceFor(this.connection);
	       
//	       connection.addAsyncStanzaListener(new StanzaListener() {
//			
//			@Override
//			public void processPacket(Stanza packet) throws NotConnectedException {
//				DefaultExtensionElement extension = packet.getExtension(PlatformMessage.ROOT_ELEMENT_NAME, PlatformMessage.NAMESPACE_URI);
//				String xml = PlatformMessage.buildXml(extension);
//				System.out.println(PlatformMessage.parsePlatform(xml));
//				
//				handler.onMessage(PlatformMessage.parsePlatform(xml));
//				
//			}
//
//
//		}, new StanzaFilter() {
//			
//			@Override
//			public boolean accept(Stanza stanza) {
//				if(stanza.hasExtension("PlatformMessage", "http://raychani.net/MultimediaEmergencyInformationPlatform"))
//					return true;
//				else
//					return false;
//			}
//		});
	       
	       manager.addChatListener(new ChatManagerListener() {
			
			@Override
			public void chatCreated(Chat chat, boolean createdLocally) {
				chat.addMessageListener(new ChatMessageListener() {
					
					@Override
					public void processMessage(Chat chat, Message message) {
						// TODO Auto-generated method stub
						//String msg = message.toString();
						//System.out.println(message.toXML());
						
						//System.out.println(PlatformMessage.parseXmpp(msg));
						
						
						//System.out.println(msg);
						
						DefaultExtensionElement extension = message.getExtension(PlatformMessage.ROOT_ELEMENT_NAME, PlatformMessage.NAMESPACE_URI);
						String xml = PlatformMessage.buildXml(extension);
						String cap = PlatformMessage.parsePlatform(xml);
						if(cap==null)
							return;
						handler.onMessage(cap);
						
						
					}
				});
			}

		});

		
	}

	@Override
	public void setOnMessageHandler(IOnMessageHandler handler) {
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
