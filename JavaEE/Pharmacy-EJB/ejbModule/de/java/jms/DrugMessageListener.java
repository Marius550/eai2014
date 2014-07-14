package de.java.jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Message;

import de.java.domain.Drug;
import de.java.ejb.DrugService;;

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

			if(action.equals("create")) {
				Drug newDrug = new Drug();
				newDrug.setPzn(pzn);
				newDrug.setDescription(description);
				newDrug.setPrice(price);
				newDrug.setName(name);
				drugService.createDrug(newDrug);
			} else if(action.equals("update")) {
				drugService.updateMasterData(pzn, name, price, description);
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
