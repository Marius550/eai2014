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
  
  //Exam practice
  void getPrescriptionsForCustomer(long id);

  Prescription getPrescription(long id);
  
  Prescription getPrescriptionWithItems(long id);

  void updateEntryData(long id, String issuer, Date issueDate, Date entryDate, double totalPrice);

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
  
  /**
   * Looks up and returns the amount of prescriptions in the database that were entered between the dates dateFrom and dateTo.
   * @param dateFrom
   * @param dateTo
   * @return The number of prescriptions between the dates.
   */
  int getNumberPrescriptionsBetweenDates(Date dateFrom, Date dateTo);
  
  /**
   * Calculates the average number of items per prescription between dateFrom and dateTo.
   * Only prescriptions with at least one item are considered.
   * @param dateFrom
   * @param dateTo
   * @return The average number of items as a double.
   */
  double getAvItemNumberBetweenDates (Date dateFrom, Date dateTo);
  
  /**
   * Calculates the average total price per prescription between dateFrom and dateTo.
   * Only prescriptions with at least one item are considered.
   * @param dateFrom
   * @param dateTo
   * @return The average number of total price as a double.
   */
  double getAvTotalPriceBetweenDates (Date dateFrom, Date dateTo);
  
  /**
   * Calculates average timespan of fulfilment (from date of entry to date of fulfilment) per prescription.
   * Only prescriptions in Fulfilled state considered.
   * @param dateFrom
   * @param dateTo
   * @return The average timespan of fulfilment in seconds.
   */
  long getAvFulfillmentTimesBetweenDates (Date dateFrom, Date dateTo);
  
  
  
}
