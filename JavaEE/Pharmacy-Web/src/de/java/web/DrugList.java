package de.java.web;

import java.io.Serializable;
import java.util.Collection;

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
	 System.out.println(getDrug(2348234));
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
