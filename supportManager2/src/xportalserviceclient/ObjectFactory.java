
package xportalserviceclient;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the xportalserviceclient package. 
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

    private final static QName _CardBlockUnBlockCardnum_QNAME = new QName("http://xportal.etranzact.com/", "cardBlock_UnBlock_cardnum");
    private final static QName _PinResetRequestCardnumResponse_QNAME = new QName("http://xportal.etranzact.com/", "pinResetRequest_cardnumResponse");
    private final static QName _GetCardHolderDetailsMobile_QNAME = new QName("http://xportal.etranzact.com/", "getCardHolderDetails_mobile");
    private final static QName _GetCardHolderDetailsCardnum_QNAME = new QName("http://xportal.etranzact.com/", "getCardHolderDetails_cardnum");
    private final static QName _CardBlockUnBlockCardnumResponse_QNAME = new QName("http://xportal.etranzact.com/", "cardBlock_UnBlock_cardnumResponse");
    private final static QName _GetCardHolderDetailsCardnumResponse_QNAME = new QName("http://xportal.etranzact.com/", "getCardHolderDetails_cardnumResponse");
    private final static QName _CardBlockUnBlockMobileResponse_QNAME = new QName("http://xportal.etranzact.com/", "cardBlock_UnBlock_mobileResponse");
    private final static QName _GetCardHolderDetailsMobileResponse_QNAME = new QName("http://xportal.etranzact.com/", "getCardHolderDetails_mobileResponse");
    private final static QName _PinResetRequestMobileResponse_QNAME = new QName("http://xportal.etranzact.com/", "pinResetRequest_mobileResponse");
    private final static QName _CardBlockUnBlockMobile_QNAME = new QName("http://xportal.etranzact.com/", "cardBlock_UnBlock_mobile");
    private final static QName _Trial_QNAME = new QName("http://xportal.etranzact.com/", "trial");
    private final static QName _PinResetRequestCardnum_QNAME = new QName("http://xportal.etranzact.com/", "pinResetRequest_cardnum");
    private final static QName _TrialResponse_QNAME = new QName("http://xportal.etranzact.com/", "trialResponse");
    private final static QName _PinResetRequestMobile_QNAME = new QName("http://xportal.etranzact.com/", "pinResetRequest_mobile");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: xportalserviceclient
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PinResetRequestCardnumResponse }
     * 
     */
    public PinResetRequestCardnumResponse createPinResetRequestCardnumResponse() {
        return new PinResetRequestCardnumResponse();
    }

    /**
     * Create an instance of {@link CardBlockUnBlockCardnum }
     * 
     */
    public CardBlockUnBlockCardnum createCardBlockUnBlockCardnum() {
        return new CardBlockUnBlockCardnum();
    }

    /**
     * Create an instance of {@link GetCardHolderDetailsCardnumResponse }
     * 
     */
    public GetCardHolderDetailsCardnumResponse createGetCardHolderDetailsCardnumResponse() {
        return new GetCardHolderDetailsCardnumResponse();
    }

    /**
     * Create an instance of {@link Trial }
     * 
     */
    public Trial createTrial() {
        return new Trial();
    }

    /**
     * Create an instance of {@link CardBlockUnBlockCardnumResponse }
     * 
     */
    public CardBlockUnBlockCardnumResponse createCardBlockUnBlockCardnumResponse() {
        return new CardBlockUnBlockCardnumResponse();
    }

    /**
     * Create an instance of {@link GetCardHolderDetailsMobile }
     * 
     */
    public GetCardHolderDetailsMobile createGetCardHolderDetailsMobile() {
        return new GetCardHolderDetailsMobile();
    }

    /**
     * Create an instance of {@link GetCardHolderDetailsMobileResponse }
     * 
     */
    public GetCardHolderDetailsMobileResponse createGetCardHolderDetailsMobileResponse() {
        return new GetCardHolderDetailsMobileResponse();
    }

    /**
     * Create an instance of {@link CardBlockUnBlockMobileResponse }
     * 
     */
    public CardBlockUnBlockMobileResponse createCardBlockUnBlockMobileResponse() {
        return new CardBlockUnBlockMobileResponse();
    }

    /**
     * Create an instance of {@link PinResetRequestCardnum }
     * 
     */
    public PinResetRequestCardnum createPinResetRequestCardnum() {
        return new PinResetRequestCardnum();
    }

    /**
     * Create an instance of {@link PinResetRequestMobile }
     * 
     */
    public PinResetRequestMobile createPinResetRequestMobile() {
        return new PinResetRequestMobile();
    }

    /**
     * Create an instance of {@link CardBlockUnBlockMobile }
     * 
     */
    public CardBlockUnBlockMobile createCardBlockUnBlockMobile() {
        return new CardBlockUnBlockMobile();
    }

    /**
     * Create an instance of {@link PinResetRequestMobileResponse }
     * 
     */
    public PinResetRequestMobileResponse createPinResetRequestMobileResponse() {
        return new PinResetRequestMobileResponse();
    }

    /**
     * Create an instance of {@link TrialResponse }
     * 
     */
    public TrialResponse createTrialResponse() {
        return new TrialResponse();
    }

    /**
     * Create an instance of {@link GetCardHolderDetailsCardnum }
     * 
     */
    public GetCardHolderDetailsCardnum createGetCardHolderDetailsCardnum() {
        return new GetCardHolderDetailsCardnum();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CardBlockUnBlockCardnum }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xportal.etranzact.com/", name = "cardBlock_UnBlock_cardnum")
    public JAXBElement<CardBlockUnBlockCardnum> createCardBlockUnBlockCardnum(CardBlockUnBlockCardnum value) {
        return new JAXBElement<CardBlockUnBlockCardnum>(_CardBlockUnBlockCardnum_QNAME, CardBlockUnBlockCardnum.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PinResetRequestCardnumResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xportal.etranzact.com/", name = "pinResetRequest_cardnumResponse")
    public JAXBElement<PinResetRequestCardnumResponse> createPinResetRequestCardnumResponse(PinResetRequestCardnumResponse value) {
        return new JAXBElement<PinResetRequestCardnumResponse>(_PinResetRequestCardnumResponse_QNAME, PinResetRequestCardnumResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCardHolderDetailsMobile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xportal.etranzact.com/", name = "getCardHolderDetails_mobile")
    public JAXBElement<GetCardHolderDetailsMobile> createGetCardHolderDetailsMobile(GetCardHolderDetailsMobile value) {
        return new JAXBElement<GetCardHolderDetailsMobile>(_GetCardHolderDetailsMobile_QNAME, GetCardHolderDetailsMobile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCardHolderDetailsCardnum }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xportal.etranzact.com/", name = "getCardHolderDetails_cardnum")
    public JAXBElement<GetCardHolderDetailsCardnum> createGetCardHolderDetailsCardnum(GetCardHolderDetailsCardnum value) {
        return new JAXBElement<GetCardHolderDetailsCardnum>(_GetCardHolderDetailsCardnum_QNAME, GetCardHolderDetailsCardnum.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CardBlockUnBlockCardnumResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xportal.etranzact.com/", name = "cardBlock_UnBlock_cardnumResponse")
    public JAXBElement<CardBlockUnBlockCardnumResponse> createCardBlockUnBlockCardnumResponse(CardBlockUnBlockCardnumResponse value) {
        return new JAXBElement<CardBlockUnBlockCardnumResponse>(_CardBlockUnBlockCardnumResponse_QNAME, CardBlockUnBlockCardnumResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCardHolderDetailsCardnumResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xportal.etranzact.com/", name = "getCardHolderDetails_cardnumResponse")
    public JAXBElement<GetCardHolderDetailsCardnumResponse> createGetCardHolderDetailsCardnumResponse(GetCardHolderDetailsCardnumResponse value) {
        return new JAXBElement<GetCardHolderDetailsCardnumResponse>(_GetCardHolderDetailsCardnumResponse_QNAME, GetCardHolderDetailsCardnumResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CardBlockUnBlockMobileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xportal.etranzact.com/", name = "cardBlock_UnBlock_mobileResponse")
    public JAXBElement<CardBlockUnBlockMobileResponse> createCardBlockUnBlockMobileResponse(CardBlockUnBlockMobileResponse value) {
        return new JAXBElement<CardBlockUnBlockMobileResponse>(_CardBlockUnBlockMobileResponse_QNAME, CardBlockUnBlockMobileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCardHolderDetailsMobileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xportal.etranzact.com/", name = "getCardHolderDetails_mobileResponse")
    public JAXBElement<GetCardHolderDetailsMobileResponse> createGetCardHolderDetailsMobileResponse(GetCardHolderDetailsMobileResponse value) {
        return new JAXBElement<GetCardHolderDetailsMobileResponse>(_GetCardHolderDetailsMobileResponse_QNAME, GetCardHolderDetailsMobileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PinResetRequestMobileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xportal.etranzact.com/", name = "pinResetRequest_mobileResponse")
    public JAXBElement<PinResetRequestMobileResponse> createPinResetRequestMobileResponse(PinResetRequestMobileResponse value) {
        return new JAXBElement<PinResetRequestMobileResponse>(_PinResetRequestMobileResponse_QNAME, PinResetRequestMobileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CardBlockUnBlockMobile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xportal.etranzact.com/", name = "cardBlock_UnBlock_mobile")
    public JAXBElement<CardBlockUnBlockMobile> createCardBlockUnBlockMobile(CardBlockUnBlockMobile value) {
        return new JAXBElement<CardBlockUnBlockMobile>(_CardBlockUnBlockMobile_QNAME, CardBlockUnBlockMobile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Trial }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xportal.etranzact.com/", name = "trial")
    public JAXBElement<Trial> createTrial(Trial value) {
        return new JAXBElement<Trial>(_Trial_QNAME, Trial.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PinResetRequestCardnum }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xportal.etranzact.com/", name = "pinResetRequest_cardnum")
    public JAXBElement<PinResetRequestCardnum> createPinResetRequestCardnum(PinResetRequestCardnum value) {
        return new JAXBElement<PinResetRequestCardnum>(_PinResetRequestCardnum_QNAME, PinResetRequestCardnum.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TrialResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xportal.etranzact.com/", name = "trialResponse")
    public JAXBElement<TrialResponse> createTrialResponse(TrialResponse value) {
        return new JAXBElement<TrialResponse>(_TrialResponse_QNAME, TrialResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PinResetRequestMobile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xportal.etranzact.com/", name = "pinResetRequest_mobile")
    public JAXBElement<PinResetRequestMobile> createPinResetRequestMobile(PinResetRequestMobile value) {
        return new JAXBElement<PinResetRequestMobile>(_PinResetRequestMobile_QNAME, PinResetRequestMobile.class, null, value);
    }

}
