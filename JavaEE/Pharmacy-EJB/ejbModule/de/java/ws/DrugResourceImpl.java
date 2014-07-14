package de.java.ws;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.java.domain.Drug;
import de.java.ejb.DrugService;
import de.java.ejb.PrescriptionService;
import de.java.ejb.ReplenishmentOrderService;

@Stateless
public class DrugResourceImpl implements DrugResource {
	
	@EJB
	private DrugService drugService;
	
	@EJB
	private ReplenishmentOrderService replenishmentOrderService;
	
	@EJB
	private PrescriptionService prescriptionService;

	public Collection<MessageDrug> getAllDrugs(){
		Collection<Drug> drugs = drugService.getAllDrugs();

		Collection<MessageDrug> collection = new ArrayList<MessageDrug>();
		for (final Drug d : drugs){
			collection.add(setupDrug(d));
		}
		
		return collection;
	}
	
	public MessageDrug getDrug(int pzn){
		Drug drug = drugService.getDrug(pzn);
		
		return setupDrug(drug);
	}
	
	@Override
	public void createDrug(MessageDrug sDrug){
		drugService.createDrug(sDrug.convertToDrug());
	}

	@Override
	public void updateDrug(int pzn, MessageDrug sDrug) {
		drugService.updateMasterData(pzn, sDrug.getName(), sDrug.getPrice(), sDrug.getDescription());		
	}
	
	@Override
	public void createDrugs(Collection<MessageDrug> drugs){
		for (MessageDrug d : drugs){
			drugService.createDrug(d.convertToDrug());
		}
	}
	
	/**
	 * Creates a message drug from a Drug object
	 * Includes master data, replenishment configuration and pending and unfulfilled quantities
	 * @param d
	 * @return
	 */
	private MessageDrug setupDrug(Drug d){
		// Sets up a message drug with everything but the pending quantity and unfulfilled quantity
		MessageDrug tmpDrug = new MessageDrug(d);
		// Sets up the rest
		tmpDrug.setPendingQuantity(replenishmentOrderService.getQuantityPendingForDrug(d.getPzn()));
		tmpDrug.setUnfulfilledQuantity(prescriptionService.getQuantityUnfulfilledForDrug(d.getPzn()));
		return tmpDrug;
	}

}
