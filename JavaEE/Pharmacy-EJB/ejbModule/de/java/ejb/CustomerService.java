package de.java.ejb;

import java.util.Collection;

import javax.ejb.Remote;

import de.java.domain.customer.Customer;
import de.java.domain.prescription.Prescription;

@Remote
public interface CustomerService {
  
  Customer getCustomer(long id);
  
  Customer getCustomerWithPrescriptions(long id);

  Collection<Customer> getAllCustomers();

  Customer createCustomer(Customer newCustomer);

  Customer update(long id, String telephoneNumber, String address);

  Prescription createPrescription(long customerId, String issuer);

}
