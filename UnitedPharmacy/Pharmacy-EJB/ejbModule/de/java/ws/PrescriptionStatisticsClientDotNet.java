package de.java.ws;

import javax.ws.rs.*;

/**
 * Simplified client side interface of the dotnet web service
 */
@Path("statistic")
public interface PrescriptionStatisticsClientDotNet {
	
	/**
	 * Requests prescription statistics from C.Sharpe
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 */
	@GET
	@Produces({"application/json"})
	@Path("{dateFrom}/{dateTo}")
	public PrescriptionStatisticsDO getPrescriptionStatistics(@PathParam("dateFrom") String dateFrom, @PathParam("dateTo") String dateTo);
}
