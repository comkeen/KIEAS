package kr.or.kpew.kieas.network.xmpp;

import org.jivesoftware.smack.packet.ExtensionElement;

public class PlatformMessage implements ExtensionElement
{
	private String element;
	private CharSequence xml;
	private String namespace;
	
	public PlatformMessage(String element, CharSequence xml, String namespace) {
		// TODO Auto-generated constructor stub
		this.element = element;
		this.xml = xml;
		this.namespace = namespace;		
	}
	
	@Override
	public String getElementName() {
		// TODO Auto-generated method stub
		return element;
	}

	@Override
	public CharSequence toXML() {
		// TODO Auto-generated method stub
		return xml;
	}

	@Override
	public String getNamespace() {
		// TODO Auto-generated method stub
		return namespace;
	}
}
