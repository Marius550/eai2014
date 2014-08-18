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

/**
 * Message driven Bean for prescription statistics
 * Messages are coming in at queue PrescriptionStatisticsQueue04.
 * Parameters are dateFrom ("yyyy-MM-dd"), dateTo ("yyyy-MM-dd"), and JMSReplyTo as temporary reply queue.
 */
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
			// Date parsing
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dateFrom = sdf.parse(m.getStringProperty("dateFrom"));
			Date dateTo = sdf.parse(m.getStringProperty("dateTo"));
			
			// Prepare connection for reply
			conn = cf.createConnection();
			conn.start();
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination replyTo = m.getJMSReplyTo();
			MessageProducer producer = session.createProducer(replyTo);
			MapMessage reply = session.createMapMessage();
			
			// Let values calculate in EJB
			int totalNumberOfPrescriptions = prescriptionService.getNumberPrescriptionsBetweenDates(dateFrom, dateTo);
			double averageNumberOfItemsPerPrescription = prescriptionService.getAvItemNumberBetweenDates(dateFrom, dateTo);
			double averageTotalpricePerPrescription = prescriptionService.getAvTotalPriceBetweenDates(dateFrom, dateTo);
			long averageTimeSpanOfFulfilment = prescriptionService.getAvFulfillmentTimesBetweenDates(dateFrom, dateTo);
			
			// Prepare reply
			reply.setIntProperty("totalNumberOfPrescriptions",totalNumberOfPrescriptions);
			reply.setDoubleProperty("averageNumberOfItemsPerPrescription", averageNumberOfItemsPerPrescription);
			reply.setDoubleProperty("averageTotalpricePerPrescription", averageTotalpricePerPrescription);
			reply.setLongProperty("averageTimeSpanOfFulfilment",averageTimeSpanOfFulfilment);

			// Send reply
			producer.send(reply);
		} 
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
