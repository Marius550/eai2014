package de.java.ws;

import java.util.Collection;

import javax.ws.rs.*;

/**
 * Client side interface of the drug web service
 */
@Path("drug")
public interface DrugResourceClientJava {
	
	/**
	 * Returns all existing drugs
	 * @return array of MessageObjects of all available drugs
	 */
	@GET
	@Produces({"application/json"})
	public Collection<MessageDrug> getAllDrugs();
	
	/**
	 * Returns a specific drug
	 * @param pzn
	 * @return MessageObject for a specific drug
	 */
	@GET
	@Produces("application/json")
	@Path("{pzn}")
	public MessageDrug getDrug(
			@PathParam("pzn") int pzn);

	/**
	 * Creates a new drug
	 * @param drug MessageObject for a new drug. pzn, name and description are used
	 */
	@POST
	@Consumes("application/json")
	@Path("create")
	public void createDrug(MessageDrug drug);
	
	/**
	 * Updates the name and description of a drug
	 * @param pzn pzn of the drug to update
	 * @param drug MessageDrug-object containing the new name and description
	 */
	@POST
	@Consumes("application/json")
	@Path("{pzn}/update")
	public void updateDrug(@PathParam("pzn") int pzn, MessageDrug drug);
	
	/**
	 * Creates a number of drugs via batch create
	 * @param drugs array of MessageDrugs, that should be created
	 */
	@POST
	@Consumes("application/json")
	@Path("create/batch")
	public void createDrugs(Collection<MessageDrug> drugs);
}
