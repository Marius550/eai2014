package de.java.ws;

/**
 * Data Object used for exchange of prescription statistics between the pharmacies in a determined span of dates.
 * 
 */
public class PrescriptionStatisticsDO implements java.io.Serializable {
	private static final long serialVersionUID = -7390541876123905009L;

	private int totalNumberOfPrescriptions;
	private double averageNumberOfItemsPerPrescription;
	private double averageTotalpricePerPrescription;
	private long averageTimeSpanOfFulfilment; // in seconds
	
	/**
	 * Initializes data object with default values. 
	 * Uses -1 for averageNumberOfItemsPerPrescription and averageTimeSpanOfFulfilment 
	 * to show they are not valid.
	 */
	public PrescriptionStatisticsDO(){
		totalNumberOfPrescriptions = 0;
		averageNumberOfItemsPerPrescription = -1;
		averageTotalpricePerPrescription = -1;
		averageTimeSpanOfFulfilment = -1;
	}

	/**
	 * Total number of prescriptions in a pharmacy
	 * @return total number of prescriptions  
	 */
	public int getTotalNumberOfPrescriptions() {
		return totalNumberOfPrescriptions;
	}
	public void setTotalNumberOfPrescriptions(int totalNumberOfPrescriptions) {
		this.totalNumberOfPrescriptions = totalNumberOfPrescriptions;
	}
	
	/**
	 * Average number of items per prescription. 
	 * Only looks at the prescription with at least 1 drug in the prescription.
	 * @return average number of items per prescription
	 */
	public double getAverageNumberOfItemsPerPrescription() {
		return averageNumberOfItemsPerPrescription;
	}
	public void setAverageNumberOfItemsPerPrescription(
			double averageNumberOfItemsPerPrescription) {
		this.averageNumberOfItemsPerPrescription = averageNumberOfItemsPerPrescription;
	}
	
	/**
	 * Average total price per prescription. 
	 * Only looks at the prescription with at least 1 drug in the prescription.
	 * @return average number of total price per prescription
	 */
	public double getAverageTotalpricePerPrescription() {
		return averageTotalpricePerPrescription;
	}
	public void setAverageTotalpricePerPrescription(double averageTotalpricePerPrescription) {
		this.averageTotalpricePerPrescription = averageTotalpricePerPrescription;
	}
	
	/**
	 * Average timespan between date of entry and date of fulfilment in seconds
	 * Only looks at fulfilled prescriptions.
	 * @return average timespan to fulfilment in seconds
	 */
	public long getAverageTimeSpanOfFulfilment() {
		return averageTimeSpanOfFulfilment;
	}
	public void setAverageTimeSpanOfFulfilment(long averageTimeSpanOfFulfilment) {
		this.averageTimeSpanOfFulfilment = averageTimeSpanOfFulfilment;
	}
}