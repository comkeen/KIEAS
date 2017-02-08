package kr.or.kpew.kieas.network.xmpp;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.commons.logging.Log;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import kr.or.kpew.kieas.common.IOnMessageHandler;
import kr.or.kpew.kieas.network.ITransmitter;

public class AlertSystemXmpp extends XmppTransmitter {
	
	@Override
	public void sendTo(String target, String message) {
		// TODO Auto-generated method stub
		
		target = server;
		
		super.sendTo(target, message);
	}


}
