package de.java.web;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import de.java.domain.customer.Customer;
import de.java.domain.prescription.Item;
import de.java.ejb.CustomerService;
import de.java.domain.prescription.Prescription;

@ManagedBean
public class CustomerList implements Serializable {

  private static final long serialVersionUID = 5212289925893596467L;

  @EJB
  private CustomerService customerService;
  
  private Map<Long, Prescription> customerPrescriptions;

  public Collection<Customer> getCustomers() {
    return customerService.getAllCustomers();
  }
  
  public int getCustomerPrescriptionsSize(Customer customer) {
	  return customerService.getCustomerWithPrescriptions(customer.getId()).getPrescriptions().size();
  }
  
  /**
   * Convert Collection <Item> into HashMap and assign id to each map element
 * @param collection
 * @return Map<Long, Item> map
 */
private Map<Long, Prescription> convertPharmacyCustomerPrescriptionsCollectionIntoHashMap(Collection<Prescription> collection){
	  Map<Long, Prescription> map = new HashMap<Long, Prescription>();
	  for (Prescription mPrescription : collection){
		  map.put(mPrescription.getId(), mPrescription);
	  }
	  return map;
  }

public Map<Long, Prescription> getAllCustomerPrescriptionsToHashMap(Customer customer){
	  if (customerPrescriptions == null){
		  customerPrescriptions = convertPharmacyCustomerPrescriptionsCollectionIntoHashMap(customerService.getCustomerWithPrescriptions(customer.getId()).getPrescriptions());
	  }
	  return customerPrescriptions;
}

public double getCustomerPrescriptionBill(Customer customer){
	  double totalPrice = 0;
	  for (Prescription p : customerService.getCustomerWithPrescriptions(customer.getId()).getPrescriptions()){
		  // Iterate only through displayed items in order to display right sums
		  if (getAllCustomerPrescriptionsToHashMap(customer).get(p.getId()) != null) {
			  System.out.println("test: " + getAllCustomerPrescriptionsToHashMap(customer).get(p.getId()).getIssuer());
			  totalPrice += getAllCustomerPrescriptionsToHashMap(customer).get(p.getId()).getTotalPrice();
		  }
	  }
	  return totalPrice;
}

public double getCustomerPrescriptionBill2(Customer customer){
	double totalPrice = 0;
	  for (Prescription p : customerService.getCustomerWithPrescriptions(customer.getId()).getPrescriptions()){
		  totalPrice = totalPrice + getAllCustomerPrescriptionsToHashMap(customer).get(p.getId()).getTotalPrice(); 
//			  System.out.println("Issuer: " + getAllCustomerPrescriptionsToHashMap(customer).get(p.getId()).getIssuer() 
//					  		   + ", Cost: " + getAllCustomerPrescriptionsToHashMap(customer).get(p.getId()).getTotalPrice());
	  }
	  return totalPrice;
	  //<h:commandButton value="Prescription Bill" action="#{customerList.getCustomerPrescriptionBill2(cur)}" />
}

public void test(Customer customer) {
	System.out.println("getCustomerPrescriptionBill: " + getCustomerPrescriptionBill(customer));
}

}
