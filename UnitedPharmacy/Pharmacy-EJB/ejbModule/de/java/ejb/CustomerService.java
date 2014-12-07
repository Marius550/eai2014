package de.java.ejb;

import java.util.Collection;
import java.util.Map;

import javax.ejb.Remote;

import de.java.domain.Customer;
import de.java.ws.MessageCustomer;

@Remote
public interface CustomerService {

  /**
 * @return all customers from the database of the HO
 */
Collection<Customer> getAllCustomers();

/**
 * @return amount of customers in the database of the HO
 */
int getAmountOfAllCustomers();

/**
 * @return all customers from the JaVa pharmacy database in a HashMap of id and MessagCustomer
 */
Map<Long, MessageCustomer> getAllCustomersFromJava();

/**
 * @return all customers from the C Sharpe pharmacy database in a HashMap of id and MessagCustomer
 */
Map<Long, MessageCustomer> getAllCustomersFromDotNet();

  /**
 * @param searchTerm : name or id to be searched for
 * @return all customers with id oder name equal to the seachTerm from the database of the HO
 */
Collection<Customer> getAllCustomersLike(String searchTermCustomer);

  /**
 * @param id
 * @return customer with the id from the database of the HO
 */
Customer getCustomer(long id);

  /**
 * @param id
 * @return customer with the id from the JaVa pharmacy database, accessed via webservices
 */
MessageCustomer getCustomerFromJava(long id);

/**
* @param id
* @return customer with the id from the C Sharp pharmacy database, accessed via webservices
*/
MessageCustomer getCustomerFromDotNet(int id);

/**1. Customers from jCustomers and cCustomers are parameters, as they are already fetched in InitCustomers()
 * 2. To summarize (only get) all customers from both pharmacies in the HO, customers are merged into the entity manager
 * 3. Known issue: unfortunately this does not include Java customers since I could not figure out yet how to add customers 
 * from both pharmacies to the entity manager without facing id conflicts
 * 4. To summarize all customers initDatabaseEntityManager() is used
 * @param jCustomers
 * @param cCustomers
 * @return collection of customers including C# customers, 'duplicate customers' but not java customers
 */
Collection<MessageCustomer> initDatabase(Map<Long, MessageCustomer> jCustomers, Map<Long, MessageCustomer> cCustomers);

/**
 * @param jCustomers
 * @param cCustomers
 * @return the amount of customers who are registered in JavaPharmacy and C#Pharmacy (same name, address and email strings)
 */
int getAmountOfDuplicateCustomers(Map<Long, MessageCustomer> jCustomers, Map<Long, MessageCustomer> cCustomers);

/**1. Summarizes (only get) all customers from both pharmacies within a collection
 * 2. Disadvantage: customers not persisted into entity manager -> many useful functions can therefore not be used
 * 3. Prescription bills of 'duplicate customers' are considered
 * @param jCustomers
 * @param cCustomers
 * @return collection of all Java and C# customers
 */
//Collection<MessageCustomer> initDatabaseCollectionOnly(Map<Long, MessageCustomer> jCustomers, Map<Long, MessageCustomer> cCustomers);

}
