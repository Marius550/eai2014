package de.java.ejb;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.java.domain.customer.Customer;
import de.java.domain.prescription.Prescription;

@Stateless
public class CustomerServiceBean implements CustomerService {

  @PersistenceContext
  private EntityManager em;
  
  @Override
  public Customer getCustomer(long id) {
    return em.find(Customer.class, id);
  }

  @Override
  public Customer getCustomerWithPrescriptions(long id) {
    return forceLoadOfPrescriptions(getCustomer(id));
  }

  private Customer forceLoadOfPrescriptions(Customer customer) {
    if (customer == null) return customer;
    customer.getPrescriptions().size();
    return customer;
  }

  @Override
  public Collection<Customer> getAllCustomers() {
    return em.createQuery("From Customer", Customer.class).getResultList();
  }

  @Override
  public Customer createCustomer(Customer newCustomer) {
    if (em
        .createQuery("SELECT COUNT(*) FROM Customer WHERE name = :name", Long.class)
        .setParameter("name", newCustomer.getName()).getSingleResult() > 0) {
      throw new KeyConstraintViolation(String.format(
          "Customer with name: %s already in database", newCustomer.getName()));
    }
    em.persist(newCustomer);
    return newCustomer;
  }

  @Override
  public Customer update(long id, String telephoneNumber, String address) {
    Customer customer = getCustomer(id);
    customer.setTelephoneNumber(telephoneNumber);
    customer.setAddress(address);
    return customer;
  }

  @Override
  public Prescription createPrescription(long customerId, String issuer) {
    Prescription newPrescription = getCustomer(customerId).createPrescription();
    newPrescription.setIssuer(issuer);
    em.persist(newPrescription);
    return newPrescription;
  }

}
