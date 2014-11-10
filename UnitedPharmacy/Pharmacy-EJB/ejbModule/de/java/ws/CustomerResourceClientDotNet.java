package de.java.ws;

import java.util.Collection;

import javax.ws.rs.*;

/**
 * Simplified client side interface of the dotnet web service
 */
@Path("customer")
public interface CustomerResourceClientDotNet {
	
	/**
	 * Returns all existing customers
	 * @return array of MessageObjects of all available customers
	 */
	@GET
	@Produces({"application/json"})
	public Collection<MessageCustomer> getAllCustomers();
	
	/**
	 * Returns a specific customer
	 * @param id
	 * @return MessageObject for a specific customer
	 */
	@GET
	@Produces("application/json")
	@Path("{id}")
	public MessageCustomer getCustomer(
			@PathParam("id") int id);

	/**
	 * Creates a new drug
	 * @param drug MessageObject for a new drug. pzn, name and description are used
	 */
	/*
	@POST
	@Consumes("application/json")
	@Path("create")
	public void createDrug(MessageDrug drug);
	*/
	
	/**
	 * Updates the name and description of a drug
	 * @param pzn pzn of the drug to update
	 * @param drug MessageDrug-object containing the new name and description
	 */
	/*
	@POST
	@Consumes("application/json")
	@Path("{pzn}/update")
	public void updateDrug(@PathParam("pzn") int pzn, MessageDrug drug);
	*/
	
	/**
	 * Creates a number of drugs via batch create
	 * @param drugs array of MessageDrugs, that should be created
	 */
	/*
	@POST
	@Consumes("application/json")
	@Path("create/batch")
	public void createDrugs(Collection<MessageDrug> drug);
	*/
	
	/**
	 * Updates a number of drugs via batch create
	 * @param drugs array of MessageDrugs, that should be updated
	 */
	/*
	@POST
	@Consumes("application/json")
	@Path("update/batch")
	public void updateDrugs(Collection<MessageDrug> drug);
	*/
}
