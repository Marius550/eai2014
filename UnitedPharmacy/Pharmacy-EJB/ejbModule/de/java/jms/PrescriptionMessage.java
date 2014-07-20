package de.java.jms;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

public class PrescriptionMessage extends AbstractMessage{
	private Date dateFrom;
	private Date dateTo;

	/**
	 * Creates a message that requests prescriptions statistics
	 * @param dateFrom Date to request statistics from
	 * @param dateTo Date to request statistics to
	 */
	public PrescriptionMessage(Date dateFrom, Date dateTo) {
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
	}
	
	/**
	 * Builds up the prescription message
	 */
	@Override
	protected Message buildMessage(Session session) throws JMSException {
		TextMessage message = session.createTextMessage();
		
		// Create strings in format yyyy-DD-dd from Dates
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		message.setStringProperty("dateFrom", sdf.format(dateFrom));
		message.setStringProperty("dateTo", sdf.format(dateTo));
		message.setStringProperty("action", "requestStatistics");
		// Create temporary queue for reply
		message.setJMSReplyTo(session.createTemporaryQueue());
		
		return message;
	}

	@Override
	protected String getQueue() {
		return "/queue/PrescriptionStatisticsQueue04";
	}
}
