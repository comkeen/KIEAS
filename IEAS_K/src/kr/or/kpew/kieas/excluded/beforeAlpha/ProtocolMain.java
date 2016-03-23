package kr.or.kpew.kieas.protocol;


public class ProtocolMain {
	public static void main(String[] args) throws InterruptedException {

		GatewayTransmitterTcp gateway = new GatewayTransmitterTcp();
		Thread g = new Thread(gateway);
		g.start();
		
		Thread.sleep(100);
		
		AlertSystemTransmitterTcp alertsystem = new AlertSystemTransmitterTcp();
		Thread a = new Thread(alertsystem);
		a.start();
		
		Thread.sleep(100);
		
		AlertSystemTransmitterTcp alertsystem2 = new AlertSystemTransmitterTcp();
		Thread a2 = new Thread(alertsystem2);
		a2.start();
		
		Thread.sleep(100);		
		
		AlertSystemTransmitterTcp alertsystem3 = new AlertSystemTransmitterTcp();
		Thread a3 = new Thread(alertsystem3);
		a3.start();
		
		Thread.sleep(100);
		
		IssuerTransmitterTcp issuer = new IssuerTransmitterTcp();
		Thread i = new Thread(issuer);
		i.start();
		
		Thread.sleep(100);
		
		i.join();
		a.join();
		a2.join();
		a3.join();
		g.join();
		
		System.out.println("all finished.");

	}
}
