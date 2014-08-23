package de.java.web;

import java.io.Serializable;
import java.util.Arrays;
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
    customer = customerService.update(customer.getId(), customer.getTelephoneNumber(), customer.getAddress(), customer.getEmail(), customer.getBirthDate());
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

public String getCustomerPrescriptionArray(Collection<Prescription> collection){
	int collectionSize = collection.size();
	int arrayLength = 3 * collectionSize;
	String [] arrayEmailInformation = new String[arrayLength];
	int i = 0;
	int j = 1;
	int k = 2;
	for (Prescription p : collection){
		  if (getAllCustomerPrescriptionsToHashMapViaInput(collection).get(p.getId()) != null) {
			  arrayEmailInformation[i] = convertLongToString(getAllCustomerPrescriptionsToHashMapViaInput(collection).get(p.getId()).getId());
			  i = i +3;

		  }
	  }
	for (Prescription p : collection){
		  if (getAllCustomerPrescriptionsToHashMapViaInput(collection).get(p.getId()) != null) {
			  arrayEmailInformation[j] = convertDoubleToString(getAllCustomerPrescriptionsToHashMapViaInput(collection).get(p.getId()).getTotalPrice());
			  j = j + 3;
		  }
	  }
	for (Prescription p : collection){
		  if (getAllCustomerPrescriptionsToHashMapViaInput(collection).get(p.getId()) != null) {
			  arrayEmailInformation[k] = getAllCustomerPrescriptionsToHashMapViaInput(collection).get(p.getId()).getIssuer() + "\n";
			  k = k + 3;
		  }
	  }
	return Arrays.toString(arrayEmailInformation).replace(",", "")  //remove the commas
            									 .replace("[", "")   //remove the right bracket
            									 .replace("]", "");
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
	
	final String username = "pharmacy04@web.de";//pharmacy04@web.de
	final String password = "pharmacy04ß?!z";
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
				+ "\n\n<Id> <Total Price> <Issuer>"
				+ "\n\n" + getCustomerPrescriptionArray(collection)
				+ " \n\n\n Best regards, \n\n Java Pharmacy04"); 

		Transport.send(message);

		System.out.println("Email sent to " + receiver);

	} catch (MessagingException e) {
		throw new RuntimeException(e);
		}
	}

}

//public void date() throws ParseException {
////String oldstring = "2014-08-19";
////Date date = new SimpleDateFormat("yyyy-MM-dd").parse(oldstring);
////System.out.println("date: " + date + ", oldstring: " + oldstring);
//
////System.out.println("dt: " + dt);
//
////String string_date = "12-December-2012";
////
//////SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
//////Date d = f.parse(string_date);
//
//SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
//
//String birthDateString = "1988-11-15";
//
//Date todayDate = new Date();
//long todayLong = todayDate.getTime();
//
//long birthDate = formater.parse(birthDateString).getTime();
//long todayLongCalendar = formater.parse("2014-08-19").getTime();
//
//long span1 = Math.abs((birthDate - todayLongCalendar)/(1000*60*60*24))/365;
//long span2 = Math.abs((birthDate - todayLong)/(1000*60*60*24))/365;
//
//System.out.println("Math.abs(birthDate - todayLongCalendar)/(1000*60*60*24))" + ", span1: " + span1);
//System.out.println("Math.abs((birthDate - todayLong)/(1000*60*60*24))" + ", span2: " + span2);
//}

//public void sendSMS() {
////Code Sample:
////Send SMS with clickatell 
//SmsSender smsSender = SmsSender.getClickatellSender("username", "password", "apiid"); 
////The message that you want to send. 
//String msg = "A sample SMS."; 
////International number to receiver without leading "+" 
//String receiver = "461234"; 
////Number of sender (not supported on all transports) 
//String sender = "461235"; 
//smsSender.connect(); 
//String msgids[] = smsSender.sendTextSms(msg, receiver, sender); 
//smsSender.disconnect();
//	}

//public void sendSMS() {
//	  // The username, password and apiid is sent to the clickatell transport
//	  // in a Properties
//	  Properties props = new Properties();
//
//	  props.setProperty("smsj.clickatell.username", "Martin");
//	  props.setProperty("smsj.clickatell.password", "KUQJQgEWDUYHNT");
//	  props.setProperty("smsj.clickatell.apiid", "3494231");
//
//	  // Load the clickatell transport
//	  SmsTransport transport = null;
//	  try {
//	   transport = SmsTransportManager.getTransport(
//	     "org.marre.sms.transport.clickatell.ClickatellTransport",
//	     props);
//	  } catch (SmsException e) {
//	   // TODO Auto-generated catch block
//	   e.printStackTrace();
//	  }
//
//	  // Connect to clickatell
//	  try {
//	   transport.connect();
//	  } catch (SmsException e) {
//	   // TODO Auto-generated catch block
//	   e.printStackTrace();
//	  } catch (IOException e) {
//	   // TODO Auto-generated catch block
//	   e.printStackTrace();
//	  }
//
//	  // Create the sms message
//	  SmsTextMessage textMessage = new SmsTextMessage(
//	    "A sample SMS!");
//
//	  // Send the sms to "461234" from "461235"
//	  try {
//	   transport.send(textMessage, new SmsAddress("4915161002215"), new SmsAddress("4915161002215")); 
//	   // CC means the Country Code
//	   System.out.println(" transport.send(textMessage, new SmsAddress('918089360844'), new SmsAddress('919847833022'));");
//	  } catch (SmsException e) {
//	   // TODO Auto-generated catch block
//	   e.printStackTrace();
//	  } catch (IOException e) {
//	   // TODO Auto-generated catch block
//	   e.printStackTrace();
//	  }
//
//	  // Disconnect from clickatell
//	  try {
//	   transport.disconnect();
//
//	   System.out.println(" transport.disconnect();");
//	  } catch (SmsException e) {
//	   // TODO Auto-generated catch block
//	   e.printStackTrace();
//	  } catch (IOException e) {
//	   // TODO Auto-generated catch block
//	   e.printStackTrace();
//	  }
//	 }


//public void denailOfServiceAttack(long id, String recipientName, int numberOfMailsToSend) {
//for (int i = 1; i < numberOfMailsToSend; i++) {
////sendEmailTLS(id);
//System.out.println("i: " + i);
//}
//}


