package de.java.web;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import de.java.domain.customer.Customer;
import de.java.domain.prescription.Prescription;
import de.java.ejb.CustomerService;
import de.java.web.util.Util;

@ManagedBean
@ViewScoped
public class CreatePrescription implements Serializable {
  
  private static final long serialVersionUID = -1451801302773013396L;
  
  private long customerId;
  private Prescription prescription = new Prescription();
  
  @EJB
  private CustomerService customerService;

  public void ensureInitialized(){
      Customer customer = customerService.getCustomer(customerId);
      if (customer == null)
        Util.redirectToRoot();
      prescription.setCustomer(customer);
  }

  public Prescription getPrescription() {
    return prescription;
  }

  public String create() {
    prescription = customerService.createPrescription(getCustomerId(), prescription.getIssuer());
    return "details.xhtml?faces-redirect=true&id=" + prescription.getId();
  }

  public long getCustomerId() {
    return customerId;
  }
  
  public void setCustomerId(long customerId) {
    this.customerId = customerId;
  }

}
