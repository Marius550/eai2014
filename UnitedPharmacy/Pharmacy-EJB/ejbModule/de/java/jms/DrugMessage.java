package de.java.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import de.java.domain.Drug;

public class DrugMessage extends AbstractMessage{
	private Drug drug;
	private String action;

	/**
	 * Creates a Message for submitting a drug
	 * @param drug the drug to create or update
	 * @param action either 'create' or 'update', depending on the demanded action
	 */
	public DrugMessage(Drug drug, String action) {
		this.drug = drug;
		this.action = action;
	}
	
	protected Message buildMessage(Session session) throws JMSException {
		MapMessage message = session.createMapMessage();
		
		message.setIntProperty("pzn", drug.getPzn());
		message.setStringProperty("name", drug.getName());
		message.setStringProperty("description", drug.getDescription());
		message.setStringProperty("action", this.action);
		
		return message;
	}
	
	protected String getQueue(){
		return "/queue/DrugQueue04";
	}
}
