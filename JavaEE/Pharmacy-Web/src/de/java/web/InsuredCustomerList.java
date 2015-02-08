package de.java.web;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import de.java.domain.InsuredCustomer;
import de.java.ejb.InsuredCustomerService;

@ManagedBean
public class InsuredCustomerList implements Serializable {

  private static final long serialVersionUID = 5212289925893596467L;

  @EJB
  private InsuredCustomerService insuredCustomerService;

  public Collection<InsuredCustomer> getInsuredCustomers() {
    return insuredCustomerService.getAllInsuredCustomers();
  }
 
}