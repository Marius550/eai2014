package de.java.web;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import de.java.domain.customer.Customer;
import de.java.ejb.CustomerService;
import de.java.domain.prescription.Prescription;

@ManagedBean
public class CustomerList implements Serializable {

  private static final long serialVersionUID = 5212289925893596467L;

  @EJB
  private CustomerService customerService;
  
  private Map<Long, Prescription> customerPrescriptions;
  
//  private Customer customer;

  public Collection<Customer> getCustomers() {
    return customerService.getAllCustomers();
  }
  
  public int getCustomerPrescriptionsSize(Customer customer) {
	  return customerService.getCustomerWithPrescriptions(customer.getId()).getPrescriptions().size();
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
		  //System.out.println("Issuer: " + getAllCustomerPrescriptionsToHashMap(customer).get(p.getId()).getIssuer() + ", Cost: " 
		  //+ getAllCustomerPrescriptionsToHashMap(customer).get(p.getId()).getTotalPrice()  + ", i: " + i);
		  }
	  }
	  //System.out.println("TotalPrice: " + totalPrice);
	  return totalPrice;
	}

/** Important Method: Update the prescription bill in the customer list view by taking the customer Id as input parameter (see list.xhtml) to predefine the customer for whom
 * the prescription bill is determined
 * Call updatePrescriptionBill() and pass over the ID of the currently selected customer as well as his prescription bill determined by getCustomerPrescriptionBill()
 * @param id
 * @return Reroute to the given URI String
 */
public String submitUpdatePrescriptionBill(long id) {
	Customer customer = customerService.getCustomer(id);
    customer = customerService.updatePrescriptionBill(customer.getId(), getCustomerPrescriptionBill(customer));
    return "list.xhtml";
  }

}





//What is this supposed to do?
//public void setCustomer(Customer customer) {
//  this.customer = customer;
//}

//public void updatePrescriptionBill(long id) {
//  Customer customer = customerService.getCustomer(id);
//  customer.setPrescriptionBill(getCustomerPrescriptionBill(customer));
//}

//public Customer updatePrescriptionBill2(long id) {
//	  Customer customer = customerService.getCustomer(id);
//	  customer.setPrescriptionBill(getCustomerPrescriptionBill(customer));
//	  System.out.println("Customer: " + customer);
//	  return customer;
//	}

//public double testCurId(long id) {
//	Customer customer = customerService.getCustomer(id);
//	return customer.getPrescriptionBill();
//  }

//      <h:form>
//  	<h:commandButton value="Prescription Bill" action="#{customerList.getCustomerPrescriptionBill2(cur)}" />
//  </h:form>	
	  //<f:facet name="header">Prescription Bill</f:facet>
      //#{customerList.getCustomerPrescriptionBill2(cur)}

//public String submit() {
//    customer = customerService.updatePrescriptionBill(customer.getId(), customer.getTelephoneNumber(), customer.getAddress());
//    return "details.xhtml?faces-redirect=true&id=" + id;
//  }

//public double getTotalPrice() {
//    return totalPrice;
//  }
//
//  public void setTotalPrice(double totalPrice) {
//    this.totalPrice = totalPrice;
//  }

//  public void setTotalPrice(Customer customer) {
//	  //double totalPrice;
//	  totalPrice = getCustomerPrescriptionBill2(customer);
//	  System.out.println("TotalPrice: " + totalPrice);
//	  //this.totalPrice = totalPrice;
//  }
//  



