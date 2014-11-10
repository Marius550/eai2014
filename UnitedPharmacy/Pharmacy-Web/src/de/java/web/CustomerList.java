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

  private String searchTerm;
  // All drugs at JaVa
  private Map<Long, MessageCustomer> jCustomers;
  // All drugs at C. Sharpe
  private Map<Long, MessageCustomer> cCustomers;
  
  /*
  public Collection<Customer> getCustomers() {
    return drugService.getAllDrugsLike(searchTerm);
  }
  */
  
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

}
