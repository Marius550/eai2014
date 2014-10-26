package de.java.web;

import java.io.Serializable;
import java.util.Date;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import de.java.ejb.PrescriptionStatisticsService;
import de.java.ws.PrescriptionStatisticsDO;

@ManagedBean
@ViewScoped
public class PrescriptionStatisticsPage implements Serializable {
	private static final long serialVersionUID = 8259012392232409420L;

	@EJB
	PrescriptionStatisticsService prescriptionStatisticsService;
	
	// Date objects for statistics. 
	// As they are initialized on page displaying
	// and at least empty PrescriptionStatisticsDO are returned, they are never null
	private PrescriptionStatisticsDO javaDO;
	private PrescriptionStatisticsDO dotNetDO;
	// dateFrom is initialized on 1970-01-01
	private Date dateFrom = new Date(0);
	// dateTo is initialized on current day
	private Date dateTo = new Date();
	
	// Determines the error state
	// Is either "dateInsertion" if something failed during date entry
	// or a pharmacies name if requesting statistics from a subsidiary failed.
	private String errorAt;

	/**
	 * Initializes statistics page
	 */
	public void ensureInitialized(){
		requestStatistics();
	}

	// Getters for adequate representation in html-page
	public String getTotalNumberOfPrescriptionsInJava() {
		return formatInt(javaDO.getTotalNumberOfPrescriptions());
	}
	public String getAverageNumberOfItemsInJava() {
		return formatDouble(javaDO.getAverageNumberOfItemsPerPrescription());
	}
	public String getAverageTotalpricePerPrescriptionInJava() {
		return formatDouble(javaDO.getAverageTotalpricePerPrescription());
	}
	public String getAverageTimespanOfFulfilmentInJava() {
		return formatTimeStamp(javaDO.getAverageTimeSpanOfFulfilment());
	}
	
	public String getTotalNumberOfPrescriptionsInDotNet() {
		return formatInt(dotNetDO.getTotalNumberOfPrescriptions());
	}
	public String getAverageNumberOfItemsInDotNet() {
		return formatDouble(dotNetDO.getAverageNumberOfItemsPerPrescription());
	}
	public String getAverageTimespanOfFulfilmentInDotNet() {
		return formatTimeStamp(dotNetDO.getAverageTimeSpanOfFulfilment());
	}
	public String getAverageTotalpricePerPrescriptionInDotNet() {
		return formatDouble(dotNetDO.getAverageTotalpricePerPrescription());
	}
	
	/**
	 * Requests statistics from both subsidiaries
	 */
	public void requestStatistics(){
		errorAt = null;		
		// Set default dates, if one of the fields is null
		dateFrom = (dateFrom == null) ? new Date(0) : dateFrom;
		dateTo = (dateTo == null) ? new Date() : dateTo;
		
		// Check for valid dates
		if (dateFrom.compareTo(dateTo) > 0){
			// "DateFrom" is after "DateTo"
			errorAt = "dateInsertion";
			return;
		};
		
		// Request statistics from JaVa
		javaDO = prescriptionStatisticsService.getStatisticsFromJava(dateFrom, dateTo);
		if (javaDO == null){
			errorAt = "JaVa's pharmacy";
			javaDO = new PrescriptionStatisticsDO();
		}
		// Request statistics from C.Sharpe
		dotNetDO = prescriptionStatisticsService.getStatisticsFromDotNet(getDateFrom(), dateTo);
		if (dotNetDO == null){
			errorAt = errorAt == null ? "C. Sharpe's pharmacy" : "both pharmacies";
			dotNetDO = new PrescriptionStatisticsDO();
		}
	}
	
	/**
	 * Formats a double value to adequate representation. Rounds to 4 digits behind comma.
	 * Returns "n/a", if value is invalid (= -1)
	 * @param value
	 * @return
	 */
	private String formatDouble (double value){
		if (value == -1) return "n/a";
		return "" + (double) Math.round(value * 10000) / 10000;
	}
	
	/**
	 * Formats an integer to adequate representation. Only converts it to a string.
	 * @param value
	 * @return
	 */
	private String formatInt (int value){
		return "" + value;
	}
	
	/**
	 * Formats a timestamp in seconds to an adequate representation. 
	 * Returns "n/a", if value is invalid (= -1)
	 * @param value
	 * @return timestamp in format "1d 10h 32m 12s"
	 */
	private String formatTimeStamp(long value){
		if (value == -1) return "n/a"; 
		String s = "";
		s += (value / 86400) + "d ";
		value = value % 86400;
		s += (value / 3600) + "h ";
		value = value % 3600;
		s += (value / 60) + "m ";
		value = value % 60;
		s += value + "s ";
		return s;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}
	
	public boolean isError(){
		return errorAt != null;
	}
	
	/**
	 * Creates an error message for displaying to user.
	 * @return
	 */
	public String getErrorMessage (){
		if (errorAt == null) return null;
		if (errorAt == "dateInsertion") return "'Date to' needs to be after 'Date from'";
		return "Fetching data from " + errorAt + " failed!";
	}
}
