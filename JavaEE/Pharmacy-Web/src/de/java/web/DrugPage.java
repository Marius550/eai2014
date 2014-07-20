package de.java.web;

import static de.java.web.util.Util.errorMessage;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import de.java.domain.Drug;
import de.java.domain.replenishment.Position;
import de.java.ejb.DrugService;
import de.java.ejb.ReplenishmentOrderService;
import de.java.web.util.Util;

@ManagedBean
@ViewScoped
public class DrugPage implements Serializable {
  private static final long serialVersionUID = 8259058692232409420L;

  @EJB
  private DrugService drugService;

  @EJB
  private ReplenishmentOrderService orderService;

  private int pzn;
  private Drug drug;

  private long quantity;
  private Date dateOfAction;

  private Collection<Position> pendingReplenishmentOrderPositions;

  public int getPzn() {
    return pzn;
  }

  public void setPzn(int pzn) {
    this.pzn = pzn;
    init();
  }

  private void init() {
    drug = null;
    setQuantity(0);
    setDateOfAction(new Date());
    setPendingReplenishmentOrderPositions(null);
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

  public String submitMasterDataChanges() {
    drug = drugService.updateMasterData(drug.getPzn(), drug.getName(), drug.getDescription());
    return toDrugPage();
  }

  private String toDrugPage() {
    return "/drug/details.xhtml?faces-redirect=true&pzn=" + pzn;
  }

  public String submitReplenishmentConfigChanges() {
    drug = drugService.updateReplenishmentConfig(drug.getPzn(), drug.getMinimumInventoryLevel(), drug.getOptimalInventoryLevel());
    return toDrugPage();
  }

  public String withdraw() {
    if (quantityIsZero()) {
      return toDrugPage();
    }

    try {
      drug = drugService.withdraw(drug.getPzn(), quantity, dateOfAction);
    } catch (EJBException e) {
      String msg = Util.getCausingMessage(e);
      FacesContext.getCurrentInstance().addMessage(null, errorMessage(msg));
      return null;
    }
    return toDrugPage();
  }

  public String restock() {
    if (quantityIsZero()) {
      return toDrugPage();
    }

    drug = drugService.restock(drug.getPzn(), quantity, dateOfAction);
    return toDrugPage();
  }

  public String initiateReplenishment() {
    if (quantityIsZero()) {
      return toDrugPage();
    }

    orderService.initiateReplenishmentForDrug(drug, quantity);
    return toDrugPage();
  }

  private boolean quantityIsZero() {
    return quantity == 0;
  }

  public long getQuantityPending() {
    return getSumOfQuantities(getPendingReplenishmentOrderPositions());
  }

  private long getSumOfQuantities(Collection<Position> positions) {
    long sum = 0;
    for (Position p : positions) {
      sum += p.getQuantity();
    }
    return sum;
  }

  public Drug getDrug() {
    if (drug == null) {
      drug = drugService.getDrug(pzn);
    }
    return drug;
  }

  public void setDrug(Drug drug) {
    this.drug = drug;
  }

  public long getQuantity() {
    return quantity;
  }

  public void setQuantity(long quantity) {
    this.quantity = quantity;
  }

  public Date getDateOfAction() {
    return dateOfAction;
  }

  public void setDateOfAction(Date dateOfAction) {
    this.dateOfAction = dateOfAction;
  }

  public Collection<Position> getPendingReplenishmentOrderPositions() {
    if (pendingReplenishmentOrderPositions == null) {
      pendingReplenishmentOrderPositions = orderService.getPendingPositionsForDrug(pzn);
    }
    return pendingReplenishmentOrderPositions;
  }

  public void setPendingReplenishmentOrderPositions(Collection<Position> pendingReplenishmentOrderPositions) {
    this.pendingReplenishmentOrderPositions = pendingReplenishmentOrderPositions;
  }

}
