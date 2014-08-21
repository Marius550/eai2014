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

  Customer update(long id, String telephoneNumber, String address, String email, String birthDate);
  
  /**Import customer Id to clearly identify the customer and pass over the customer's prescription bill (double)
 * @param id
 * @param prescriptionBill
 * @return Customer object in which the prescriptionBill is set (setPrescriptionBill() is located with corresponding getter in customer persistence)
 */
Customer updatePrescriptionBill(long id, double prescriptionBill);

  /**Important method! Called in web.createPrescription to set the parameter inputs issuer and totalPrice to the prescription of the of a certain Id belonging to a certain customer
   * Persists the data set!
 * @param customerId
 * @param issuer
 * @param totalPrice
 * @return prescription
 */
Prescription createPrescription(long customerId, String issuer, double totalPrice);

}
