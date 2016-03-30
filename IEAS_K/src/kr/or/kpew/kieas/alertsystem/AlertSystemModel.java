package kr.or.kpew.kieas.alertsystem;

import kr.or.kpew.kieas.common.AlertSystemProfile;
import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.IntegratedEmergencyAlertSystem;
import kr.or.kpew.kieas.common.KieasConfiguration.KieasAddress;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.common.Profile;
import kr.or.kpew.kieas.network.ITransmitter;

public class AlertSystemModel extends IntegratedEmergencyAlertSystem {
	private AlertSystemProfile profile;
	
	ITransmitter transmitter;

	public static final String GEO_CODE = "GeoCode";
	public static final String ALERT_SYSTEM_TYPE = "AlertSystemType";

	public static final long DELAY = 1000;

	public AlertSystemModel(ITransmitter transmitter, AlertSystemProfile profile) {
		super(profile);

		this.transmitter = transmitter;
		transmitter.setOnMessageHandler(this);
	}

	public void init() {

		transmitter.init(profile.getSender());
		setChanged();
		notifyObservers(profile);
	}

	@Override
	public void onMessage(String message)
	{
		System.out.println(message);
		IKieasMessageBuilder builder = new KieasMessageBuilder();
		try
		{
			builder.parse(message);
			
			switch (builder.getStatus())
			{
			case ACTUAL:
				sendAcknowledge(message, KieasAddress.GATEWAY_ID);
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
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	protected void sendAcknowledge(String message, String destination)
	{
		IKieasMessageBuilder kieasMessageBuilder = new KieasMessageBuilder();
		String ackMessage = kieasMessageBuilder.createAckMessage(createMessageId(), profile.getSender(), destination);
		
		try
		{
			Thread.sleep(DELAY);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		System.out.println("AS: " + profile.getSender() + "send message to : " + destination);
		transmitter.sendTo(KieasAddress.GATEWAY_ID, ackMessage);
	}

	public void readyForExit() {
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
		notifyObservers("register succeed");
		
	}

}
