package de.java.ejb;

import java.util.Collection;
import java.util.Date;

import javax.ejb.Remote;

import de.java.domain.prescription.Item;
import de.java.domain.prescription.Prescription;
import de.java.domain.prescription.PrescriptionState;
import de.java.domain.prescription.WrappedItem;

@Remote
public interface PrescriptionService {

  Collection<Prescription> getAllPrescriptions();
  
  Collection<Prescription> getPrescriptionsInState(
      PrescriptionState filterForState);

  Prescription getPrescription(long id);
  
  Prescription getPrescriptionWithItems(long id);

  void updateEntryData(long id, String issuer, String diagnosis, Date issueDate, Date entryDate);

  void cancel(long id);

  void addNewItem(long prescriptionId, int itemPzn);

  void removeItem(long itemId);

  void fulfil(long itemId);

  void replenish(WrappedItem item);

  void returnToPreviousState(long id);

  void proceedToNextState(long id);

  Collection<WrappedItem> wrapItems(Collection<Item> items);

  void updateFulfilmentDate(long id, Date fulfilmentDate);

  public long getQuantityUnfulfilledForDrug(int pzn);
  
  public double test();//New
}
