package de.java.jms;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.MapMessage;
import javax.jms.Session;
import javax.jms.MessageListener;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.Connection;
import javax.jms.MessageProducer;

import de.java.ejb.PrescriptionService;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/PrescriptionStatisticsQueue04")})
public class PrescriptionStatisticsListener implements MessageListener {
	@Resource(lookup = "java:/JmsXA")
	private ConnectionFactory cf;
	
	@EJB
	private PrescriptionService prescriptionService;
	
	public void onMessage(Message m) {
		Connection conn = null;

		try {
			System.out.println("Prescription-Server: Message received");
		
			// Date parsing
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
			Date dateFrom = sdf.parse(m.getStringProperty("dateFrom"));
			Date dateTo = sdf.parse(m.getStringProperty("dateTo"));
			
			// Prepare connection for reply
			conn = cf.createConnection();
			conn.start();
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination replyTo = m.getJMSReplyTo();
			MessageProducer producer = session.createProducer(replyTo);
			MapMessage reply = session.createMapMessage();
			
			// TODO: Get required numbers in timespan from prescriptionService
			// Dummy return value:
			int totalNumberOfPrescriptions = 42;
			reply.setIntProperty("totalNumberOfPrescriptions",totalNumberOfPrescriptions);

			// Send reply
			producer.send(reply);
			System.out.println("Answer sent.");
		} 
		catch (Exception e) {e.printStackTrace();} 
		finally {
			if (conn != null) {
				try {conn.close();} 
				catch (Exception e) {e.printStackTrace();}}}}
}
