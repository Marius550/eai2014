package de.java.web;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;

import de.java.domain.Drug;
import de.java.domain.replenishment.Position;
import de.java.ejb.DrugService;
import de.java.web.util.Util;

@ManagedBean
public class DrugHistory implements Serializable {
  private static final long serialVersionUID = -1323161833846877488L;

  @EJB
  private DrugService drugService;

  private int pzn;
  private Drug drug;

  public int getPzn() {
    return pzn;
  }

  public void setPzn(int pzn) {
    this.pzn = pzn;
  }

  public void ensureInitialized(){
    try{
      if(getDrug() != null)
        // Success
        return;
    } catch(EJBException e) {
      e.printStackTrace();
    }
    Util.redirectToRoot();
  }

  public Collection<Position> getPendingReplenishmentOrderPositions() {
    return new ArrayList<Position>();
  }

  public Drug getDrug() {
    if (drug == null) {
      drug = drugService.getDrugWithInventoryEvents(pzn);
    }
    return drug;
  }

  public void setDrug(Drug drug) {
    this.drug = drug;
  }

}
