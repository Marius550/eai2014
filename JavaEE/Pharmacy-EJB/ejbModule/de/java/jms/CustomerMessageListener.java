
package de.java.jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Message;

import de.java.domain.customer.Customer;
import de.java.ejb.CustomerService;
import de.java.domain.Drug;
import de.java.ejb.DrugService;;

/**
 * Message Driven Bean to handle customer creation and update from HQ.
 * Message is incoming at queue DrugQueue04. 
 * Parameters are id, name, address, birthDate, email, telephoneNumber, prescriptionBill and action either being "create" or "update"
 */

/*
@MessageDriven(activationConfig = {
	@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
	@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/DrugQueue04"),
})
public class CustomerMessageListener implements MessageListener {
	@EJB
	private CustomerService customerService;
	
	public void onMessage(Message message) {		
		try {
			String action = message.getStringProperty("action");
			
			long id = message.getLongProperty("id");
			String name = message.getStringProperty("name");
			String address = message.getStringProperty("address");
			String birthDate = message.getStringProperty("birthDate");
			String email = message.getStringProperty("email");
			String telephoneNumber = message.getStringProperty("telephoneNumber");
			double prescriptionBill = message.getDoubleProperty("prescriptionBill");

			if(action.equals("create")) {
				Customer newCustomer = new Customer();
				newCustomer.setId(id);
				newCustomer.setName(name);
				newCustomer.setAddress(address);
				newCustomer.setBirthDate(birthDate);
				newCustomer.setEmail(email);
				newCustomer.setTelephoneNumber(telephoneNumber);
				newCustomer.setPrescriptionBill(prescriptionBill);
				customerService.createCustomer(newCustomer);
			} else if(action.equals("update")) {
				System.out.println("updateMasterData");
				//customerService.updateMasterData(pzn, name, price, description, drugMinimumAgeYears);
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
*/
