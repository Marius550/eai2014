package de.java.ws;

import de.java.domain.customer.Customer;

/**
 * Message Customer is an object representing a customer and used for exchange via rest webservices
 * Depending on the usage, only id, name and ... are filled (for create and update) (Not yet)
 * or all attributes are filled (for get -> displaying a customer)
 */ 

public class MessageCustomer implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String name;
	private String address;
	private String birthDate;
	private String email;	
	private String telephoneNumber;
	private double prescriptionBill;

	public MessageCustomer(){
		
	}
	/**
	 * Creates a simplified customer based on the master data of a customer. Uses all attributes in a customer, i.e.
	 * id, name, ...
	 * @param customer
	 */
	public MessageCustomer(Customer customer){
		if (customer == null){
			return;
		}
		this.id = customer.getId();
		this.name = customer.getName();
		this.address = customer.getAddress();
		this.birthDate = customer.getBirthDate();
		this.email = customer.getEmail();
		this.telephoneNumber = customer.getTelephoneNumber();
		this.prescriptionBill = customer.getPrescriptionBill();
	}
	
	  public long getId() {
		    return id;
		  }

		  public void setId(long id) {
		    this.id = id;
		  }

		  public String getName() {
		    return name;
		  }

		  public void setName(String name) {
		    this.name = name;
		  }

		  public String getTelephoneNumber() {
		    return telephoneNumber;
		  }

		  public void setTelephoneNumber(String telephoneNumber) {
		    this.telephoneNumber = telephoneNumber;
		  }

		  public String getAddress() {
		    return address;
		  }

		  public void setAddress(String address) {
		    this.address = address;
		  }
		  
		  public double getPrescriptionBill() {
			return prescriptionBill;
		  }

		  public void setPrescriptionBill(double prescriptionBill) {
			this.prescriptionBill = prescriptionBill;
		  }
		  
		  public String getEmail() {
			return email;
		  }

		  public void setEmail(String email) {
			this.email = email;
		  }
		  
		  public String getBirthDate() {
			return birthDate;
		  }

		  public void setBirthDate(String birthDate) {
			this.birthDate = birthDate;
		  }
	
	/**
	 * Converts a Message customer to a real customer object ONLY USING id, name, ...
	 * @return
	 */ 
	public Customer convertToCustomer (){
		Customer realCustomer = new Customer();
		realCustomer.setId(id);
		realCustomer.setName(name);
		realCustomer.setAddress(address);
		realCustomer.setBirthDate(birthDate);
		realCustomer.setEmail(email);
		realCustomer.setTelephoneNumber(telephoneNumber);
		realCustomer.setPrescriptionBill(prescriptionBill);
		return realCustomer;
	}

}