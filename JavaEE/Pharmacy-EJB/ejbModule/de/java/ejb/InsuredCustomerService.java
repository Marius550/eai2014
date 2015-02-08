package de.java.ejb;

import java.util.Collection;

import javax.ejb.Remote;

import de.java.domain.InsuredCustomer;

@Remote
public interface InsuredCustomerService {
  
  InsuredCustomer getInsuredCustomer(long id);

  Collection<InsuredCustomer> getAllInsuredCustomers();

  InsuredCustomer createInsuredCustomer(InsuredCustomer newInsuredCustomer);

  InsuredCustomer update(long id, String vorname, String nachname, String email);

}
