package de.java.web;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import de.java.domain.customer.Customer;
import de.java.ejb.CustomerService;

@ManagedBean
public class CustomerList implements Serializable {

  private static final long serialVersionUID = 5212289925893596467L;

  @EJB
  private CustomerService customerService;

  public Collection<Customer> getCustomers() {
    return customerService.getAllCustomers();
  }

}
