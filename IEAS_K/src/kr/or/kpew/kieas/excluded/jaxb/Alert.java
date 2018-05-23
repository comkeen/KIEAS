//
// 이 파일은 JAXB(JavaTM Architecture for XML Binding) 참조 구현 2.2.8-b130911.1802 버전을 통해 생성되었습니다. 
// <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>를 참조하십시오. 
// 이 파일을 수정하면 소스 스키마를 재컴파일할 때 수정 사항이 손실됩니다. 
// 생성 날짜: 2017.02.14 시간 06:51:41 PM KST 
//


package kr.or.kpew.kieas.jaxb;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;
import org.w3c.dom.Element;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "identifier",
    "sender",
    "sent",
    "status",
    "msgType",
    "source",
    "scope",
    "restriction",
    "addresses",
    "code",
    "note",
    "references",
    "incidents",
    "info",
    "any"
})
@XmlRootElement(name = "alert")
public class Alert {

    @XmlElement(required = true)
    protected String identifier;
    @XmlElement(required = true)
    protected String sender;
    @XmlElement(required = true)
    protected XMLGregorianCalendar sent;
    @XmlElement(required = true)
    protected String status;
    @XmlElement(required = true)
    protected String msgType;
    protected String source;
    @XmlElement(required = true)
    protected String scope;
    protected String restriction;
    protected String addresses;
    protected List<String> code;
    protected String note;
    protected String references;
    protected String incidents;
    protected List<Alert.Info> info;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * identifier 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * identifier 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentifier(String value) {
        this.identifier = value;
    }

    /**
     * sender 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSender() {
        return sender;
    }

    /**
     * sender 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSender(String value) {
        this.sender = value;
    }

    /**
     * sent 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSent() {
        return sent;
    }

    /**
     * sent 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSent(XMLGregorianCalendar value) {
        this.sent = value;
    }

    /**
     * status 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * status 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * msgType 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgType() {
        return msgType;
    }

    /**
     * msgType 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgType(String value) {
        this.msgType = value;
    }

    /**
     * source 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSource() {
        return source;
    }

    /**
     * source 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSource(String value) {
        this.source = value;
    }

    /**
     * scope 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScope() {
        return scope;
    }

    /**
     * scope 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScope(String value) {
        this.scope = value;
    }

    /**
     * restriction 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRestriction() {
        return restriction;
    }

    /**
     * restriction 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRestriction(String value) {
        this.restriction = value;
    }

    /**
     * addresses 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddresses() {
        return addresses;
    }

    /**
     * addresses 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddresses(String value) {
        this.addresses = value;
    }

    /**
     * Gets the value of the code property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the code property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getCode() {
        if (code == null) {
            code = new ArrayList<String>();
        }
        return this.code;
    }

    /**
     * note 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNote() {
        return note;
    }

    /**
     * note 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNote(String value) {
        this.note = value;
    }

    /**
     * references 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferences() {
        return references;
    }

    /**
     * references 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferences(String value) {
        this.references = value;
    }

    /**
     * incidents 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncidents() {
        return incidents;
    }

    /**
     * incidents 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncidents(String value) {
        this.incidents = value;
    }

    /**
     * Gets the value of the info property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the info property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Alert.Info }
     * 
     * 
     */
    public List<Alert.Info> getInfo() {
        if (info == null) {
            info = new ArrayList<Alert.Info>();
        }
        return this.info;
    }

