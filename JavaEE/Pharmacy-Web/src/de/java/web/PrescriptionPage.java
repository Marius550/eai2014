package de.java.web;

import static de.java.web.util.Util.errorMessage;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;

import de.java.domain.Drug;
import de.java.domain.prescription.Item;
import de.java.domain.prescription.Prescription;
import de.java.domain.prescription.PrescriptionState;
import de.java.domain.prescription.WrappedItem;
import de.java.ejb.DrugService;
import de.java.ejb.PrescriptionService;
import de.java.web.util.Util;

@ManagedBean
@ViewScoped
public class PrescriptionPage implements Serializable {

  private static final long serialVersionUID = -1321629547690677910L;

  @EJB
  private PrescriptionService prescriptionService;

  @EJB
  private DrugService drugService;

  private long id;
  private Prescription prescription;

  private int newPzn;
  private Drug newItemDrug;

  private Collection<WrappedItem> wrappedItems;
  
  private Map<Long, Item> PrescriptionItems;
  
  private Date fulfilmentDate = new Date();

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
    init();
  }

  public void init() {
    prescription = null;
    wrappedItems = null;
    newPzn = 0;
  }

  public void ensureInitialized(){
    try{
      if(getPrescription() != null)
        // Success
        return;
    } catch(EJBException e) {
      e.printStackTrace();
    }
    Util.redirectToRoot();
  }

  public String update() {
    prescriptionService.updateEntryData(prescription.getId(),
        prescription.getIssuer(),
        prescription.getIssueDate(),
        prescription.getEntryDate());
    return toPrescriptionPage();
  }
  
  private String toPrescriptionPage() {
    return "details.xhtml?faces-redirect=true&id=" + id;
  }

  public String returnToPrevious() {
    prescriptionService.returnToPreviousState(getPrescription().getId());
    return toPrescriptionPage();
  }

  public boolean isProceedable() {
    return getPrescription().getState().isProceedable(getPrescription().getItems());
  }

  public String proceed() {
    if (getPrescription().getState() == PrescriptionState.FULFILLING) {
      prescriptionService.updateFulfilmentDate(getPrescription().getId(), getFulfilmentDate());
    }
    prescriptionService.proceedToNextState(getPrescription().getId());
    return toPrescriptionPage();
  }

  public String cancel() {
    prescriptionService.cancel(prescription.getId());
    return toPrescriptionList();
  }
  
  public int getAmountOfItemsInPrescription() {
	  return prescriptionService.getPrescriptionWithItems(id).getItems().size();
  }
  
  public long getPrescriptionId() {
	  return prescriptionService.getPrescription(id).getId();
  }
  
  /**
   * Convert Collection <Item> into HashMap and assign id to each map element
 * @param collection
 * @return Map<Long, Item> map
 */
private Map<Long, Item> convertPharmacyItemsCollectionIntoHashMap(Collection<Item> collection){
	  Map<Long, Item> map = new HashMap<Long, Item>();
	  for (Item mItem : collection){
		  map.put(mItem.getId(), mItem);
	  }
	  return map;
  }

public Map<Long, Item> getAllPrescriptionItemsToHashMap(){
	  if (PrescriptionItems == null){
		  PrescriptionItems = convertPharmacyItemsCollectionIntoHashMap(prescriptionService.getPrescriptionWithItems(id).getItems());
	  }
	  return PrescriptionItems;
}

public double getPriceOfPrescriptionItems(){
	  double itemPrice = 0;
	  for (Item i : prescriptionService.getPrescriptionWithItems(id).getItems()){
		  // Iterate only through displayed itmes in order to display right sums
		  if (getAllPrescriptionItemsToHashMap().get(i.getId()) != null) {
			  itemPrice += getAllPrescriptionItemsToHashMap().get(i.getId()).getPrescribedDrug().getPrice();
		  }
	  }
	  return itemPrice;
}

  private String toPrescriptionList() {
    return "list.xhtml";
  }

  public String remove(Item item) {
    prescriptionService.removeItem(item.getId());
    return toPrescriptionPage();
  }

  public void addNewItem() {
    try {
      prescriptionService.addNewItem(getPrescription().getId(), getNewPzn());
    } catch (EJBException e) {
      String msg = Util.getCausingMessage(e);
      FacesContext.getCurrentInstance().addMessage(null, errorMessage(msg));
    }
    init();
  }

  public Prescription getPrescription() {
    if (prescription == null) {
      prescription = prescriptionService.getPrescriptionWithItems(id);
    }
    return prescription;
  }
  
  public int getNewPzn() {
    return newPzn;
  }
  
  public void setNewPzn(int newPzn) {
    this.newPzn = newPzn;
    newItemDrug = null;
  }
  
  public Drug getNewItemDrug() {
    if (newItemDrug == null) {
      try {
        newItemDrug = drugService.getDrug(getNewPzn());
      } catch (EJBException e) {
        if (!(e.getCausedByException() instanceof NoResultException)) {
          throw e;
        }
      }
    }
    return newItemDrug;
  }

  public String fulfil(WrappedItem item) {
    prescriptionService.fulfil(item.getItem().getId());
    return toPrescriptionPage();
  }

  public String replenish(WrappedItem item) {
    prescriptionService.replenish(item);
    return toPrescriptionPage();
  }

  public Collection<WrappedItem> getWrappedItems() {
    if (wrappedItems == null) {
      wrappedItems = prescriptionService.wrapItems(prescription.getItems());
    }
    return wrappedItems;
  }

  public Date getFulfilmentDate() {
    return fulfilmentDate;
  }

  public void setFulfilmentDate(Date fulfilmentDate) {
    this.fulfilmentDate = fulfilmentDate;
  }

//testing...
//  public void PrintToConsole() {
//	  System.out.println("Size: " + prescriptionService.getPrescriptionWithItems(10).getItems().size());
//	  
//		Object[] list = prescriptionService.getPrescriptionWithItems(10).getItems().toArray();
//		
//		for (int j = 0; j < list.length; j++) {
//			System.out.println(list[j]);
//		}
//		System.out.println(list);
//  }
  
}
