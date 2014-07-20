package de.java.web;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import de.java.domain.customer.Customer;
import de.java.ejb.CustomerService;
import de.java.web.util.Util;

@ManagedBean
@ViewScoped
public class CustomerPage implements Serializable {

  private static final long serialVersionUID = 3577839317048078008L;

  @EJB
  private CustomerService customerService;

  private long id;
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

  public String submit() {
    customer = customerService.update(customer.getId(), customer.getTelephoneNumber(), customer.getAddress());
    return "details.xhtml?faces-redirect=true&id=" + id;
  }

  public Customer getCustomer() {
    if (customer == null) {
      customer = customerService.getCustomerWithPrescriptions(id);
    }
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

}
