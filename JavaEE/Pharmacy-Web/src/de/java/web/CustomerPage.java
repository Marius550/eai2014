package de.java.web;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import de.java.domain.customer.Customer;
import de.java.domain.prescription.Prescription;
import de.java.ejb.CustomerService;
import de.java.web.util.Util;

@ManagedBean
@ViewScoped
public class CustomerPage implements Serializable {

  private static final long serialVersionUID = 3577839317048078008L;

  @EJB
  private CustomerService customerService;
  
  private CustomerList customerList;

  private long id;
  private Customer customer;
  
  private Map<Long, Prescription> customerPrescriptions;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
    init();
  }

  private void init() {
    customer = null;
  }

  public void ensureInitialized(){
    try{
      if(getCustomer() != null)
        // Success
        return;
    } catch(EJBException e) {
      e.printStackTrace();
    }
    Util.redirectToRoot();
  }

  public String submit() {
    customer = customerService.update(customer.getId(), customer.getTelephoneNumber(), customer.getAddress());
    return "details.xhtml?faces-redirect=true&id=" + id;
  }

  public Customer getCustomer() {
    if (customer == null) {
      customer = customerService.getCustomerWithPrescriptions(id);
    }
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }
  
  /**
   * Convert Collection <Prescription> into HashMap and assign id to each map element
 * @param collection
 * @return Map<Long, Prescription> map
 */
private Map<Long, Prescription> convertPharmacyCustomerPrescriptionsCollectionIntoHashMap(Collection<Prescription> collection){
	  Map<Long, Prescription> map = new HashMap<Long, Prescription>();
	  for (Prescription mPrescription : collection){
		  map.put(mPrescription.getId(), mPrescription);
	  }
	  return map;
	}

/** Call the CustomerWithPrescriptions collection for a specific customer
 * @param customer
 * @return Map<Long, Prescription> based on the CustomerWithPrescriptions collection
 */
public Map<Long, Prescription> getAllCustomerPrescriptionsToHashMap(Customer customer){
	  if (customerPrescriptions == null){
		  customerPrescriptions = convertPharmacyCustomerPrescriptionsCollectionIntoHashMap(customerService.getCustomerWithPrescriptions(customer.getId()).getPrescriptions());
	  }
	  return customerPrescriptions;
}

/** Iterate over the CustomerWithPrescriptions HashMap and only sum up the total price of prescriptions of the specific customer
 * @param customer
 * @return Summed up totalPrice
 */
public double getCustomerPrescriptionBill(Customer customer){
	double totalPrice = 0;
	  for (Prescription p : customerService.getCustomerWithPrescriptions(customer.getId()).getPrescriptions()){
		  if (getAllCustomerPrescriptionsToHashMap(customer).get(p.getId()) != null) {
		  totalPrice = totalPrice + getAllCustomerPrescriptionsToHashMap(customer).get(p.getId()).getTotalPrice();  
		  }
	  }
	  return totalPrice;
	}

public double getCustomerPrescriptionSum(long id) {
	  Customer customer = customerService.getCustomer(id);
	  return getCustomerPrescriptionBill(customer);
	}

}
