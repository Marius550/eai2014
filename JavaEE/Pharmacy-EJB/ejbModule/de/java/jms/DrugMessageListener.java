package de.java.jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Message;

import de.java.domain.Drug;
import de.java.ejb.DrugService;;

/**
 * Message Driven Bean to handle drug creation and update from HQ.
 * Message is incoming at queue DrugQueue04. 
 * Parameters are pzn, name, description, and action either being "create" or "update"
 */
@MessageDriven(activationConfig = {
	@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
	@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/DrugQueue04"),
})
public class DrugMessageListener implements MessageListener {
	@EJB
	private DrugService drugService;

	
	public void onMessage(Message message) {		
		try {
			String action = message.getStringProperty("action");
			
			int pzn = message.getIntProperty("pzn");
			String name = message.getStringProperty("name");
			double price = message.getDoubleProperty("price");
			String description = message.getStringProperty("description");
			long drugMinimumAgeYears = message.getLongProperty("drugMinimumAgeYears");

			if(action.equals("create")) {
				Drug newDrug = new Drug();
				newDrug.setPzn(pzn);
				newDrug.setPrice(price);
				newDrug.setDescription(description);
				newDrug.setName(name);
				newDrug.setDrugMinimumAgeYears(drugMinimumAgeYears);
				drugService.createDrug(newDrug);
			} else if(action.equals("update")) {
				drugService.updateMasterData(pzn, name, price, description, drugMinimumAgeYears);
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
