package de.java.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import de.java.domain.Customer;
import de.java.ws.CustomerResourceClientDotNet;
//import de.java.domain.Drug;
//import de.java.jms.CustomerMessage;
//import de.java.jms.MessageSender;
import de.java.ws.CustomerResourceClientJava;
import de.java.ws.MessageCustomer;

@Stateless
public class CustomerServiceBean implements CustomerService {

  // URLs of the webservices	
  private static final String BASE_URI_JAVA = "http://localhost:8080/Pharmacy04-Web/api";
  private static final String BASE_URI_DOTNET = "http://localhost:1054/RestService";
	
  @PersistenceContext
  private EntityManager em;
  
  /**
   * Prepares a rest-webservice-proxy for connection to JaVa
   * @return WebService proxy for JaVa
   */
  private CustomerResourceClientJava prepareCustomerResourceClientJava (){
	  RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
	  ResteasyWebTarget target = new ResteasyClientBuilder().build().target(BASE_URI_JAVA);
	  return target.proxy(CustomerResourceClientJava.class);
  }
  
  /**
   * Prepares a rest-webservice-proxy for connection to C.Sharpe
   * @return WebService proxy for C.Sharpe
   */
  
  private CustomerResourceClientDotNet prepareCustomerResourceClientDotNet(){
	  RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
	  ResteasyWebTarget target = new ResteasyClientBuilder().build().target(BASE_URI_DOTNET);
	  return target.proxy(CustomerResourceClientDotNet.class);
  }
  
  
  @Override
  public Collection<Customer> getAllCustomers() {
    return em.createQuery("FROM Customer", Customer.class).getResultList();
  }
  
  
  @Override
  public int getAmountOfAllCustomers() {
    return em.createQuery("FROM Customer", Customer.class).getResultList().size();
  }
  
  
  @Override
  public Collection<Customer> getAllCustomersLike(String searchTermCustomer) {
    if (empty(searchTermCustomer)) {
      return getAllCustomers();
    }					 
    final String query = "SELECT c FROM Customer c WHERE c.id = :id OR lower(c.name) LIKE :searchTermCustomer";
    return em.createQuery(query, Customer.class)
        .setParameter("searchTermCustomer", prepareUniversalMatch(searchTermCustomer))
        .setParameter("id", parseLongOrDefaultToZero(searchTermCustomer))	//c.id = :id OR
        .getResultList();
  }
  
  
  private boolean empty(String searchTermCustomer) {
    return searchTermCustomer == null || searchTermCustomer.trim().isEmpty();
  }
 
  
  private long parseLongOrDefaultToZero(String searchTermCustomer) {
    try {
      return Long.parseLong(searchTermCustomer);
    } catch (NumberFormatException e) {
      return 0;
    }
  } 

  
  private String prepareUniversalMatch(String searchTermCustomer) {
    return "%" + searchTermCustomer.toLowerCase() + "%";
  }

  
  @Override
  public Customer getCustomer(long id) {
    return em.find(Customer.class, id);
  }
  
  @Override
  public MessageCustomer getCustomerFromJava(long id){
	  return prepareCustomerResourceClientJava().getCustomer(id);
  }
  
  
  @Override
  public MessageCustomer getCustomerFromDotNet(int id){
	  return prepareCustomerResourceClientDotNet().getCustomer(id);
  }
  
  
  @Override
  public Map<Long, MessageCustomer> getAllCustomersFromJava(){
	  return convertMessageCustomerCollectionIntoHashMap(prepareCustomerResourceClientJava().getAllCustomers());
  }
  
  
  @Override
  public Map<Long, MessageCustomer> getAllCustomersFromDotNet(){
	  return convertMessageCustomerCollectionIntoHashMap(prepareCustomerResourceClientDotNet().getAllCustomers());
  }
  
