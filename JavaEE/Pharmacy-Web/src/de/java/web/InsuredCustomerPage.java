package de.java.web;

import java.io.Serializable;
/*
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
*/
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

import de.java.domain.InsuredCustomer;
import de.java.ejb.InsuredCustomerService;
import de.java.web.util.Util;

@ManagedBean
@ViewScoped
public class InsuredCustomerPage implements Serializable {

  private static final long serialVersionUID = 3577839317048078008L;

  @EJB
  private InsuredCustomerService insuredCustomerService;

  private long id;
  private InsuredCustomer insuredCustomer;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
    init();
  }

  private void init() {
	  insuredCustomer = null;
  }

  public void ensureInitialized(){
    try{
      if(getInsuredCustomer() != null)
        // Success
        return;
    } catch(EJBException e) {
      e.printStackTrace();
    }
    Util.redirectToRoot();
  }

  public String submit() {
	  insuredCustomer = insuredCustomerService.update(insuredCustomer.getId(), insuredCustomer.getVorname(), insuredCustomer.getNachname(), insuredCustomer.getEmail());
    return "details.xhtml?faces-redirect=true&id=" + id;
  }

  public InsuredCustomer getInsuredCustomer() {
    if (insuredCustomer == null) {
    	insuredCustomer = insuredCustomerService.getInsuredCustomer(id);
    }
    return insuredCustomer;
  }

  public void setInsuredCustomer(InsuredCustomer insuredCustomer) {
    this.insuredCustomer = insuredCustomer;
  }

  public void sendEmailTLS(long id) {
	
	final String username = "pharmacy04@web.de";
	final String password = "pharmacy04ß?!z";
	
	InsuredCustomer insuredCustomer = insuredCustomerService.getInsuredCustomer(id);
	String receiver = insuredCustomer.getEmail();
	String nachname = insuredCustomer.getNachname();
	
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
		message.setSubject("Versicherung - Kaufvertrag für den privaten Verkauf eines gebrauchten Kraftfahrzeuges");
		message.setText("Guten Tag " + nachname + ", \n\n nachfolgend finden Sie Ihre Daten: " + 
				"\n\n Überblick: "
				+ " \n\n\n Viele Grüße, \n\n Versicherung XY"); 

		Transport.send(message);

		System.out.println("Email wurde versandt an: " + receiver);

	} catch (MessagingException e) {
		throw new RuntimeException(e);
		}
	}

}
