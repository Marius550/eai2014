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
	 * Creates a Message for submitting a drug
	 * @param drug the drug to create or update
	 * @param action either 'create' or 'update', depending on the demanded action
	 */
	public PrescriptionMessage(Date dateFrom, Date dateTo) {
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
	}
	
	protected Message buildMessage(Session session) throws JMSException {
		TextMessage message = session.createTextMessage();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		
		message.setStringProperty("dateFrom", sdf.format(dateFrom));
		message.setStringProperty("dateTo", sdf.format(dateTo));
		message.setStringProperty("action", "requestStatistics");
		message.setJMSReplyTo(session.createTemporaryQueue());
		
		return message;
	}

	@Override
	protected String getQueue() {
		return "/queue/PrescriptionStatisticsQueue04";
	}
}
