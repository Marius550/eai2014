package de.java.web;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import de.java.domain.Drug;
import de.java.ejb.DrugService;
import de.java.ws.MessageDrug;

@ManagedBean
public class DrugList implements Serializable {
  private static final long serialVersionUID = 9000977856559982072L;

  @EJB
  private DrugService drugService;

  private String searchTerm;
  // All drugs at JaVa
  private Map<Integer, MessageDrug> jDrugs;
  // All drugs at C. Sharpe
  private Map<Integer, MessageDrug> cDrugs;
  
  public Collection<Drug> getDrugs() {
    return drugService.getAllDrugsLike(searchTerm);
  }
  
  //testing
//  public void removeDrug1() {
//	  drugService.getAllDrugs().remove(getjDrugs());
//	  }
  
  public Map<Integer, MessageDrug> getjDrugs(){
	  if (jDrugs == null){
		  jDrugs = drugService.getAllDrugsFromJava();
	  }
	  return jDrugs;
  }
  
  public Map<Integer, MessageDrug> getcDrugs(){
	  if (cDrugs == null){
		  cDrugs = drugService.getAllDrugsFromDotNet();
	  }
	  return cDrugs;
  }
  
  /**
   * Returns a specific drug from the Database of JaVa
   * @param pzn
   * @return MessageDrug-object, representing the drug at JaVa
   */
  public MessageDrug getjDrug(int pzn){
	  return getjDrugs().get(pzn);
  }
  
  /**
   * Returns a specific drug from the Database of C.Sharpe
   * @param pzn
   * @return MessageDrug-object, representing the drug at JaVa
   */
  public MessageDrug getcDrug(int pzn){
	  return getcDrugs().get(pzn);
  }
  
  /**
   * Sums up the unfulfilled quantity for all currently displayed drugs (i.e. search is considered)
   * @return sum of unfulfilled quantity of all displayed drugs at C.Sharpe and JaVa
   */
  public int sumUpUnfulfilledQuantity (){
	  int unfulfilledQuantity = 0;
	  for (Drug d : getDrugs()){
		  // Iterate only through displayed drugs in order to display right sums
		  // Put sum into if-clauses to avoid NPE, if one of the databases is inconsistent
		  if (getjDrugs().get(d.getPzn()) != null) {
			  unfulfilledQuantity += getjDrugs().get(d.getPzn()).getUnfulfilledQuantity();
		  }
	  	  if (getcDrugs().get(d.getPzn()) != null) {
	  		  unfulfilledQuantity += getcDrugs().get(d.getPzn()).getUnfulfilledQuantity();
	  	  }
	  }
	  return unfulfilledQuantity;
  }
  
  /**
   * Sums up the pending quantity for all currently displayed drugs (i.e. search is considered)
   * @return sum of pending quantity of all displayed drugs at C.Sharpe and JaVa
   */
  public int sumUpPendingQuantity (){
	  int pendingQuantity = 0;
	  for (Drug d : getDrugs()){
		  // Iterate only through displayed drugs in order to display right sums
		  if (getjDrugs().get(d.getPzn()) != null) {
			  pendingQuantity += getjDrugs().get(d.getPzn()).getPendingQuantity();
		  }
	  	  if (getcDrugs().get(d.getPzn()) != null) {
	  		  pendingQuantity += getcDrugs().get(d.getPzn()).getPendingQuantity();
	  	  }
	  }
	  return pendingQuantity;
  }
  
  /**
   * Sums up the stock for all currently displayed drugs (i.e. search is considered)
   * @return sum of stock of all displayed drugs at C.Sharpe and JaVa
   */
  public int sumUpStock(){
	  int stock = 0;
	  for (Drug d : getDrugs()){
		  // Iterate only through displayed drugs in order to display right sums
		  if (getjDrugs().get(d.getPzn()) != null) {
			  stock += getjDrugs().get(d.getPzn()).getStock();
		  }
	  	  if (getcDrugs().get(d.getPzn()) != null) {
	  		  stock += getcDrugs().get(d.getPzn()).getStock();
	  	  }
	  }
	  return stock;
  }
  
  /**
   * Sums up the prices for all currently displayed drugs (i.e. search is considered)
   * @return sum of prices of all displayed drugs at C.Sharpe and JaVa
   */
  public double sumUpPrices(){
	  double summedUpPrice = 0;
	  for (Drug d : getDrugs()){
		  // Iterate only through displayed drugs in order to display right sums
		  if (getjDrugs().get(d.getPzn()) != null) {
			  summedUpPrice += getjDrugs().get(d.getPzn()).getPrice();
		  }
	  	  if (getcDrugs().get(d.getPzn()) != null) {
	  		summedUpPrice += getcDrugs().get(d.getPzn()).getPrice();
	  	  }
	  }
	  summedUpPrice = summedUpPrice * 1/2;
	  return summedUpPrice;
  }
  
  /**
   * Gets the amount for all currently displayed drugs (i.e. search is considered)
   * @return amount of all displayed drugs at C.Sharpe and JaVa
   */
  public int getAmountOfPharmacyHODrugs(){

	  int javaDrugs = 0;
	  int cDrugs = 0;
	  
	  for (Drug d : getDrugs()){
	  // Iterate only through displayed drugs in order to display right amount

		 getjDrugs().get(d.getPzn());
		 javaDrugs = javaDrugs + 1;
			  
		 getjDrugs().get(d.getPzn());
		 cDrugs = cDrugs + 1;		  
	  }
	  //System.out.println("javaDrugs: " + javaDrugs + ", cDrugs: " + cDrugs);
	  int totalAmount = (javaDrugs + cDrugs)/2;
	  return totalAmount;
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
  
  public boolean isInitialized(){
	  return !drugService.getAllDrugs().isEmpty();
  }
  
  public String remove(Drug drug) {
	    drugService.removeDrug(drug.getPzn());
	    return "/drug/list.xhtml";
  }

}
