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
//import de.java.jms.CustomerMessage;
//import de.java.jms.MessageSender;
import de.java.ws.CustomerResourceClientJava;
import de.java.ws.CustomerResourceClientDotNet;
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

  /*
  @Override
  public Collection<Drug> getAllDrugsLike(String searchTerm) {
    if (empty(searchTerm)) {
      return getAllDrugs();
    }

    final String query = "SELECT d FROM Drug d WHERE d.pzn = :pzn OR lower(d.name) LIKE :searchTerm";
    return em.createQuery(query, Drug.class)
        .setParameter("searchTerm", prepareUniversalMatch(searchTerm))
        .setParameter("pzn", parseIntOrDefaultToZero(searchTerm))
        .getResultList();
  }
  */
  
  /*

  private boolean empty(String searchTerm) {
    return searchTerm == null || searchTerm.trim().isEmpty();
  }

  private int parseIntOrDefaultToZero(String searchTerm) {
    try {
      return Integer.parseInt(searchTerm);
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  private String prepareUniversalMatch(String searchTerm) {
    return "%" + searchTerm.toLowerCase() + "%";
  }

*/
  
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
  
	@Override
	public Collection<MessageCustomer> initDatabase(Map<Long, MessageCustomer> jCustomers, Map<Long, MessageCustomer> cCustomers) {
		
		Collection<MessageCustomer> mergedCustomers = new ArrayList<MessageCustomer>();
		
			for (MessageCustomer customerJava : jCustomers.values()){
				for (MessageCustomer customerCSharpe : cCustomers.values()){
					if(customerJava.getName().equals(customerCSharpe.getName()) && 
					   customerJava.getAddress().equals(customerCSharpe.getAddress()) &&
					   customerJava.getEmail().equals(customerCSharpe.getEmail())) {
						double combinedPrescriptionBillJavaCSharpe = customerJava.getPrescriptionBill() + customerCSharpe.getPrescriptionBill();	
						customerCSharpe.setPrescriptionBill(combinedPrescriptionBillJavaCSharpe);
						jCustomers.remove(customerJava.getId());
					}
				}
			}
			for (MessageCustomer customerJava : jCustomers.values()){
				mergedCustomers.add(customerJava);				
			}			 	
			
		for (MessageCustomer customerCSharpe : cCustomers.values()){

			mergedCustomers.add(customerCSharpe);
		}
		return mergedCustomers;
	}
	
}
