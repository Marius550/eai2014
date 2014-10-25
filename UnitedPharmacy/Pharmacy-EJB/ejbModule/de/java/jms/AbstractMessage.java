package de.java.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * AbstractMessage class provides sharable JMS actions 
 */
public abstract class AbstractMessage {
	
	/**
	 * Returns the Name of the queue a message should be sent to.
	 * Function is to be implemented in each message type.
	 * 
	 * @return Name of the message queue.
	 */
	protected abstract String getQueue();

	/**
	 * Builds up the message to be sent with all parameters.
	 * @param session the session, in which the message should be sent.
	 * @return the message to be sent.
	 * @throws JMSException
	 */
	protected abstract Message buildMessage(Session session) throws JMSException;

	/**
	 * Sends the JMS-message build in buildMessage() to queue depicted in getQueue().
	 * @throws NamingException
	 * @throws JMSException
	 */
	public void sendMessage() throws NamingException, JMSException {
		// Create session
		InitialContext context = new InitialContext();
		ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = connectionFactory.createConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		// Build message
		Message message = buildMessage(session);
		
		// Send message
		connection.start();
		Queue queue = (Queue) context.lookup(getQueue());
		MessageProducer sender = session.createProducer(queue);
		sender.send(message);		
		
		connection.close();
	}
	
	/**
	 * Sends the JMS-message build in buildMessage() to queue depicted in getQueue().
	 * Futhermore: create a temporary queue and wait for a reply.
	 * @throws NamingException
	 * @throws JMSException
	 */
	public Message sendMessageWithReply() throws NamingException, JMSException {
		InitialContext context = new InitialContext();
		ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = connectionFactory.createConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Message message = buildMessage(session);
		
		connection.start();
		
		Queue queue = (Queue) context.lookup(getQueue());
		MessageProducer sender = session.createProducer(queue);
		sender.send(message);
		
		if (message.getJMSReplyTo() == null){
			// Built message does not include JMSReply-argument and therefore does not expect an answer
			return null;
		}
		
		MessageConsumer consumer = session.createConsumer(message.getJMSReplyTo());
		// Wait 1 second for reply
		MapMessage resultMsg = (MapMessage) consumer.receive(1000);
		
		connection.close();
		return resultMsg;
	}
}

