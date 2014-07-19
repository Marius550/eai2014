package de.java.domain.prescription;

import java.io.Serializable;

import de.java.domain.Drug;


public class WrappedItem implements Fulfillable, Serializable {
  private static final long serialVersionUID = -4955900242712940679L;

  private final Item item;
  
  private double prescriptionDrugsPrice;//New

  private final long quantityPending;

  private final long quantityRequired;

  public WrappedItem(Item item, long quantityPending, long quantityRequired) {
    this.item = item;
    this.quantityPending = quantityPending;
    this.quantityRequired = quantityRequired;
  }
  
  public double getPrescriptionDrugsPrice() { //New
	  
	  int i = 150;
	  int j = 100;
	  prescriptionDrugsPrice = i + j;
	  
	  return prescriptionDrugsPrice;
  }

  public Item getItem() {
    return item;
  }

  public FulfilmentState getState() {
    return item.getState();
  }

  public Drug getPrescribedDrug() {
    return item.getPrescribedDrug();
  }

  public long getQuantityPending() {
    return quantityPending;
  }

  public long getQuantityRequired() {
    return quantityRequired;
  }

  @Override
  public boolean isFulfilled() {
    return item.isFulfilled();
  }
}
