package de.java.ejb;

import java.util.Date;

import javax.ejb.Remote;

import de.java.ws.PrescriptionStatisticsDO;
@Remote
public interface PrescriptionStatisticsService {

	/**
	 * Request prescription statistics from JaVa's pharmacy
	 * @param dateFrom Date to request statistics from
	 * @param dateTo Date to request statistics to
	 * @return PrescriptionStatisticsDO-object containing all the statistics
	 */
	PrescriptionStatisticsDO getStatisticsFromJava(Date dateFrom, Date dateTo);

	/**
	 * Request prescription statistics from C.Sharpe's pharmacy
	 * @param dateFrom Date to request statistics from
	 * @param dateTo Date to request statistics to
	 * @return PrescriptionStatisticsDO-object containing all the statistics
	 */
	PrescriptionStatisticsDO getStatisticsFromDotNet(Date dateFrom, Date dateTo);

}