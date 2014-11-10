package de.java.web;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import de.java.ejb.CustomerService;
import de.java.ws.MessageCustomer;

@ManagedBean
public class InitCustomers implements Serializable {
  private static final long serialVersionUID = -2902383377336267606L;
  
  // All customers at JaVa
  private Map<Long, MessageCustomer> jCustomers;
  // All customers at C. Sharpe
  private Map<Long, MessageCustomer> cCustomers;
  // All maps in the HO-database, after running the init script
  private Collection<MessageCustomer> mergedCustomers;
  
  @EJB
  private CustomerService customerService;
  
  public Map<Long, MessageCustomer> getjCustomers (){
	  if (jCustomers == null){
		  jCustomers = customerService.getAllCustomersFromJava(); 
	  }
	  return jCustomers;
  }
  
  
  public Map<Long, MessageCustomer> getcCustomers (){
	  if (cCustomers == null){
		  cCustomers = customerService.getAllCustomersFromDotNet();
	  }
	  return cCustomers;
  }
  
  public Collection<MessageCustomer> getMergedCustomers() {
	  if (mergedCustomers == null) {
		  // Run init script
		  // Pass jCustomers and cCustomers to the init-function to avoid another webservice call
		  mergedCustomers = customerService.initDatabase(getjCustomers(), getcCustomers());
	  }
	  return mergedCustomers;
  }
    
  /**
   * The main office database is initialized, if customers are in the database
   * @return true, if the database contains customers i.e. is initialized
   */
  public boolean isInitialized(){
	  return !customerService.getAllCustomers().isEmpty();
  }
}