  /**
   * Converts a Collection of MessageCustomers into a HashMap in order to search through them by id
   * @param collection the collection of MessageCustomer's to convert
   * @return a HashMap with id as key and the MessageCustomer-object as value
   */
  private Map<Long, MessageCustomer> convertMessageCustomerCollectionIntoHashMap (Collection<MessageCustomer> collection){
	  Map<Long, MessageCustomer> map = new HashMap<Long, MessageCustomer>();
	  for (MessageCustomer mCustomer : collection){
		  map.put(mCustomer.getId(), mCustomer);
	  }
	  return map;
  }

/*
  @Override
  public Drug updateMasterData(int pzn, String name, double price, String description, long drugMinimumAgeYears) {
    Drug drug = getDrug(pzn);
    drug.setName(name);
    drug.setPrice(price);
    drug.setDescription(description); 
    drug.setDrugMinimumAgeYears(drugMinimumAgeYears);
    
    persistUpdateMasterDataToDotNet(drug);
    persistUpdateMasterDataToJava(drug);
    
    return drug;
  }
  */
  /*
  private long setEvenCustomerId() {
	Random random = new Random();
	long randomNumber = 1000 + random.nextInt(9000);
	if (randomNumber < 0) {
		randomNumber *= -1;
	}
	  return randomNumber;  
  }
  
  public Customer determineCustomerId(long customerId) {
	  Customer c = getCustomer(customerId);
	  c.setId(setEvenCustomerId());
	  System.out.println("determineCustomerId: " + c.getName() + ", Id: " + c.getId());
	  return  c;
  }
  */
  
  /**
   * Creates a customer only in the HO-database and does not publish it to JaVa and C.Sharpe
   * @param newCustomer the customer to create
   * @return the created customer
   */
  private Customer createCustomerEntityManager(Customer newCustomer) {
	  System.out.println("newCustomer EntityManager: " + newCustomer.getName() + ", ID: " + newCustomer.getId() + ", prescriptionBill: " + newCustomer.getPrescriptionBill());
	  if (em.createQuery("SELECT COUNT(*) FROM Customer WHERE id=:id",
			Long.class).setParameter("id", newCustomer.getId())
			.getSingleResult() > 0) 
		  
		throw new KeyConstraintViolation(String.format(
				"Customer with Id: %s already in database!", newCustomer.getId()));
	  //Attention: previously merge(), but that does not work --> Delete @GeneratedValue for customerId in customer entity
	  em.persist(newCustomer);  
	return newCustomer;
  }
  
