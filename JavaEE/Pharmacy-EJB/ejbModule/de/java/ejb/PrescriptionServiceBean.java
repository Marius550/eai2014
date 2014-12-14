package de.java.ejb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.java.domain.Drug;
import de.java.domain.prescription.FulfilmentState;
import de.java.domain.prescription.Item;
import de.java.domain.prescription.Prescription;
import de.java.domain.prescription.PrescriptionState;
import de.java.domain.prescription.WrappedItem;
import de.java.ws.MessageCustomer;

@Stateless
public class PrescriptionServiceBean implements PrescriptionService {

  @PersistenceContext
  private EntityManager em;

  @EJB
  private DrugService drugService;
  
  @EJB
  private CustomerService customerService;

  @EJB
  private ReplenishmentOrderService replenishmentOrderService;

  @Override
  public Collection<Prescription> getAllPrescriptions() {
    return em.createQuery("FROM Prescription p ORDER BY p.state",
        Prescription.class).getResultList();
  }

  @Override
  public Collection<Prescription> getPrescriptionsInState(PrescriptionState state) {
    final String query = "FROM Prescription p WHERE p.state = :state";
    return em
        .createQuery(query, Prescription.class)
        .setParameter("state", state)
        .getResultList();
  }
   
  //Exam practice
  @Override
  public void getPrescriptionsForCustomer(long id) {
	  final String query = "FROM Prescription p JOIN p.customer c WHERE c.id = :id";
	    int i =  em	.createQuery(query, Prescription.class)
	    			.setParameter("id", id)
	    			.getResultList().size();
	    System.out.println("int i: " + i);
  }
  

  
  
  //Exam practice
  /*
public Collection<Invoice> getAllUnpaidInvoicesForCustomer(Customer c) {
	final String query = "FROM Invoice i JOIN i.customer c WHERE c.id = :customerId AND i.paymentStatus = 0"; // 0 represents unpaid
	return em .createQuery(query, Invoice.class)
		.setParameter("customerId", c.getId())
		.getResultList();
}
  */
  
  
  /*
  public Collection<Invoice> getAllUnpaidInvoicesForCustomer(Customer c) {
		final String query = "FROM Invoice i JOIN i.customer c WHERE c.id = :customerId AND i.paymentStatus = 0"; // 0 represents unpaid
		return em .createQuery(query, Invoice.class)
			.setParameter("customerId", c.getId())
			.getResultList();
	}
	*/

  @Override
  public Prescription getPrescription(long id) {
    return em.find(Prescription.class, id);
  }
  
  @Override
  public Prescription getPrescriptionWithItems(long id) {
    return forceLoadOfItems(getPrescription(id));
  }

  private Prescription forceLoadOfItems(Prescription prescription) {
    if (prescription == null) return prescription;
    prescription.getItems().size();
    return prescription;
  }

  //Getting the input parameters from prescriptionPage.update()
  //updateEntryData() takes these parameters and sets them for the prescription of a certain Id
  @Override
  public void updateEntryData(long id, String issuer, Date issueDate, Date entryDate, double totalPrice) {
    Prescription p = getPrescription(id);
    p.setIssuer(issuer);
    p.setIssueDate(issueDate);
    p.setEntryDate(entryDate);
    p.setTotalPrice(totalPrice);
  }
  
//  @Override
//  public void updateTotalPrice(long id, double evaluation) {
//    Prescription p = getPrescription(id);
//    p.setEvaluation(evaluation);
//  }

  @Override
  public void cancel(long id) {
    Prescription prescription = getPrescription(id);
    if (prescription.getState().isCancellable()) {
      em.remove(prescription);
    }
  }

  @Override
  public void addNewItem(long prescriptionId, int itemPzn) {
    Prescription p = getPrescription(prescriptionId);
    Drug drug = drugService.getDrug(itemPzn);
    validateDrugNotInPrescription(p, drug);
    
    em.persist(new Item(drug, p));
  }

  private void validateDrugNotInPrescription(Prescription prescription, Drug drug) {
    for (Item item : prescription.getItems()) {
      if (item.getPrescribedDrug().getPzn() == drug.getPzn()) {
        throw new IllegalArgumentException(drug + " already present in this prescription");
      }
    }
  }

  @Override
  public void removeItem(long itemId) {
    Item item = getItem(itemId);
    Prescription p = item.getPrescription();

    validateEntryState(p);

    p.getItems().remove(item);
    em.remove(item);
  }

  private Item getItem(long itemId) {
    return em.find(Item.class, itemId);
  }

  private void validateEntryState(Prescription p) {
    if (p.getState() != PrescriptionState.ENTRY) {
      throw new IllegalStateException("Cannot remove items from non-ENTRY prescriptions");
    }
  }

  @Override
  public void fulfil(long itemId) {
    Item item = getItem(itemId);
    drugService.withdraw(item.getPrescribedDrug().getPzn(), 1, new Date());
    item.setState(FulfilmentState.FULFILLED);
  }

  @Override
  public void replenish(WrappedItem item) {
    replenishmentOrderService.initiateReplenishmentForDrug(item.getPrescribedDrug(), item.getQuantityRequired());
  }

  @Override
  public void returnToPreviousState(long id) {
    Prescription p = getPrescription(id);
    p.setState(p.getState().getPrevious());
  }

  @Override
  public void proceedToNextState(long id) {
    Prescription p = getPrescription(id);
    p.setState(p.getState().getNext());
  }

  @Override
  public Collection<WrappedItem> wrapItems(Collection<Item> items) {
    Collection<WrappedItem> wrappedItems = new ArrayList<>();
    for (Item i : items) {
      long quantityPending = replenishmentOrderService.getQuantityPendingForDrug(i.getPrescribedDrug().getPzn());
      long quantityRequired = getQuantityRequired(i, quantityPending);
      wrappedItems.add(new WrappedItem(i, quantityPending, quantityRequired));
    }
    return wrappedItems;
  }

