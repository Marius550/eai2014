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
  
  // All drugs at C. Sharpe
  //private Map<Integer, MessageDrug> cDrugs;
  // All maps in the HO-database, after running the init script
  //private Collection<MessageDrug> mergedDrugs;
  
  @EJB
  private CustomerService customerService;
  
  //Map<Long, MessageCustomer> getAllCustomersFromJava();//
  
  public Map<Long, MessageCustomer> getjCustomers (){
	  if (jCustomers == null){
		  jCustomers = customerService.getAllCustomersFromJava(); 
	  }
	  return jCustomers;
  }
  
  /*
  public Map<Integer, MessageDrug> getcDrugs (){
	  if (cDrugs == null){
		  cDrugs = drugService.getAllDrugsFromDotNet();
	  }
	  return cDrugs;
  }
  
  public Collection<MessageDrug> getMergedDrugs() {
	  if (mergedDrugs == null) {
		  // Run init script
		  // Pass jDrugs and cDrugs to the init-function to avoid another webservice call
		  mergedDrugs = drugService.initDatabase(getjDrugs(), getcDrugs());
	  }
	  return mergedDrugs;
  }
  */
    
  /**
   * The main office database is initialized, if customers are in the database
   * @return true, if the database contains drugs i.e. is initialized
   */
  public boolean isInitialized(){
	  return !customerService.getAllCustomers().isEmpty();
  }
}