    /**
     * Gets the value of the any property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * {@link Element }
     * 
     * 
     */
    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
    }



    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "language",
        "category",
        "event",
        "responseType",
        "urgency",
        "severity",
        "certainty",
        "audience",
        "eventCode",
        "effective",
        "onset",
        "expires",
        "senderName",
        "headline",
        "description",
        "instruction",
        "web",
        "contact",
        "parameter",
        "resource",
        "area"
    })
    public static class Info {

        @XmlElement(defaultValue = "en-US")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "language")
        protected String language;
        @XmlElement(required = true)
        protected List<String> category;
        @XmlElement(required = true)
        protected String event;
        protected List<String> responseType;
        @XmlElement(required = true)
        protected String urgency;
        @XmlElement(required = true)
        protected String severity;
        @XmlElement(required = true)
        protected String certainty;
        protected String audience;
        protected List<Alert.Info.EventCode> eventCode;
        protected XMLGregorianCalendar effective;
        protected XMLGregorianCalendar onset;
        protected XMLGregorianCalendar expires;
        protected String senderName;
        protected String headline;
        protected String description;
        protected String instruction;
        @XmlSchemaType(name = "anyURI")
        protected String web;
        protected String contact;
        protected List<Alert.Info.Parameter> parameter;
        protected List<Alert.Info.Resource> resource;
        protected List<Alert.Info.Area> area;

        /**
         * language 속성의 값을 가져옵니다.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLanguage() {
            return language;
        }

        /**
         * language 속성의 값을 설정합니다.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLanguage(String value) {
            this.language = value;
        }

        /**
         * Gets the value of the category property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the category property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCategory().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getCategory() {
            if (category == null) {
                category = new ArrayList<String>();
            }
            return this.category;
        }

        /**
         * event 속성의 값을 가져옵니다.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEvent() {
            return event;
        }

        /**
         * event 속성의 값을 설정합니다.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEvent(String value) {
            this.event = value;
        }

        /**
         * Gets the value of the responseType property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the responseType property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getResponseType().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getResponseType() {
            if (responseType == null) {
                responseType = new ArrayList<String>();
            }
            return this.responseType;
        }

        /**
         * urgency 속성의 값을 가져옵니다.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUrgency() {
            return urgency;
        }

        /**
         * urgency 속성의 값을 설정합니다.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUrgency(String value) {
            this.urgency = value;
        }

        /**
         * severity 속성의 값을 가져옵니다.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSeverity() {
            return severity;
        }

        /**
         * severity 속성의 값을 설정합니다.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSeverity(String value) {
            this.severity = value;
        }

        /**
         * certainty 속성의 값을 가져옵니다.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCertainty() {
            return certainty;
        }

        /**
         * certainty 속성의 값을 설정합니다.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCertainty(String value) {
            this.certainty = value;
        }

        /**
         * audience 속성의 값을 가져옵니다.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAudience() {
            return audience;
        }

        /**
         * audience 속성의 값을 설정합니다.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAudience(String value) {
            this.audience = value;
        }

        /**
         * Gets the value of the eventCode property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the eventCode property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getEventCode().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Alert.Info.EventCode }
         * 
         * 
         */
        public List<Alert.Info.EventCode> getEventCode() {
            if (eventCode == null) {
                eventCode = new ArrayList<Alert.Info.EventCode>();
            }
            return this.eventCode;
        }

        /**
         * effective 속성의 값을 가져옵니다.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getEffective() {
            return effective;
        }

        /**
         * effective 속성의 값을 설정합니다.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setEffective(XMLGregorianCalendar value) {
            this.effective = value;
        }

        /**
         * onset 속성의 값을 가져옵니다.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getOnset() {
            return onset;
        }

        /**
         * onset 속성의 값을 설정합니다.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setOnset(XMLGregorianCalendar value) {
            this.onset = value;
        }

        /**
         * expires 속성의 값을 가져옵니다.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getExpires() {
            return expires;
        }

        /**
         * expires 속성의 값을 설정합니다.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setExpires(XMLGregorianCalendar value) {
            this.expires = value;
        }

        /**
         * senderName 속성의 값을 가져옵니다.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSenderName() {
            return senderName;
        }

        /**
         * senderName 속성의 값을 설정합니다.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSenderName(String value) {
            this.senderName = value;
        }

        /**
         * headline 속성의 값을 가져옵니다.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHeadline() {
            return headline;
        }

        /**
         * headline 속성의 값을 설정합니다.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHeadline(String value) {
            this.headline = value;
        }

        /**
         * description 속성의 값을 가져옵니다.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescription() {
            return description;
        }

        /**
         * description 속성의 값을 설정합니다.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescription(String value) {
            this.description = value;
        }

        /**
         * instruction 속성의 값을 가져옵니다.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getInstruction() {
            return instruction;
        }

        /**
         * instruction 속성의 값을 설정합니다.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setInstruction(String value) {
            this.instruction = value;
        }

        /**
         * web 속성의 값을 가져옵니다.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getWeb() {
            return web;
        }

        /**
         * web 속성의 값을 설정합니다.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setWeb(String value) {
            this.web = value;
        }

        /**
         * contact 속성의 값을 가져옵니다.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getContact() {
            return contact;
        }

        /**
         * contact 속성의 값을 설정합니다.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setContact(String value) {
            this.contact = value;
        }

        /**
         * Gets the value of the parameter property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the parameter property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getParameter().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Alert.Info.Parameter }
         * 
         * 
         */
        public List<Alert.Info.Parameter> getParameter() {
            if (parameter == null) {
                parameter = new ArrayList<Alert.Info.Parameter>();
            }
            return this.parameter;
        }

        /**
         * Gets the value of the resource property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the resource property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getResource().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Alert.Info.Resource }
         * 
         * 
         */
        public List<Alert.Info.Resource> getResource() {
            if (resource == null) {
                resource = new ArrayList<Alert.Info.Resource>();
            }
            return this.resource;
        }

        /**
         * Gets the value of the area property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the area property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getArea().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Alert.Info.Area }
         * 
         * 
         */
        public List<Alert.Info.Area> getArea() {
            if (area == null) {
                area = new ArrayList<Alert.Info.Area>();
            }
            return this.area;
        }


        /**
         * <p>anonymous complex type에 대한 Java 클래스입니다.
         * 
         * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="areaDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="polygon" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
         *         &lt;element name="circle" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
         *         &lt;element name="geocode" maxOccurs="unbounded" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element ref="{urn:oasis:names:tc:emergency:cap:1.2}valueName"/>
         *                   &lt;element ref="{urn:oasis:names:tc:emergency:cap:1.2}value"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="altitude" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
         *         &lt;element name="ceiling" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "areaDesc",
            "polygon",
            "circle",
            "geocode",
            "altitude",
            "ceiling"
        })
        public static class Area {

            @XmlElement(required = true)
            protected String areaDesc;
            protected List<String> polygon;
            protected List<String> circle;
            protected List<Alert.Info.Area.Geocode> geocode;
            protected BigDecimal altitude;
            protected BigDecimal ceiling;

            /**
             * areaDesc 속성의 값을 가져옵니다.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAreaDesc() {
                return areaDesc;
            }

            /**
             * areaDesc 속성의 값을 설정합니다.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAreaDesc(String value) {
                this.areaDesc = value;
            }

            /**
             * Gets the value of the polygon property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the polygon property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getPolygon().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link String }
             * 
             * 
             */
            public List<String> getPolygon() {
                if (polygon == null) {
                    polygon = new ArrayList<String>();
                }
                return this.polygon;
            }

            /**
             * Gets the value of the circle property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the circle property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getCircle().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link String }
             * 
             * 
             */
            public List<String> getCircle() {
                if (circle == null) {
                    circle = new ArrayList<String>();
                }
                return this.circle;
            }

            /**
             * Gets the value of the geocode property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the geocode property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getGeocode().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Alert.Info.Area.Geocode }
             * 
             * 
             */
            public List<Alert.Info.Area.Geocode> getGeocode() {
                if (geocode == null) {
                    geocode = new ArrayList<Alert.Info.Area.Geocode>();
                }
                return this.geocode;
            }

            /**
             * altitude 속성의 값을 가져옵니다.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getAltitude() {
                return altitude;
            }

            /**
             * altitude 속성의 값을 설정합니다.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setAltitude(BigDecimal value) {
                this.altitude = value;
            }

            /**
             * ceiling 속성의 값을 가져옵니다.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getCeiling() {
                return ceiling;
            }

            /**
             * ceiling 속성의 값을 설정합니다.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setCeiling(BigDecimal value) {
                this.ceiling = value;
            }


            /**
             * <p>anonymous complex type에 대한 Java 클래스입니다.
             * 
             * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;sequence>
             *         &lt;element ref="{urn:oasis:names:tc:emergency:cap:1.2}valueName"/>
             *         &lt;element ref="{urn:oasis:names:tc:emergency:cap:1.2}value"/>
             *       &lt;/sequence>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "valueName",
                "value"
            })
            public static class Geocode {

                @XmlElement(required = true)
                protected String valueName;
                @XmlElement(required = true)
                protected String value;

                /**
                 * valueName 속성의 값을 가져옵니다.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getValueName() {
                    return valueName;
                }

                /**
                 * valueName 속성의 값을 설정합니다.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setValueName(String value) {
                    this.valueName = value;
                }

                /**
                 * value 속성의 값을 가져옵니다.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getValue() {
                    return value;
                }

                /**
                 * value 속성의 값을 설정합니다.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setValue(String value) {
                    this.value = value;
                }

            }

        }


        /**
         * <p>anonymous complex type에 대한 Java 클래스입니다.
         * 
         * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element ref="{urn:oasis:names:tc:emergency:cap:1.2}valueName"/>
         *         &lt;element ref="{urn:oasis:names:tc:emergency:cap:1.2}value"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "valueName",
            "value"
        })
        public static class EventCode {

            @XmlElement(required = true)
            protected String valueName;
            @XmlElement(required = true)
            protected String value;

            /**
             * valueName 속성의 값을 가져옵니다.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getValueName() {
                return valueName;
            }

            /**
             * valueName 속성의 값을 설정합니다.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setValueName(String value) {
                this.valueName = value;
            }

            /**
             * value 속성의 값을 가져옵니다.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getValue() {
                return value;
            }

            /**
             * value 속성의 값을 설정합니다.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setValue(String value) {
                this.value = value;
            }

        }


        /**
         * <p>anonymous complex type에 대한 Java 클래스입니다.
         * 
         * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element ref="{urn:oasis:names:tc:emergency:cap:1.2}valueName"/>
         *         &lt;element ref="{urn:oasis:names:tc:emergency:cap:1.2}value"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "valueName",
            "value"
        })
        public static class Parameter {

            @XmlElement(required = true)
            protected String valueName;
            @XmlElement(required = true)
            protected String value;

            /**
             * valueName 속성의 값을 가져옵니다.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getValueName() {
                return valueName;
            }

            /**
             * valueName 속성의 값을 설정합니다.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setValueName(String value) {
                this.valueName = value;
            }

            /**
             * value 속성의 값을 가져옵니다.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getValue() {
                return value;
            }

            /**
             * value 속성의 값을 설정합니다.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setValue(String value) {
                this.value = value;
            }

        }


        /**
         * <p>anonymous complex type에 대한 Java 클래스입니다.
         * 
         * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="resourceDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="mimeType" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="size" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
         *         &lt;element name="uri" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
         *         &lt;element name="derefUri" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="digest" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "resourceDesc",
            "mimeType",
            "size",
            "uri",
            "derefUri",
            "digest"
        })
        public static class Resource {

            @XmlElement(required = true)
            protected String resourceDesc;
            @XmlElement(required = true)
            protected String mimeType;
            protected BigInteger size;
            @XmlSchemaType(name = "anyURI")
            protected String uri;
            protected String derefUri;
            protected String digest;

            /**
             * resourceDesc 속성의 값을 가져옵니다.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getResourceDesc() {
                return resourceDesc;
            }

            /**
             * resourceDesc 속성의 값을 설정합니다.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setResourceDesc(String value) {
                this.resourceDesc = value;
            }

            /**
             * mimeType 속성의 값을 가져옵니다.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getMimeType() {
                return mimeType;
            }

            /**
             * mimeType 속성의 값을 설정합니다.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setMimeType(String value) {
                this.mimeType = value;
            }

            /**
             * size 속성의 값을 가져옵니다.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            public BigInteger getSize() {
                return size;
            }

            /**
             * size 속성의 값을 설정합니다.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            public void setSize(BigInteger value) {
                this.size = value;
            }

            /**
             * uri 속성의 값을 가져옵니다.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getUri() {
                return uri;
            }

            /**
             * uri 속성의 값을 설정합니다.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setUri(String value) {
                this.uri = value;
            }

            /**
             * derefUri 속성의 값을 가져옵니다.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDerefUri() {
                return derefUri;
            }

            /**
             * derefUri 속성의 값을 설정합니다.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDerefUri(String value) {
                this.derefUri = value;
            }

            /**
             * digest 속성의 값을 가져옵니다.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDigest() {
                return digest;
            }

            /**
             * digest 속성의 값을 설정합니다.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDigest(String value) {
                this.digest = value;
            }

        }

    }

}
