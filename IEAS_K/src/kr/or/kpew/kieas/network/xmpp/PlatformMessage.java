package kr.or.kpew.kieas.network.xmpp;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jivesoftware.smack.packet.DefaultExtensionElement;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.provider.EmbeddedExtensionProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class PlatformMessage implements ExtensionElement
{
	public static final String ROOT_ELEMENT_NAME = "PlatformMessage";
	public static final String NAMESPACE_URI = "http://raychani.net/MultimediaEmergencyInformationPlatform";
	public static final String NAMESPACE_NAME = "mei";

	public static final String NS_CAP12 = "urn:oasis:names:tc:emergency:cap:1.2";


	enum Type {
		AlertBroadcastRequest,
		AlertBroadcastResponse,
		GetAlertRequest,
		GetAlertResponse,
		RegisterRequest,
		RegisterResponse,
		ReportRequest
	}

	static private int version = 1;
	private Type type;
	private String xml;
	
	public PlatformMessage() {
	}

	public PlatformMessage(Type type, String content) {
		this.type = Type.AlertBroadcastRequest;
		this.xml = createMessage(type, content);
	}

	private static String createMessage(Type type, String content) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(content));

			Document doc = builder.parse(is);

			Element platform = doc.createElementNS(NAMESPACE_URI, NAMESPACE_NAME + ":" + ROOT_ELEMENT_NAME);
			platform.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:"+NAMESPACE_NAME, NAMESPACE_URI);

			Element ver = doc.createElementNS(NAMESPACE_URI, NAMESPACE_NAME + ":version");

			ver.appendChild(doc.createTextNode(""+PlatformMessage.version));
			Element envelope = doc.createElementNS(NAMESPACE_URI, NAMESPACE_NAME + ":" + type.toString());
			Element credential = doc.createElementNS(NAMESPACE_URI, NAMESPACE_NAME + ":credential");
			ver.appendChild(doc.createTextNode(""));

			//Document part = builder.parse(is);
			//Element cap = part.getDocumentElement();

			Element cap = doc.getDocumentElement();

			envelope.appendChild(cap);
			platform.appendChild(ver);
			platform.appendChild(envelope);
			platform.appendChild(credential);

			return toString(platform);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public CharSequence toXML() {
		return xml;
	}

	public static String parseXmpp(String xmpp) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xmpp));

		Document doc;
		try {
			doc = builder.parse(is);
		} catch (SAXException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		Element root = doc.getDocumentElement();

		if(!"message".equals(root.getNodeName())) {
			System.out.println("error");
			return null;
		}

		Node node = root.getFirstChild();
		while(node != null) {
			System.out.println(node.toString());

			if(ROOT_ELEMENT_NAME.equals(node.getNodeName())) {
				return toString(node);
			}

			node = node.getNextSibling();
		}



		Element e = root;
		return null;
	}
	
	public static String parsePlatform(String xmpp) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xmpp));

		Document doc;
		try {
			doc = builder.parse(is);
		} catch (SAXException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		Element root = doc.getDocumentElement();

		if(!ROOT_ELEMENT_NAME.equals(root.getNodeName())) {
			System.out.println("error");
			return null;
		}
		
		Node node;
		Node body;
		
		node = root.getFirstChild();
		while(node != null) {
			if(node.getNodeType() != Node.ELEMENT_NODE) {
				node = node.getNextSibling();
				continue;
			}

			if("version".equals(node.getNodeName())) {
				node = node.getNextSibling();
				continue;
			}
			
			switch (Type.valueOf(node.getNodeName())) {
			case AlertBroadcastRequest:
				Node n = node.getFirstChild();
				while(n != null) {
					if(n.getNodeType() != Node.ELEMENT_NODE) {
						n = n.getNextSibling();
						continue;
					}
						
					
					if("alert".equals(n.getNodeName()))
						return toString(n);
					
					node = node.getNextSibling();
				}
				
				break;
			default:
				break;
			}
			
			if("cridential".equals(node.getNodeName())) {
				node = node.getNextSibling();
				continue;
			}

		node = node.getNextSibling();
	}
		
		return toString(root);
		

//		node = root.getFirstChild();
//		while(node != null) {
//			System.out.println(node.toString());
//
//			if(ROOT_ELEMENT_NAME.equals(node.getNodeName())) {
//				return toString(node);
//			}
//
//			node = node.getNextSibling();
//		}
//
//
//
//		Element e = root;
//		return null;
	}

	private static String toString(Node node) {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		StringWriter writer = new StringWriter();
		try {
			transformer.transform(new DOMSource(node), new StreamResult(writer));
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return writer.getBuffer().toString();

	}

	@Override
	public String getElementName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNamespace() {
		// TODO Auto-generated method stub
		return null;
	}


	public static String buildXml(DefaultExtensionElement extension) {
		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element root = doc.createElement(ROOT_ELEMENT_NAME);
		Element message;
		Element alert;
		
		
		appendChild(extension, doc, root, "version");	
		message = appendChild(extension, doc, root, Type.AlertBroadcastRequest.toString());
		appendChild(extension, doc, root, "credential");
		
		alert = appendChild(extension, doc, message, "alert");	
		alert.setAttribute("xmlns", "urn:oasis:names:tc:emergency:cap:1.2");
		
		appendChild(extension, doc, alert, "identifier");
		appendChild(extension, doc, alert, "sender");
		appendChild(extension, doc, alert, "sent");
		appendChild(extension, doc, alert, "status");
		appendChild(extension, doc, alert, "msgType");
		appendChild(extension, doc, alert, "scope");
		appendChild(extension, doc, alert, "code");
		appendChild(extension, doc, alert, "references");
	
		
		Stack<String> stack = new Stack<>();
		StringBuilder builder = new StringBuilder();

		builder.append(ROOT_ELEMENT_NAME);
		builder.insert(0, ROOT_ELEMENT_NAME);
		
		builder.append(ROOT_ELEMENT_NAME);
		builder.insert(0, ROOT_ELEMENT_NAME);
		
		
		return toString(root);
	}

	private static Element appendChild(DefaultExtensionElement extension, Document doc, Element parent, String name) {
		if(!extension.getNames().contains(name))
			return null;
		
		
		Element element;
		element = doc.createElement(name);
		if(extension.getValue(name) != null) {
			element.appendChild(doc.createTextNode(extension.getValue(name)));
		}
		parent.appendChild(element);
		return element;
	}


}