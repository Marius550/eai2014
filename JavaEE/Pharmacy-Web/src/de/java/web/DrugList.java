package de.java.web;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.java.domain.Drug;
import de.java.ejb.DrugService;

@ManagedBean
public class DrugList implements Serializable {
  private static final long serialVersionUID = 9000977856559982072L;

  @EJB
  private DrugService drugService;

  private String searchTerm;
  
  @PersistenceContext
  private EntityManager em;
  
  private Map<Integer, Drug> PharmacyDrugs;
 

  public Collection<Drug> getDrugs() {
    return drugService.getAllDrugsLike(searchTerm);
  }
  
  public Collection<Drug> getAllDrugs() {
    return em.createQuery("FROM Drug", Drug.class).getResultList();
  }
  
  public Drug getDrug(int pzn) {
    return em.find(Drug.class, pzn);
  }
  
  //alt+shift+J
  /**
 * Just some testing which is print out in the console
 */
//public void getAllDrugsInConsole() {
//	 System.out.println("getAllDrugsInConsole()");
//	 System.out.println(getAllDrugs());
//	 System.out.println("getParticularDrugInConsole()");
//	 System.out.println(getDrug(451151));//.getPrice(); is working
//	 System.out.println("getAllDrugsToHashMap: " + getAllDrugsToHashMap());
//  }
  
  /**
 * @return The amount of PharmacyDrugs (counts the Collection<Drug> elements)
 */
public int getAmountOfDrugsInCollection() {
	  return getAllDrugs().size();
	  //Alternative
	  //Object[] list = getAllDrugs().toArray();
	  //int i = list.length;
	  }
  
  /**
 * Transform Collection<Drug> to an Array over which one can iterate and print out each drug
 */
public void iterateOverAllDrugsArray() {
	Object[] list = getAllDrugs().toArray();
	
	for (int j = 0; j < list.length; j++) {
		System.out.println(list[j]);
	}
	System.out.println(list);
  }
  
  /**
   * Convert Collection <Drug> into HashMap and assign pzn to each map element
 * @param collection
 * @return Map<Integer, Drug> map
 */
private Map<Integer, Drug> convertPharmacyDrugCollectionIntoHashMap(Collection<Drug> collection){
	  Map<Integer, Drug> map = new HashMap<Integer, Drug>();
	  for (Drug mDrug : collection){
		  map.put(mDrug.getPzn(), mDrug);
	  }
	  return map;
  }
  
  /** Get all Pharmacy drugs in the form of a Collection and concert them to HashMap using the convertMessageDrugCollectionIntoHashMap method
 * @return  Map<Integer, Drug> 
 */
public Map<Integer, Drug> getAllDrugsToHashMap(){
	  if (PharmacyDrugs == null){
		  PharmacyDrugs = convertPharmacyDrugCollectionIntoHashMap(getAllDrugs());
	  }
	  return PharmacyDrugs;
  }
  
  /** Iterate through Collection<Drug> and get the price for each element using the Map<Integer, Drug> HashMap
   * Sums up the prices for all currently displayed PharmacyDrugs (i.e. search is considered)
   * @return sum of prices of all PharmacyDrugs
   */
  public double sumUpPricesPharmacyDrugs(){
	  double summedUpPrice = 0;
	  for (Drug d : getAllDrugs()){
		  // Iterate only through displayed drugs in order to display right sums
		  if (getAllDrugsToHashMap().get(d.getPzn()) != null) {
			  summedUpPrice += getAllDrugsToHashMap().get(d.getPzn()).getPrice();
		  }
	  }
	  return summedUpPrice;
  }
  
  /** Iterate through Collection<Drug> and get the stock for each element using the Map<Integer, Drug> HashMap
   * Sums up the stock for all currently displayed PharmacyDrugs (i.e. search is considered)
   * @return sum of stock of all PharmacyDrugs
   */
  public double sumUpStockPharmacyDrugs(){
	  int stock = 0;
	  for (Drug d : getAllDrugs()){
		  // Iterate only through displayed drugs in order to display right sums
		  if (getAllDrugsToHashMap().get(d.getPzn()) != null) {
			  stock += getAllDrugsToHashMap().get(d.getPzn()).getStock();
		  }
	  }
	  return stock;
  }
  
  
  public String getSearchTerm() {
    return searchTerm;
  }

  public void setSearchTerm(String searchTerm) {
    this.searchTerm = searchTerm;
  }

  public String search() {
    return "/drug/list.xhtml?faces-redirect=true&search=" + searchTerm;
  }
  
  public String refresh() {
	return "/drug/list.xhtml";
  }

}