  private long getQuantityRequired(Item item, long quantityPending) {
    long optimalInventoryLevel = item.getPrescribedDrug().getOptimalInventoryLevel();
    long quantityUnfulfilled = getQuantityUnfulfilledForDrug(item.getPrescribedDrug().getPzn());
    long currentStock = item.getPrescribedDrug().getStock();
    long quantityRequired = (optimalInventoryLevel + quantityUnfulfilled) - (currentStock + quantityPending);
    return Math.max(0, quantityRequired);
  }

  public long getQuantityUnfulfilledForDrug(int pzn) {
    String query = "SELECT COUNT(i)"
        + " FROM Item i"
        + " JOIN i.prescription p"
        + " JOIN i.prescribedDrug d"
        + " WHERE d.pzn = :pzn"
        + " AND i.state = de.java.domain.prescription.FulfilmentState.UNFULFILLED"
        + " AND ("
        + "   p.state = de.java.domain.prescription.PrescriptionState.FULFILLING"
        + "   OR p.state = de.java.domain.prescription.PrescriptionState.CHECKING"
        + " )";
    return em.createQuery(query, Long.class)
        .setParameter("pzn", pzn)
        .getSingleResult();
  }

  @Override
  public void updateFulfilmentDate(long id, Date fulfilmentDate) {
    getPrescription(id).setFulfilmentDate(fulfilmentDate);
  }
  	
  	
	/**
	 * Looks up all prescriptions from the database entered between dateFrom and dateTo.
	 * @param dateFrom
	 * @param dateTo
	 * @return A Collection of all prescriptions with entryDate between dateFrom and dateTo.
	 */
	private Collection<Prescription> getPrescriptionsBetweenDates(Date dateFrom, Date dateTo) {
		//incrementing dateTo by one, so prescriptions created on dateTo are included
		Calendar c = Calendar.getInstance(); 
		c.setTime(dateTo); 
		c.add(Calendar.DATE, 1);
		Date newDateTo = c.getTime();
	    final String query = "FROM Prescription p WHERE p.entryDate >= :dateFrom AND p.entryDate < :newDateTo";
	    return em
	        .createQuery(query, Prescription.class)
	        .setParameter("dateFrom", dateFrom)
	        .setParameter("newDateTo", newDateTo)
	        .getResultList();
	}
	
	/**
	 * Looks up all fulfilled prescriptions from the database entered between dateFrom and dateTo.
	 * @param dateFrom
	 * @param dateTo
	 * @return A Collection of all prescriptions with entryDate between dateFrom and dateTo and state "FULFILLED".
	 */
	private Collection<Prescription> getFulfilledPrescriptionsBetweenDates(Date dateFrom, Date dateTo) {
		//incrementing dateTo by one, so prescriptions created on dateTo are included
		Calendar c = Calendar.getInstance(); 
		c.setTime(dateTo); 
		c.add(Calendar.DATE, 1);
		Date newDateTo = c.getTime();
	    final String query = "FROM Prescription p WHERE p.entryDate >= :dateFrom AND p.entryDate < :newDateTo AND p.state = de.java.domain.prescription.PrescriptionState.FULFILLED";
	    return em
	        .createQuery(query, Prescription.class)
	        .setParameter("dateFrom", dateFrom)
	        .setParameter("newDateTo", newDateTo)
	        .getResultList();
}

	@Override
	public int getNumberPrescriptionsBetweenDates(Date dateFrom, Date dateTo){
		int number = getPrescriptionsBetweenDates(dateFrom, dateTo).size();
		return number;
	}
	
	@Override
	public double getAvItemNumberBetweenDates (Date dateFrom, Date dateTo){
		int itemCounter = 0;
		int numberPrescNotEmpty = 0;
		Collection<Prescription> prescriptions = getPrescriptionsBetweenDates(dateFrom, dateTo);
		for(Prescription presc : prescriptions){
			if (presc.getItems().size() > 0){
			itemCounter += presc.getItems().size();
			numberPrescNotEmpty++;
			}
		}
		double average = -1;
		if (numberPrescNotEmpty > 0){
			average = (double) itemCounter / (double) numberPrescNotEmpty;
		}
		return average;
	}
	
	@Override
	public double getAvTotalPriceBetweenDates (Date dateFrom, Date dateTo){
		//int itemCounter = 0;
		double totalPriceSum = 0;
		int numberPrescNotEmpty = 0;
		Collection<Prescription> prescriptions = getPrescriptionsBetweenDates(dateFrom, dateTo);
		for(Prescription presc : prescriptions){
			if (presc.getItems().size() > 0){
				totalPriceSum = totalPriceSum + presc.getTotalPrice();
			//itemCounter += presc.getItems().size();
			numberPrescNotEmpty++;
			}
		}
		double average = -1;
		if (numberPrescNotEmpty > 0){
			average = ((double) totalPriceSum / (double) numberPrescNotEmpty);
		}
		return average;
	}
	
	@Override
	public long getAvFulfillmentTimesBetweenDates (Date dateFrom, Date dateTo){
		long time = 0;
		Collection<Prescription> prescriptions = getFulfilledPrescriptionsBetweenDates(dateFrom, dateTo);
		for(Prescription presc : prescriptions){
			time += (presc.getFulfilmentDate().getTime()-presc.getEntryDate().getTime());
		}
		long average = -1;
		if (prescriptions.size() > 0){
			average = ((time / 1000) / prescriptions.size());
		}
		return average;
	}


}
