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
	
	private PrescriptionStatisticsDO javaDO = null;
	private PrescriptionStatisticsDO dotNetDO = null;
	private Date dateFrom = new Date();
	private Date dateTo = new Date();

	private PrescriptionStatisticsDO getJavaDO() {
		return javaDO;
	}
	
	private PrescriptionStatisticsDO getDotNetDO() {
		return dotNetDO;
	}

	public int getTotalNumberOfPrescriptionsInJava() {
		if (getJavaDO() == null){
			return -1;
		}
		return getJavaDO().getTotalNumberOfPrescriptions();
	}
	
	public double getAverageNumberOfItemsInJava() {
		if (getJavaDO() == null){
			return -1;
		}
		return 99;
		//return getJavaDO().getAverageItemsPerPrescription();
	}
	
	public double getAverageTimespanOfFulfilmentInJava() {
		if (getJavaDO() == null){
			return -1;
		}
		return getJavaDO().getAverageTimespanToFulfilment();
	}
	
	public int getTotalNumberOfPrescriptionsInDotNet() {
		if (getDotNetDO() == null){
			return -1;
		}
		return getDotNetDO().getTotalNumberOfPrescriptions();
	}
	
	public double getAverageNumberOfItemsInDotNet() {
		if (getDotNetDO() == null){
			return -1;
		}
		return getDotNetDO().getAverageItemsPerPrescription();
	}
	
	public double getAverageTimespanOfFulfilmentInDotNet() {
		if (getDotNetDO() == null){
			return -1;
		}
		return getDotNetDO().getAverageTimespanToFulfilment();
	}
	
	public void requestStatistics(){
		javaDO = prescriptionStatisticsService.getStatisticsFromJava(dateFrom, dateTo);
		dotNetDO = javaDO = prescriptionStatisticsService.getStatisticsFromDotNet(dateFrom, dateTo);
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
}
