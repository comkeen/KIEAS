package kr.or.kpew.kieas.network.xmpp;




import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import kr.or.kpew.kieas.common.IOnMessageHandler;
import kr.or.kpew.kieas.jaxb.Alert;
import kr.or.kpew.kieas.network.ITransmitter;
import rocks.xmpp.addr.Jid;
import rocks.xmpp.core.session.Extension;
import rocks.xmpp.core.session.TcpConnectionConfiguration;
import rocks.xmpp.core.session.XmppClient;
import rocks.xmpp.core.session.XmppSessionConfiguration;
import rocks.xmpp.core.stanza.model.Message;

public class BabblerTransmitter implements ITransmitter {
	protected IOnMessageHandler handler;
	private XmppClient client;

	@Override
	public void init(String destination) {
		TcpConnectionConfiguration tcpConfiguration = TcpConnectionConfiguration.builder()
				.port(5222)
				.secure(false)
				.build();
		XmppSessionConfiguration configuration = XmppSessionConfiguration.builder()
				.extensions(Extension.of(Alert.class))
				.build();
		client = XmppClient.create("localhost", configuration, tcpConfiguration);

		
		try {
			client.connect();

			client.login(destination, "mmlab");
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		addReceiver(destination);
	}

	@Override
	public void sendTo(String target, String message) {
		
		Message m = new Message(Jid.of(target+"@xmpp.raychani.net"), Message.Type.CHAT);
		
		try {
			JAXBContext context = JAXBContext.newInstance(Alert.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			StringReader reader = new StringReader(message);
			Alert alert = (Alert) unmarshaller.unmarshal(reader);
			m.addExtension(alert);
			client.send(m);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addReceiver(String destination) {
		client.addInboundMessageListener(e -> {
			Message message = e.getMessage();
			Alert alert = message.getExtension(Alert.class);
			
			JAXBContext context;
			try {
				context = JAXBContext.newInstance(Alert.class);
				Marshaller marshaller = context.createMarshaller();
				StringWriter writer = new StringWriter();
				
				marshaller.marshal(alert, writer);
				
				String xml = writer.toString();
				handler.onMessage(xml);
			} catch (JAXBException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
