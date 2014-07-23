package de.java.web;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.java.domain.Drug;
import de.java.ejb.DrugService;

//public class DrugList<pzn> implements Serializable {

@ManagedBean
public class DrugList implements Serializable {
  private static final long serialVersionUID = 9000977856559982072L;

  @EJB
  private DrugService drugService;

  private String searchTerm;
  
  @PersistenceContext
  private EntityManager em;
  
  //@Id
  //private int pzn;

  public Collection<Drug> getDrugs() {
    return drugService.getAllDrugsLike(searchTerm);
  }
  
  public Collection<Drug> getAllDrugs() {
    return em.createQuery("FROM Drug", Drug.class).getResultList();
  }
  
  public Drug getDrug(int pzn) {
    return em.find(Drug.class, pzn);
  }
  
  public void getAllDrugsInConsole() {
	 System.out.println("getAllDrugsInConsole()");
	 System.out.println(getAllDrugs());
  }

  public void getParticularDrugInConsole() {
	 System.out.println("getParticularDrugInConsole()");
	 System.out.println(getDrug(451151));//.getPrice(); is working
  }
  
  public int getAmountOfDrugsInCollection() {
	  return getAllDrugs().size();
	  //Alternative
	  //Object[] list = getAllDrugs().toArray();
	  //int i = list.length;
	  }
  
  public void testCollection1() {
	Object[] list = getAllDrugs().toArray();
	
	for (int j = 0; j < list.length; j++) {
		System.out.println(list[j]);
	}
  }
  
  public void testCollection2() {
	//	 Collection<Drug> list;
	//	 Map<pzn,Drug> map = new HashMap<pzn,Drug>();
	//	 for (Drug i : list) map.put(i.getPzn(), Drug); 
	  
	  System.out.println(getAllDrugs());
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
  
  //ctrl + 7
  //this was just used to test the eclipse console
//  public void testButton() { 
//	  int i = 5;
//	  int j = 5;
//	  int x = 0;
//	  x = i + j;
//	System.out.println("Calculation x = :" + x);
//  }

}
