package de.java.web;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import de.java.ejb.DrugService;

@ManagedBean
public class IndexPage {
	
	@EJB
	private DrugService drugService;
	
	  /**
	   * The main office database is initialized, if drugs are in the database
	   * @return true, if the database contains drugs i.e. is initialized
	   */
	  public boolean isInitialized(){
		  return !drugService.getAllDrugs().isEmpty();
	  }
}
