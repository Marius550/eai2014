package de.java.ws;

import java.util.Collection;

import javax.ws.rs.*;

import de.java.ws.MessageCustomer;

/**
 * Interface for customer resource
 */
@Path("customer")
public interface CustomerResource {

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
			@PathParam("id") long id);
	
	/**
	 * Creates a new customer
	 * @param customer MessageObject for a new customer. id, name ... are used
	 */
	@POST
	@Consumes("application/json")
	@Path("create")
	public void createCustomer(MessageCustomer customer);
	
	/**
	 * Updates the name and ... customer
	 * @param id of the customer to update
	 * @param customer MessageCustomer-object containing the new name and description
	 */
	@POST
	@Consumes("application/json")
	@Path("{id}/update")
	public void updateCustomer(@PathParam("id") long id, MessageCustomer customer);
	
	/**
	 * Creates a number of customers via batch create
	 * @param customers array of MessageCustomers, that should be created
	 */
	@POST
	@Consumes("application/json")
	@Path("create/batch")
	public void createCustomers(Collection<MessageCustomer> customer);

}