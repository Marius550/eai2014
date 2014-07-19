package de.java.ws;

/**
 * Message Drug is an object representing a drug and used for exchange via rest webservices
 * Depending on the usage, only pzn, name and description are filled (for create and update)
 * or all attributes are filled (for get -> displaying a drug)
 */
public class PrescriptionStatisticsDO implements java.io.Serializable {
	private static final long serialVersionUID = -7390541876123905009L;

	private int totalNumberOfPrescriptions;
	private double averageItemsPerPrescription;
	private double averageTimespanToFulfilment;
	
	public PrescriptionStatisticsDO(){
		
	}
	
	// Lots of getters and setters
	public int getTotalNumberOfPrescriptions() {
		return totalNumberOfPrescriptions;
	}

	public void setTotalNumberOfPrescriptions(int totalNumberOfPrescriptions) {
		this.totalNumberOfPrescriptions = totalNumberOfPrescriptions;
	}

	public double getAverageItemsPerPrescription() {
		return averageItemsPerPrescription;
	}

	public void setAverageItemsPerPrescription(double averageItemsPerPrescription) {
		this.averageItemsPerPrescription = averageItemsPerPrescription;
	}

	public double getAverageTimespanToFulfilment() {
		return averageTimespanToFulfilment;
	}

	public void setAverageTimespanToFulfilment(double averageTimespanToFulfilment) {
		this.averageTimespanToFulfilment = averageTimespanToFulfilment;
	}

}