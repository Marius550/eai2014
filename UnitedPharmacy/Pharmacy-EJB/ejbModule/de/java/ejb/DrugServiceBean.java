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

import de.java.domain.Drug;
import de.java.jms.DrugMessage;
import de.java.jms.MessageSender;
import de.java.ws.DrugResourceClientDotNet;
import de.java.ws.DrugResourceClientJava;
import de.java.ws.MessageDrug;

@Stateless
public class DrugServiceBean implements DrugService {

  // URLs of the webservices (Use them in POSTMAN)
  private static final String BASE_URI_JAVA = "http://localhost:8080/Pharmacy04-Web/api";
  private static final String BASE_URI_DOTNET = "http://localhost:1054/RestService";
	
  @PersistenceContext
  private EntityManager em;
  
  /**
   * Prepares a rest-webservice-proxy for connection to JaVa
   * @return WebService proxy for JaVa
   */
  private DrugResourceClientJava prepareDrugResourceClientJava (){
	  RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
	  ResteasyWebTarget target = new ResteasyClientBuilder().build().target(BASE_URI_JAVA);
	  return target.proxy(DrugResourceClientJava.class);
  }
  
  /**
   * Prepares a rest-webservice-proxy for connection to C.Sharpe
   * @return WebService proxy for C.Sharpe
   */
  private DrugResourceClientDotNet prepareDrugResourceClientDotNet(){
	  RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
	  ResteasyWebTarget target = new ResteasyClientBuilder().build().target(BASE_URI_DOTNET);
	  return target.proxy(DrugResourceClientDotNet.class);
  }

  @Override
  public Collection<Drug> getAllDrugs() {
    return em.createQuery("FROM Drug", Drug.class).getResultList();
  }

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

  @Override
  public Drug getDrug(int pzn) {
    return em.find(Drug.class, pzn);
  }
  
  @Override
  public MessageDrug getDrugFromJava(int pzn){
	  return prepareDrugResourceClientJava().getDrug(pzn);
  }
  
  @Override
  public MessageDrug getDrugFromDotNet(int pzn){
	  return prepareDrugResourceClientDotNet().getDrug(pzn);
  }
  
  @Override
  public Map<Integer, MessageDrug> getAllDrugsFromJava(){
	  return convertMessageDrugCollectionIntoHashMap(prepareDrugResourceClientJava().getAllDrugs());
  }
  
  @Override
  public Map<Integer, MessageDrug> getAllDrugsFromDotNet(){
	  return convertMessageDrugCollectionIntoHashMap(prepareDrugResourceClientDotNet().getAllDrugs());
  }
  
  /**
   * Converts a Collection of MessageDrugs into a HashMap in order to search through them by pzn
   * @param collection the collection of MessageDrug's to convert
   * @return a HashMap with pzn as key and the MessageDrug-object as value
   */
  private Map<Integer, MessageDrug> convertMessageDrugCollectionIntoHashMap (Collection<MessageDrug> collection){
	  Map<Integer, MessageDrug> map = new HashMap<Integer, MessageDrug>();
	  for (MessageDrug mDrug : collection){
		  map.put(mDrug.getPzn(), mDrug);
	  }
	  return map;
  }

  @Override
  public Drug createDrug(Drug newDrug) {
	createDrugLocally(newDrug);

    persistCreateDrugToJava(newDrug);
    persistCreateDrugToDotNet(newDrug);
    
    return newDrug;
  }
  
  /**
   * Creates a drug only in the HO-database and does not publish it to JaVa and C.Sharpe
   * @param newDrug the drug to create
   * @return the created drug
   */
  private Drug createDrugLocally(Drug newDrug) {
  	if (em.createQuery("SELECT COUNT(*) FROM Drug WHERE pzn=:pzn",
			Long.class).setParameter("pzn", newDrug.getPzn())
			.getSingleResult() > 0)
		throw new KeyConstraintViolation(String.format(
				"Drug with PZN: %s already in database", newDrug.getPzn()));

  	em.persist(newDrug);
	return newDrug;
  }
  
  /**
   * Persists creation of a drug to JaVa via a rest webservice
   * @param drug
   */
  private void persistCreateDrugToJava(Drug drug){
	  // OLD drug creation via WS. Now using JMS
	  // prepareDrugResourceClientJava().createDrug(mDrug);
	  MessageSender.send(new DrugMessage(drug, "create"));
  }
  
