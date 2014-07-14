package de.java.web;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;

import de.java.domain.customer.Customer;
import de.java.ejb.CustomerService;
import de.java.web.util.Util;

@ManagedBean
public class CreateCustomer implements Serializable {
  
  private static final long serialVersionUID = -2422460459112599577L;
  
  private Customer customer = new Customer();
  private Customer lastCustomer;
  
  private boolean batch;
  
  private String errorMessage;
  
  @EJB
  private CustomerService customerService;

  public Customer getCustomer() {
    return customer;
  }

  public boolean isBatch() {
    return batch;
  }

  public void setBatch(boolean batch) {
    this.batch = batch;
  }
  
  public String persist() {
    // Action
    try{
      lastCustomer = customerService.createCustomer(customer);
      customer = null;
      errorMessage = null;
    }
    catch(EJBException e){
      errorMessage = "Customer not created: " + Util.getCausingMessage(e);
    }
    
    // Navigation
    if(isBatch() || isError())
      return null;
    else
      return "/customer/list.xhtml";
  }

  public boolean isError() {
    return errorMessage != null;
  }
  
  public boolean isSuccess() {
    return lastCustomer != null;
  }
  
  public String getLastResult(){
    if(lastCustomer != null)
      return "Customer created: " + lastCustomer.toString();
    return errorMessage!=null?errorMessage:"";
  }
}
