package de.java.web;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import de.java.domain.Drug;
import de.java.ejb.DrugService;
import de.java.web.util.Util;
import de.java.ws.MessageDrug;

@ManagedBean
@ViewScoped
public class DrugPage implements Serializable {
  private static final long serialVersionUID = 8259058692232409420L;

  @EJB
  private DrugService drugService;

  private int pzn;
  // The currently displayed drug
  private Drug drug;
  // Representation of the currently displayed drug at JaVa
  private MessageDrug jDrug;
  // Representation of the currently displayed drug at C. Sharpe
  private MessageDrug cDrug;

  public int getPzn() {
    return pzn;
  }

  public void setPzn(int pzn) {
    this.pzn = pzn;
    init();
  }

  private void init() {
    drug = null;
  }

  /**
   * Initializes the drug page and ensures, that drugs are fetched from both JaVa and C.Sharpe
   */
  public void ensureInitialized(){
    try{
      if(getDrug() != null && getjDrug() != null && getcDrug() != null)
        // Success
        return;
    } catch(EJBException e) {
      e.printStackTrace();
    }
    Util.redirectToRoot();
  }

  public String submitMasterDataChanges() {
    drug = drugService.updateMasterData(drug.getPzn(), drug.getName(), drug.getPrice(), drug.getDescription());
    return toDrugPage();
  }

  private String toDrugPage() {
    return "/drug/details.xhtml?faces-redirect=true&pzn=" + pzn;
  }

  public Drug getDrug() {
    if (drug == null) {
      drug = drugService.getDrug(pzn);
    }
    return drug;
  }
  
  public MessageDrug getjDrug(){
	  if (jDrug == null){
		  jDrug = drugService.getDrugFromJava(pzn);
	  }
	  return jDrug;
  }
  
  public MessageDrug getcDrug(){
	  if (cDrug == null){
		   cDrug = drugService.getDrugFromDotNet(pzn);
	  }
	  return cDrug;
  }

}