  /**
   * Persists creation of a drug to C.Sharpe via a rest webservice
   * @param mDrug
   */
  private void persistCreateDrugToDotNet(Drug drug){
	  prepareDrugResourceClientDotNet().createDrug(new MessageDrug(drug));
  }

  @Override
  public Drug updateMasterData(int pzn, String name, double price, String description) {
    Drug drug = getDrug(pzn);
    drug.setName(name);
    drug.setPrice(price);
    drug.setDescription(description);
    
    persistUpdateMasterDataToDotNet(drug);
    persistUpdateMasterDataToJava(drug);
    
    return drug;
  }
  
  /**
   * Persists update of a drug to JaVa via a rest webservice
   * @param mDrug
   */
  private void persistUpdateMasterDataToJava(Drug drug){
	  // OLD update was done via WS. Now using JMS:
	  // prepareDrugResourceClientJava().updateDrug(drug.getPzn(), new MessageDrug(drug));
	  MessageSender.send(new DrugMessage(drug, "update"));
	  
  }
  
  /**
   * Persists update of a drug to C.Sharpe via a rest webservice
   * @param mDrug
   */
  private void persistUpdateMasterDataToDotNet(Drug drug){
	  prepareDrugResourceClientDotNet().updateDrug(drug.getPzn(), new MessageDrug(drug));
  }

	@Override
	public Collection<MessageDrug> initDatabase(Map<Integer, MessageDrug> jDrugs, Map<Integer, MessageDrug> cDrugs) {
		// Prevent method from getting called, when drugs are already there
		if (getAllDrugs().size() > 0){
			return null;
		}
		
		// Collections of message drugs, that are filled to call webservices with batches
		// rather than multiple times (to keep web-service calls low)
		Collection<MessageDrug> drugsToCreateAtJava = new ArrayList<MessageDrug>();
		Collection<MessageDrug> drugsToCreateAtDotNet = new ArrayList<MessageDrug>();
		Collection<MessageDrug> drugsToUpdateAtDotNet = new ArrayList<MessageDrug>();
		// return-object for displaying the resulting database
		Collection<MessageDrug> locallyCreatedDrugs = new ArrayList<MessageDrug>();
		
		// First go through drugs at JaVa, since they are created either way
		for (MessageDrug d : jDrugs.values()){
			// Create drug in local db
			createDrugLocally(d.convertToDrug());
			locallyCreatedDrugs.add(d);
			
			if (cDrugs.containsKey(d.getPzn())){
				// Drug is already at C.Sharpe (pzn-match true)
				if (!drugMasterDataIsEqual(d, cDrugs.get(d.getPzn()))){
					// Drug Master Data at C.Sharpe is differing and needs to get update
					 drugsToUpdateAtDotNet.add(d);
				}
				// Remove drug from C.Sharpe Wrapper, as pzn-based match resulted true
				// and therefore the drug is already processed
				cDrugs.remove(d.getPzn());
			} else {
				// Drug is not yet at C.Sharpe, so create it there
				drugsToCreateAtDotNet.add(d);
			}
		}
		// Afterwards process remaining drugs at C.Sharpe (that are therefore not at JaVa)
		for (MessageDrug d : cDrugs.values()){
			// create drugs locally
			createDrugLocally(d.convertToDrug());
			locallyCreatedDrugs.add(d);
			
			// add new drug to JaVa
			drugsToCreateAtJava.add(d);			
		}
		
		// Persist to Java and DotNet
		prepareDrugResourceClientJava().createDrugs(drugsToCreateAtJava);
		prepareDrugResourceClientDotNet().createDrugs(drugsToCreateAtDotNet);
		prepareDrugResourceClientDotNet().updateDrugs(drugsToUpdateAtDotNet);
		
		return locallyCreatedDrugs;	
	}
	
	/**
	 * Compares the master data (pzn, name and description) of two message drugs
	 * @param d1 MessageDrug 1 to compare
	 * @param d2 MessageDrug 2 to compare
	 * @return true, if the pzn, name, price and description are equal
	 */
	private boolean drugMasterDataIsEqual(MessageDrug d1, MessageDrug d2){
		return d1.getPzn() == d2.getPzn() && 
				d1.getName() == d2.getName() && 
				d1.getPrice() == d2.getPrice() && 
				d1.getDescription() == d2.getDescription();
	}
}
