package de.java.web;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import de.java.domain.Customer;
import de.java.ejb.CustomerService;
import de.java.web.util.Util;

@ManagedBean
@ViewScoped
public class CustomerPage implements Serializable {
  private static final long serialVersionUID = 8259058692232409420L;

  @EJB
  private CustomerService customerService;

  private long id;
  // The currently displayed customer
  private Customer customer;

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

  /**
   * Initializes the customer page and ensures
   */
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

  public Customer getCustomer() {
    if (customer == null) {
      customer = customerService.getCustomer(id);
    }
    return customer;
  }
  
}
