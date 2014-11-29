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
MessageCustomer getCustomerFromJava(long id);//

/**
* @param id
* @return customer with the id from the C Sharp pharmacy database, accessed via webservices
*/
MessageCustomer getCustomerFromDotNet(int id);

  /**
 * @param customer
 * @return the customer after it is created in the HO database and also in both JaVa and C Sharpe databases via webservices
 */
//Drug createDrug(Drug drug);

  /**
 * @param pzn of the drug to be updated
 * @param new name of the drug
 * @param new name of the drug
 * @param new description of the drug
 * @param new drugMinimumAgeYears of the drug
 * @return the drug after it is updated in the HO database and also in both JaVa and C Sharpe databases via webservices
 */
//Drug updateMasterData(int pzn, String name, double price, String description, long drugMinimumAgeYears);

//Initialize customers and merge them into entity manager (Java customers are missing because merging them as well causes an error I yet do not know how to fix)
Collection<MessageCustomer> initDatabaseEntityManager(Map<Long, MessageCustomer> jCustomers, Map<Long, MessageCustomer> cCustomers);

//Since initDatabaseEntityManager() does not contain Java customers, this is an alternative which does not persist customers to the entity manager but therefore contains all of them
Collection<MessageCustomer> initDatabaseCollectionOnly(Map<Long, MessageCustomer> jCustomers, Map<Long, MessageCustomer> cCustomers);
}
