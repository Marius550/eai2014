package de.java.jms;

import javax.ejb.EJBException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.naming.NamingException;

/**
 * Message sender class for Abstract Messages
 *
 */
public class MessageSender {
	/**
	 * Sends an abstract message
	 * @param message Message to be sent. Must implement AbstractMessage.
	 */
	public static void send(AbstractMessage message) {
	    try {      
	  	  message.sendMessage();
	    } catch (NamingException e) {
	      	throw new EJBException(e);
	  	} catch (JMSException e) {
	  		throw new EJBException(e);
	  	}
	}
	
	/**
	 * Sends an abstract message and returns the response
	 * @param message Message to be sent. Must implement AbstractMessage
	 * @return Message reply
	 */
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
