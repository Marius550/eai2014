package de.java.web;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import de.java.domain.Customer;
import de.java.ejb.CustomerService;
import de.java.ws.MessageCustomer;

@ManagedBean
public class CustomerList implements Serializable {
  private static final long serialVersionUID = 9000977856559982072L;

  @EJB
  private CustomerService customerService;

  private String searchTermCustomer;
  // All customers at JaVa
  private Map<Long, MessageCustomer> jCustomers;
  // All customers at C. Sharpe
  private Map<Long, MessageCustomer> cCustomers;
  
  
  public Collection<Customer> getCustomers() {
	  return customerService.getAllCustomersLike(searchTermCustomer);
  }
  
  /*
  public Collection<MessageCustomer> invokeInitDatabaseCollectionOnly() {
	  return customerService.initDatabaseCollectionOnly(getjCustomers(), getcCustomers());
  }
  */
  
  /*
  public int getAmountOfPharmacyHOCustomersCollection(){
	  return invokeInitDatabaseCollectionOnly().size();
  } 
  */
  
  public int getAmountOfCustomersInJava() {
	  return getjCustomers().size();
  }
  
  public int getAmountOfCustomersInDotNet() {
	  return getcCustomers().size();
  }
  
  public int getAmountOfDuplicateCustomers() { 
	  return customerService.getAmountOfDuplicateCustomers(getjCustomers(), getcCustomers());
  }
  
  
  /**
   * Gets the amount for all currently displayed customers (i.e. search is considered)
   * @return amount of all displayed customers at C.Sharpe and JaVa
   */
  public int getAmountOfPharmacyHOCustomersEntityManager(){

	  int javaCustomers = 0;
	  int cCustomers = 0;
	  
	  for (Customer c : getCustomers()){
	  // Iterate only through displayed drugs in order to display right amount

		 getjCustomers().get(c.getId());
		 javaCustomers = javaCustomers + 1;
			  
		 getcCustomers().get(c.getId());
		 cCustomers = cCustomers + 1;		  
	  }
	  //System.out.println("javaDrugs: " + javaDrugs + ", cDrugs: " + cDrugs);
	  int totalAmount = (javaCustomers + cCustomers)/2;
	  return totalAmount;
  }
  
  public Map<Long, MessageCustomer> getjCustomers(){
	  if (jCustomers == null){
		  jCustomers = customerService.getAllCustomersFromJava();
	  }
	  return jCustomers;
  }
  
  public Map<Long, MessageCustomer> getcCustomers(){
	  if (cCustomers == null){
		  cCustomers = customerService.getAllCustomersFromDotNet();
	  }
	  return cCustomers;
  }
  
  /**
   * Returns a specific customer from the Database of JaVa
   * @param id
   * @return MessageCustomer-object, representing the customer at JaVa
   */
  public MessageCustomer getjCustomer(long id){
	  System.out.println(getjCustomers().get(id).getName());
	  return getjCustomers().get(id);
  }
  
  /**
   * Returns a specific customer from the Database of C.Sharpe
   * @param id
   * @return MessageCustomer-object, representing the customer at JaVa
   */
  public MessageCustomer getcCustomer(long id){
	  System.out.println(getcCustomers().get(id).getName());
	  return getcCustomers().get(id);
  }
  
  public String getSearchTermCustomer() {
	    return searchTermCustomer;
	  }

  public void setSearchTermCustomer(String searchTermCustomer) {
	    this.searchTermCustomer = searchTermCustomer;
	  }
  
  public String refresh() {
	  return "/customer/list.xhtml";
	  }

  public String search() {
	    return "/customer/list.xhtml?faces-redirect=true&search=" + searchTermCustomer;
	  }
  
  public boolean isInitialized(){
	  return !customerService.getAllCustomers().isEmpty();
  }

}
