package de.java.ws;

import javax.ws.rs.*;

/**
 * Simplified client side interface of the dotnet web service
 */
@Path("prescriptionStatistics")
public interface PrescriptionStatisticsClientDotNet {
	
	@GET
	@Produces({"application/json"})
	@Consumes({"application/json"})
	public PrescriptionStatisticsDO getPrescriptionStatistics(Object obj);
}
