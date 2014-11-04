package de.java.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import de.java.domain.Customer;

/**
 * Message for creating and updating a customer.
 */
public class CustomerMessage extends AbstractMessage{
	private Customer customer;
	//private Drug drug;
	private String action;

	/**
	 * Creates a Message for submitting a drug
	 * @param drug the drug to create or update
	 * @param action either 'create' or 'update', depending on the demanded action
	 */
	/*
	public CustomerMessage(Drug drug, String action) {
		this.drug = drug;
		this.action = action;
	}
	*/
	
	/*
	@Override
	protected Message buildMessage1(Session session) throws JMSException {
		MapMessage message = session.createMapMessage();
		
		message.setIntProperty("pzn", drug.getPzn());
		message.setStringProperty("name", drug.getName());
		message.setDoubleProperty("price", drug.getPrice());
		message.setStringProperty("description", drug.getDescription());
		message.setLongProperty("drugMinimumAgeYears", drug.getDrugMinimumAgeYears());
		message.setStringProperty("action", this.action);
		
		return message;
	}
	*/
	
	@Override
	protected Message buildMessage(Session session) throws JMSException {
		MapMessage message = session.createMapMessage();
		
		message.setLongProperty("id", customer.getId());
		message.setStringProperty("name", customer.getName());
		message.setStringProperty("address", customer.getAddress());
		message.setStringProperty("birthDate", customer.getBirthDate());
		message.setStringProperty("email", customer.getEmail());
		message.setStringProperty("telephoneNumber", customer.getTelephoneNumber());
		message.setDoubleProperty("prescriptionBill", customer.getPrescriptionBill());
		
		message.setStringProperty("action", this.action);
		
		return message;
	}
	
	@Override
	protected String getQueue(){
		return "/queue/CustomerQueue04";
	}
}
