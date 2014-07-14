package de.java.web;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import de.java.ejb.DrugService;
import de.java.ws.MessageDrug;

@ManagedBean
public class InitDrugs implements Serializable {
  private static final long serialVersionUID = -2902383377336267606L;
  
  // All drugs at JaVa
  private Map<Integer, MessageDrug> jDrugs;
  // All drugs at C. Sharpe
  private Map<Integer, MessageDrug> cDrugs;
  // All maps in the HO-database, after running the init script
  private Collection<MessageDrug> mergedDrugs;
  
  @EJB
  private DrugService drugService;
  
  public Map<Integer, MessageDrug> getjDrugs (){
	  if (jDrugs == null){
		  jDrugs = drugService.getAllDrugsFromJava();
	  }
	  return jDrugs;
  }
  
  public Map<Integer, MessageDrug> getcDrugs (){
	  if (cDrugs == null){
		  cDrugs = drugService.getAllDrugsFromDotNet();
	  }
	  return cDrugs;
  }
  
  public Collection<MessageDrug> getMergedDrugs() {
	  if (mergedDrugs == null) {
		  // Run init script
		  // Pass jDrugs and cDrugs to the init-function to avoid another webservice call
		  mergedDrugs = drugService.initDatabase(getjDrugs(), getcDrugs());
	  }
	  return mergedDrugs;
  }
    
  /**
   * The main office database is initialized, if drugs are in the database
   * @return true, if the database contains drugs i.e. is initialized
   */
  public boolean isInitialized(){
	  return !drugService.getAllDrugs().isEmpty();
  }
}
