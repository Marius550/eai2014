package de.java.ws;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.java.domain.customer.Customer;
import de.java.ejb.CustomerService;

@Stateless
public class CustomerResourceImpl implements CustomerResource {
	
	@EJB
	private CustomerService customerService;

	public Collection<MessageCustomer> getAllCustomers(){
		Collection<Customer> customers = customerService.getAllCustomers();

		Collection<MessageCustomer> collection = new ArrayList<MessageCustomer>();
		for (final Customer c : customers){
			collection.add(setupCustomer(c));
		}
		
		return collection;
	}
	
	public MessageCustomer getCustomer(long id){
		Customer customer = customerService.getCustomer(id);
		
		return setupCustomer(customer);
	}
	
	@Override
	public void createCustomer(MessageCustomer sCustomer){
		customerService.createCustomer(sCustomer.convertToCustomer());
	}
	
	@Override
	public void updateCustomer(long id, MessageCustomer sCustomer) {
		//drugService.updateMasterData(pzn, sDrug.getName(), sDrug.getPrice(), sDrug.getDescription(), sDrug.getDrugMinimumAgeYears());	 	
	System.out.println("updateCustomer");
	}
	
	@Override
	public void createCustomers(Collection<MessageCustomer> customers){
		for (MessageCustomer c : customers){
			customerService.createCustomer(c.convertToCustomer());
		}
	}
	
	/**
	 * Creates a message customer from a Customer object
	 * @param 
	 * @return
	 */
	private MessageCustomer setupCustomer(Customer c){
		// Sets up a message drug with everything but the pending quantity and unfulfilled quantity
		MessageCustomer tmpCustomer = new MessageCustomer(c);
		// Sets up the rest
		//tmpDrug.setPendingQuantity(replenishmentOrderService.getQuantityPendingForDrug(d.getPzn()));
		//tmpDrug.setUnfulfilledQuantity(prescriptionService.getQuantityUnfulfilledForDrug(d.getPzn()));
		return tmpCustomer;
	}

}
