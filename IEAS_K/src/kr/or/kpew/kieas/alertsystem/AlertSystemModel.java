package kr.or.kpew.kieas.alertsystem;

import kr.or.kpew.kieas.common.AlertSystemProfile;
import kr.or.kpew.kieas.common.IKieasMessageBuilder;
import kr.or.kpew.kieas.common.IntegratedEmergencyAlertSystem;
import kr.or.kpew.kieas.common.KieasConfiguration.KieasAddress;
import kr.or.kpew.kieas.network.IClientTransmitter;
import kr.or.kpew.kieas.common.KieasMessageBuilder;
import kr.or.kpew.kieas.common.Profile;

public class AlertSystemModel extends IntegratedEmergencyAlertSystem {
	private AlertSystemProfile profile;
	
	IClientTransmitter transmitter;

	public static final String GEO_CODE = "GeoCode";
	public static final String ALERT_SYSTEM_TYPE = "AlertSystemType";

	public static final long DELAY = 1000;

	public AlertSystemModel(IClientTransmitter transmitter, AlertSystemProfile profile) {
		super(profile);

		this.transmitter = transmitter;
		transmitter.setOnMessageHandler(this);
	}

	public void init() {

		transmitter.init(profile.getSender(), KieasAddress.GATEWAY_ID);
		setChanged();
		notifyObservers(profile);
	}

	@Override
	public void onMessage(String sender, String message)
	{
		IKieasMessageBuilder builder = new KieasMessageBuilder();
		try {
			builder.parse(message);
			
			switch (builder.getStatus()) {
			case ACTUAL:
			case EXERCISE:
				sendAcknowledge(builder, KieasAddress.ALERTSYSTEM_TO_GATEWAY_QUEUE_DESTINATION);
				setChanged();
				notifyObservers(message);
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

	
	protected void sendAcknowledge(IKieasMessageBuilder receivedMessageBuilder, String destination)
	{
		IKieasMessageBuilder ack = receivedMessageBuilder.createAckMessage(createMessageId(), profile.getSender(), destination);
		
		try
		{
			Thread.sleep(DELAY);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		transmitter.send(ack.build());

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
