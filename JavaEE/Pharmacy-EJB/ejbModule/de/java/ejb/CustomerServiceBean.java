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
    	//.createQuery("FROM Customer c WHERE c.name = :name SELECT c", Customer.class)
        .createQuery("SELECT COUNT(*) FROM Customer WHERE name = :name", Long.class)
        .setParameter("name", newCustomer.getName()).getSingleResult() > 0) {
      throw new KeyConstraintViolation(String.format(
          "Customer with name: %s already in database", newCustomer.getName()));
    }
    em.persist(newCustomer);
    return newCustomer;
  }

  @Override
  public Customer update(long id, String telephoneNumber, String address, String email, String birthDate) {
    Customer customer = getCustomer(id);
    customer.setTelephoneNumber(telephoneNumber);
    customer.setAddress(address);
    customer.setEmail(email);
    customer.setBirthDate(birthDate);
    return customer;
  }
  
  @Override
  public Customer updatePrescriptionBill(long id, double prescriptionBill) {
	  Customer customer = getCustomer(id);
	  customer.setPrescriptionBill(prescriptionBill);
	  return customer;
  }

  @Override
  public Prescription createPrescription(long customerId, String issuer, double totalPrice) {
    Prescription newPrescription = getCustomer(customerId).createPrescription();
    newPrescription.setIssuer(issuer);
    newPrescription.setTotalPrice(totalPrice);
    em.persist(newPrescription);
    return newPrescription;
  }
  
  /*Exam practice 
   * public Invoice createInvoice(Customer c,Date invoiceDate, long totalInCent) {
   * 
   * Invoice newInvoice = new Invoice();
   * newInvoice.setCustomer(c),
   * newInvoice.setInvoiceDate(invoiceDate);
   * newInvoice.setTotalInCent(totalInCent):
   * 
   * em.persist(newInvoice);
   * return newInvoice;
   * }
   * */

}
