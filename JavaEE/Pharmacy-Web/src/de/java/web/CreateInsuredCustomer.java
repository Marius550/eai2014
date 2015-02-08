package de.java.web;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;

import de.java.domain.InsuredCustomer;
import de.java.ejb.InsuredCustomerService;
import de.java.web.util.Util;

@ManagedBean
public class CreateInsuredCustomer implements Serializable {
  
  private static final long serialVersionUID = -2422460459112599577L;
  
  private InsuredCustomer insuredCustomer = new InsuredCustomer();
  private InsuredCustomer lastInsuredCustomer;
  
  private String errorMessage;
  
  @EJB
  private InsuredCustomerService insuredCustomerService;

  public InsuredCustomer getInsuredCustomer() {
    return insuredCustomer;
  }
  
  public String persist() {
    // Action
    try{
      lastInsuredCustomer = insuredCustomerService.createInsuredCustomer(insuredCustomer);
      insuredCustomer = null;
      errorMessage = null;
    }
    catch(EJBException e){
      errorMessage = "Versicherter konnte nicht angelegt werden: " + Util.getCausingMessage(e);
    }
    
    // Navigation
      return "/insurance/list.xhtml";
  }

  public boolean isError() {
    return errorMessage != null;
  }
  
  public boolean isSuccess() {
    return lastInsuredCustomer != null;
  }
  
  public String getLastResult(){
    if(lastInsuredCustomer != null)
      return "Versicherter wurde angelegt: " + lastInsuredCustomer.getNachname() + ", mit ID: " + lastInsuredCustomer.getId();
    return errorMessage!=null?errorMessage:"";
  }
}
