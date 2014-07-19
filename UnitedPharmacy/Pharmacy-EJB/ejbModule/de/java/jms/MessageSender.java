package de.java.jms;

import javax.ejb.EJBException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.naming.NamingException;

public class MessageSender {
	public static void send(AbstractMessage message) {
	    try {      
	  	  message.sendMessage();
	    } catch (NamingException e) {
	      	throw new EJBException(e);
	  	} catch (JMSException e) {
	  		throw new EJBException(e);
	  	}
	}
	public static Message sendWithReply(AbstractMessage message) {
	    try {      
	  	  return message.sendMessageWithReply();
	    } catch (NamingException e) {
	      	throw new EJBException(e);
	  	} catch (JMSException e) {
	  		throw new EJBException(e);
	  	}
	}
}
