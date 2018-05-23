//
// 이 파일은 JAXB(JavaTM Architecture for XML Binding) 참조 구현 2.2.8-b130911.1802 버전을 통해 생성되었습니다. 
// <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>를 참조하십시오. 
// 이 파일을 수정하면 소스 스키마를 재컴파일할 때 수정 사항이 손실됩니다. 
// 생성 날짜: 2017.02.14 시간 06:51:41 PM KST 
//


package kr.or.kpew.kieas.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cap package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ValueName_QNAME = new QName("urn:oasis:names:tc:emergency:cap:1.2", "valueName");
    private final static QName _Value_QNAME = new QName("urn:oasis:names:tc:emergency:cap:1.2", "value");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cap
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Alert }
     * 
     */
    public Alert createAlert() {
        return new Alert();
    }

    /**
     * Create an instance of {@link Alert.Info }
     * 
     */
    public Alert.Info createAlertInfo() {
        return new Alert.Info();
    }

    /**
     * Create an instance of {@link Alert.Info.Area }
     * 
     */
    public Alert.Info.Area createAlertInfoArea() {
        return new Alert.Info.Area();
    }

    /**
     * Create an instance of {@link Alert.Info.EventCode }
     * 
     */
    public Alert.Info.EventCode createAlertInfoEventCode() {
        return new Alert.Info.EventCode();
    }

    /**
     * Create an instance of {@link Alert.Info.Parameter }
     * 
     */
    public Alert.Info.Parameter createAlertInfoParameter() {
        return new Alert.Info.Parameter();
    }

    /**
     * Create an instance of {@link Alert.Info.Resource }
     * 
     */
    public Alert.Info.Resource createAlertInfoResource() {
        return new Alert.Info.Resource();
    }

    /**
     * Create an instance of {@link Alert.Info.Area.Geocode }
     * 
     */
    public Alert.Info.Area.Geocode createAlertInfoAreaGeocode() {
        return new Alert.Info.Area.Geocode();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:emergency:cap:1.2", name = "valueName")
    public JAXBElement<String> createValueName(String value) {
        return new JAXBElement<String>(_ValueName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:emergency:cap:1.2", name = "value")
    public JAXBElement<String> createValue(String value) {
        return new JAXBElement<String>(_Value_QNAME, String.class, null, value);
    }

}
