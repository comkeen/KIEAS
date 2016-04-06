package kr.or.kpew.kieas.alertsystem;

import kr.or.kpew.kieas.common.AlertSystemProfile;
import kr.or.kpew.kieas.common.AlertValidator;
import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.IntegratedEmergencyAlertSystem;
import kr.or.kpew.kieas.common.KieasConfiguration.KieasAddress;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.common.Profile;
import kr.or.kpew.kieas.network.ITransmitter;

public class AlertSystemModel extends IntegratedEmergencyAlertSystem {
	private AlertSystemProfile profile;

	ITransmitter transmitter;

	private AlertValidator alertValidator;

	public static final String GEO_CODE = "GeoCode";
	public static final String ALERT_SYSTEM_TYPE = "AlertSystemType";

	public static final long DELAY = 1000;


	public AlertSystemModel(ITransmitter transmitter, AlertSystemProfile profile)
	{
		super(profile);

		this.alertValidator = new AlertValidator();
		this.transmitter = transmitter;
		transmitter.setOnMessageHandler(this);
	}

	public void init()
	{
		transmitter.init(profile.getSender());
		setChanged();
		notifyObservers(profile);
	}

	@Override
	public void onMessage(String message)
	{
		boolean[] validationResults = alertValidator.fullValidationMessage(message);
		if(!validationResults[0])
		{
			System.out.println("AS: Duplicated Alert");
			return;
		}
		else if(!validationResults[5])
		{
			System.out.println("AS: Invalid target AS GeoCode");
			return;
		}
		else
		{
			onNewMessage(message, validationResults);			
		}
	}

	private void onNewMessage(String message, boolean[] validationResults)
	{
		IKieasMessageBuilder kieasMessageBuilder = new KieasMessageBuilder();
		kieasMessageBuilder.parse(message);
		
		switch (kieasMessageBuilder.getStatus())
		{
		case ACTUAL:
			sendAcknowledge(message, validationResults);
			setChanged();
			notifyObservers(message);
			break;
		case EXERCISE:
			break;
		case SYSTEM:
			break;
		default:
			break;
		}
	}

	protected void sendAcknowledge(String message, boolean[] validationResults)
	{
		IKieasMessageBuilder kieasMessageBuilder = new KieasMessageBuilder();
		String ackMessage = kieasMessageBuilder.createAckMessage(message, createMessageId(), profile.getSender(), alertValidator.getAckCode(validationResults));

		// String ackMessage = kieasMessageBuilder.createAckMessage("YoonKwan_babo", 0, "JungEunKim", null);
		try
		{
			Thread.sleep(DELAY);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		System.out.println("AS: " + profile.getSender() + " Send Ack to GW ");
		transmitter.sendTo(KieasAddress.GATEWAY_ID, ackMessage);
	}

	public void close() {
		transmitter.close();
	}

	@Override
	public void setProfile(Profile profile) {
		this.profile = (AlertSystemProfile)profile;
	}

	@Override
	public Profile getProfile() {
		return profile;
	}

	@Override
	public void onRegister(String sender, String address) {
		setChanged();
		notifyObservers("Register Succeed");

	}

}