	@Override
	public Collection<MessageCustomer> initDatabase(Map<Long, MessageCustomer> jCustomers, Map<Long, MessageCustomer> cCustomers) {
		Collection<MessageCustomer> mergedCustomers = new ArrayList<MessageCustomer>();
		try {
			for (MessageCustomer customerJava : jCustomers.values()){
				for (MessageCustomer customerCSharpe : cCustomers.values()){
					customerCSharpe.setPharmacySource("C# Pharmacy");
					if(customerJava.getName().equals(customerCSharpe.getName()) && 
					   customerJava.getAddress().equals(customerCSharpe.getAddress()) &&
					   customerJava.getEmail().equals(customerCSharpe.getEmail())) {
						double combinedPrescriptionBillJavaCSharpe = customerJava.getPrescriptionBill() + customerCSharpe.getPrescriptionBill();
						customerCSharpe.setPharmacySource("Both pharmacies");
						customerCSharpe.setPrescriptionBill(combinedPrescriptionBillJavaCSharpe);
						jCustomers.remove(customerJava.getId());
						System.out.println("Equal customers in Java and C#: " + customerCSharpe.getName());
					}
				}
			} 
			for (MessageCustomer customerJava : jCustomers.values()){
				customerJava.setPharmacySource("Java Pharmacy");
				mergedCustomers.add(customerJava);
				int initializeCustomerId = getAmountOfAllCustomers() + 1;
				createCustomerEntityManager(customerJava.convertToCustomerFromJava(initializeCustomerId));
			}			 	 
		for (MessageCustomer customerCSharpe : cCustomers.values()){
			mergedCustomers.add(customerCSharpe);
			int initializeCustomerId = getAmountOfAllCustomers() + 1;
			createCustomerEntityManager(customerCSharpe.convertToCustomerFromDotNet(initializeCustomerId));
		}
		
		} catch(Exception ex) {
			throw new KeyConstraintViolation(String.format("Failure in initializing customers in Entity Manager: initDatabase(): " + ex));
		}
		return mergedCustomers;
	}
	
	
	@Override
	public int getAmountOfDuplicateCustomers(Map<Long, MessageCustomer> jCustomers, Map<Long, MessageCustomer> cCustomers) {
		Collection<MessageCustomer> duplicateCustomers = new ArrayList<MessageCustomer>();
		for (MessageCustomer customerJava : jCustomers.values()){
			for (MessageCustomer customerCSharpe : cCustomers.values()){
				if(customerJava.getName().equals(customerCSharpe.getName()) && 
				   customerJava.getAddress().equals(customerCSharpe.getAddress()) &&
				   customerJava.getEmail().equals(customerCSharpe.getEmail())) {
					
					duplicateCustomers.add(customerJava);
				}
			}
		}
		return duplicateCustomers.size();
	}
	
}
	 
	/*
	@Override
	public Collection<MessageCustomer> initDatabaseCollectionOnly(Map<Long, MessageCustomer> jCustomers, Map<Long, MessageCustomer> cCustomers) {
		Collection<MessageCustomer> mergedCustomers = new ArrayList<MessageCustomer>();
		try {
			for (MessageCustomer customerJava : jCustomers.values()){
				for (MessageCustomer customerCSharpe : cCustomers.values()){
					customerCSharpe.setPharmacySource("C# Pharmacy");
					if(customerJava.getName().equals(customerCSharpe.getName()) && 
					   customerJava.getAddress().equals(customerCSharpe.getAddress()) &&
					   customerJava.getEmail().equals(customerCSharpe.getEmail())) {
						double combinedPrescriptionBillJavaCSharpe = customerJava.getPrescriptionBill() + customerCSharpe.getPrescriptionBill();
						customerCSharpe.setPharmacySource("Both pharmacies");
						customerCSharpe.setPrescriptionBill(combinedPrescriptionBillJavaCSharpe);
						jCustomers.remove(customerJava.getId());
					}
				}
			}
			for (MessageCustomer customerJava : jCustomers.values()){
				customerJava.setPharmacySource("Java Pharmacy");
				mergedCustomers.add(customerJava);	
			}			 	 
			
		for (MessageCustomer customerCSharpe : cCustomers.values()){
			mergedCustomers.add(customerCSharpe);
		}
		
		} catch(Exception ex) {
			throw new KeyConstraintViolation(String.format("Failure in initializing customers in mergedCustomers collection: initDatabaseCollectionOnly(): " + ex));
		}
		return mergedCustomers; 
	}
	*/

/*
 *       <h:dataTable value="#{customerList.invokeInitDatabaseCollectionOnly()}" var="cur"
      styleClass="data-table" headerClass="data-cell header-cell"
      columnClasses="data-cell,data-cell,data-cell,data-cell,data-cell,data-cell,data-cell"
      rendered="#{not empty customerList.customers}" footerClass="data-cell footer-cell">
      <h:column>
        <f:facet name="header">Id</f:facet>
        #{cur.id}
      </h:column>
      <h:column>
        <f:facet name="header">Name</f:facet>
        #{cur.name}
        <f:facet name="footer">Amount of customers: #{customerList.getAmountOfPharmacyHOCustomersCollection()}</f:facet>
      </h:column>
      <h:column>
      <f:facet name="header">Address</f:facet>
        #{cur.address}
      </h:column>
      <h:column>
      <f:facet name="header">Email</f:facet>
        #{cur.email}
      </h:column>
      <h:column>
      <f:facet name="header">Prescription bill</f:facet>
        #{cur.prescriptionBill}
      </h:column>
      <h:column>
      <f:facet name="header">Pharmacy source</f:facet>
        #{cur.pharmacySource}
      </h:column>
    </h:dataTable>
    
    <h3>Customers in entity manager: Missing Java Customers</h3>
    <p></p>
    */
