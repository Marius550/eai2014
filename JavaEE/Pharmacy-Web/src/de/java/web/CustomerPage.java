package de.java.web;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import de.java.domain.customer.Customer;
import de.java.domain.prescription.Prescription;
import de.java.ejb.CustomerService;
import de.java.web.util.Util;

@ManagedBean
@ViewScoped
public class CustomerPage implements Serializable {

  private static final long serialVersionUID = 3577839317048078008L;

  @EJB
  private CustomerService customerService;

  private long id;
  private Customer customer;
  
  private Map<Long, Prescription> customerPrescriptions;
  
  private Map<Long, Prescription> customerPrescriptionsViaInput;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
    init();
  }

  private void init() {
    customer = null;
  }

  public void ensureInitialized(){
    try{
      if(getCustomer() != null)
        // Success
        return;
    } catch(EJBException e) {
      e.printStackTrace();
    }
    Util.redirectToRoot();
  }

  public String submit() {
    customer = customerService.update(customer.getId(), customer.getTelephoneNumber(), customer.getAddress(), customer.getEmail());
    return "details.xhtml?faces-redirect=true&id=" + id;
  }

  public Customer getCustomer() {
    if (customer == null) {
      customer = customerService.getCustomerWithPrescriptions(id);
    }
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }
  
  /**
   * Convert Collection <Prescription> into HashMap and assign id to each map element
 * @param collection
 * @return Map<Long, Prescription> map
 */
private Map<Long, Prescription> convertPharmacyCustomerPrescriptionsCollectionIntoHashMap(Collection<Prescription> collection){
	  Map<Long, Prescription> map = new HashMap<Long, Prescription>();
	  for (Prescription mPrescription : collection){
		  map.put(mPrescription.getId(), mPrescription);
	  }
	  return map;
	}

/** Call the CustomerWithPrescriptions collection for a specific customer
 * @param customer
 * @return Map<Long, Prescription> based on the CustomerWithPrescriptions collection
 */
public Map<Long, Prescription> getAllCustomerPrescriptionsToHashMap(Customer customer){
	  if (customerPrescriptions == null){
		  customerPrescriptions = convertPharmacyCustomerPrescriptionsCollectionIntoHashMap(customerService.getCustomerWithPrescriptions(customer.getId()).getPrescriptions());
	  }
	  return customerPrescriptions;
}

/** Iterate over the CustomerWithPrescriptions HashMap and only sum up the total price of prescriptions of the specific customer
 * @param customer
 * @return Summed up totalPrice
 */
public double getCustomerPrescriptionBill(Customer customer){
	double totalPrice = 0;
	  for (Prescription p : customerService.getCustomerWithPrescriptions(customer.getId()).getPrescriptions()){
		  if (getAllCustomerPrescriptionsToHashMap(customer).get(p.getId()) != null) {
		  totalPrice = totalPrice + getAllCustomerPrescriptionsToHashMap(customer).get(p.getId()).getTotalPrice();  
		  }
	  }
	  return totalPrice;
	}

public double getCustomerPrescriptionSum(long id) {
	  Customer customer = customerService.getCustomer(id);
	  return getCustomerPrescriptionBill(customer);
	}


public Map<Long, Prescription> getAllCustomerPrescriptionsToHashMapViaInput(Collection<Prescription> collection){
	  if (customerPrescriptionsViaInput == null){
		  customerPrescriptionsViaInput = convertPharmacyCustomerPrescriptionsCollectionIntoHashMap(collection);
	  }
	  return customerPrescriptionsViaInput;
}

public String[] getCustomerPrescriptionArray(Collection<Prescription> collection){
	int collectionSize = collection.size();
	int arrayLength = 3 * collectionSize;
	String [] arrayEmailInformation = new String[arrayLength];
	int i = 0;
	int j = 2;
	int k = 4;
	for (Prescription p : collection){
		  if (getAllCustomerPrescriptionsToHashMapViaInput(collection).get(p.getId()) != null) {

			  arrayEmailInformation[i] = convertLongToString(getAllCustomerPrescriptionsToHashMapViaInput(collection).get(p.getId()).getId());
			  arrayEmailInformation[j] = getAllCustomerPrescriptionsToHashMapViaInput(collection).get(p.getId()).getIssuer();
			  arrayEmailInformation[k] = convertDoubleToString(getAllCustomerPrescriptionsToHashMapViaInput(collection).get(p.getId()).getTotalPrice());

			  i++;
			  j++; 
			  k++;
		  }
	  }
	return arrayEmailInformation;
	}

public String convertLongToString(long longNumber) {
	String stringLong = Long.toString(longNumber);
	return stringLong;
}

public String convertDoubleToString(double doubleNumber) {
	String stringDouble = Double.toString(doubleNumber);
	return stringDouble;
}

public void sendEmailTLS(long id, String recipientName, double prescriptionBill, Collection<Prescription> collection) {

	String [] arrayEmailInformation = getCustomerPrescriptionArray(collection);
	
	final String username = "pharmacy04@web.de";//pharmacy04@web.de
	final String password = "";
	String receiver;
	
	Customer customer = customerService.getCustomer(id);
	receiver = customer.getEmail();
	
	Properties props = new Properties();
	props.put("mail.smtp.auth", "true");
	props.put("mail.smtp.starttls.enable", "true");
	props.put("mail.smtp.host", "smtp.web.de");
	props.put("mail.smtp.port", "587");

	Session session = Session.getInstance(props,
	  new javax.mail.Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, password);
		}
	  });

	try {
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("pharmacy04@web.de"));
		message.setRecipients(Message.RecipientType.TO,
			InternetAddress.parse(receiver));
		message.setSubject("Java Pharmacy04 - Prescription Bill");
		message.setText("Dear " + recipientName + ", \n\n Your overall prescription bill is: " + prescriptionBill + "€." + 
				"\n\n Below you can check your prescription details: "
				+ "\n\n Id: " + arrayEmailInformation[0] + "  Issuer: " + arrayEmailInformation[2] + "  Price : " + arrayEmailInformation[4]
				+ " \n\n\n\n Best regards, \n\n Pharmacy04");

		Transport.send(message);

		System.out.println("Email sent");

	} catch (MessagingException e) {
		throw new RuntimeException(e);
		}
	}

}

//public void denailOfServiceAttack(long id, String recipientName, int numberOfMailsToSend) {
//for (int i = 1; i < numberOfMailsToSend; i++) {
////sendEmailTLS(id);
//System.out.println("i: " + i);
//}
//}
