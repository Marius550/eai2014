package de.java.web;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;

import de.java.domain.Drug;
import de.java.ejb.DrugService;
import de.java.web.util.Util;

import java.util.Random;

@ManagedBean
public class CreateDrug implements Serializable {
  private static final long serialVersionUID = -2902383377336267606L;
  
  private Drug drug = new Drug();
  private Drug lastDrug;
  
  private boolean batch;
  
  private String errorMessage;
  
  @EJB
  private DrugService drugService;

  public Drug getDrug() {
    return drug;
  }

  public boolean isBatch() {
    return batch;
  }

  public void setBatch(boolean batch) {
    this.batch = batch;
  }
  
  public String persist() {
    // Action
    try{
      lastDrug = drugService.createDrug(drug);
      drug = null;
      errorMessage = null;
    }
    catch(EJBException e){
      errorMessage = "Drug not created: " + Util.getCausingMessage(e);
    }
    
    // Navigation
    if(isBatch() || isError())
      return null;
    else
      return "/drug/list.xhtml";
  }

  public boolean isError() {
    return errorMessage != null;
  }
  
  public boolean isSuccess() {
    return lastDrug != null;
  }
  
  public String getLastResult(){
    if(lastDrug != null)
      return "Drug created: " + lastDrug.toString();
    return errorMessage!=null?errorMessage:"";
  }
  
  public int randomizePZN() {
	  Random rnd = new Random();
	  int randomEightDigit = 10000000 + rnd.nextInt(90000000);

	  return randomEightDigit;
  }
  
  public String refresh() {
	  return "/drug/create.xhtml";
  } 
}
