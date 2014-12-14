package de.java.web;

import java.io.Serializable;
import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import de.java.domain.prescription.Prescription;
import de.java.domain.prescription.PrescriptionState;
import de.java.ejb.PrescriptionService;

@ManagedBean
public class PrescriptionList implements Serializable {

  private static final long serialVersionUID = -8573278652619706897L;

  @EJB
  private PrescriptionService prescriptionService;

  private PrescriptionState filterForState = null;
  
  //private Map<Long, Prescription> Prescriptions;

  public Collection<Prescription> getPrescriptions() {
    if (filterForState != null) {
      return prescriptionService.getPrescriptionsInState(filterForState);
    }
    return prescriptionService.getAllPrescriptions();
  }

  public PrescriptionState getFilterForState() {
    return filterForState;
  }

  public void setFilterForState(PrescriptionState filterForState) {
    this.filterForState = filterForState;
  }
  
  public void getPrescriptionsForCustomer() {
	  prescriptionService.getPrescriptionsForCustomer(7);
  }
  
}

///**Transfer the prescription collection into a HashMap
//* @param collection
//* @return
//*/
//private Map<Long, Prescription> convertPharmacyPrescriptionCollectionIntoHashMap(Collection<Prescription> collection){
//  Map<Long, Prescription> map = new HashMap<Long, Prescription>();
//  for (Prescription mPrescription : collection){
//	  map.put(mPrescription.getId(), mPrescription);
//  }
//  return map;
//}
//
//public Map<Long, Prescription> getAllPrescriptionsToHashMap(){
//  if (Prescriptions == null){
//	  Prescriptions = convertPharmacyPrescriptionCollectionIntoHashMap(prescriptionService.getAllPrescriptions());
//  }
//  return Prescriptions;
//}

//public double getTotalPriceOfPrescription(long id){
//  
//double totalPrice = 0;
//	for (Prescription p : prescriptionService.getAllPrescriptions()){
//		// Iterate only through displayed items in order to display right sums
//		if (getAllPrescriptionsToHashMap().get(p.getId()) != null) {
//			  
//			getAllPrescriptionsToHashMap().get(p.getId()).setTotalPrice(id);
//			totalPrice = getAllPrescriptionsToHashMap().get(p.getId()).getTotalPrice();
//		  }  
//	  }
//	  return totalPrice;
//}
