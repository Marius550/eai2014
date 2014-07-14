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
 * AbstractMessage class provides sharable JMS actions for queue: "/queue/DrugQueue04"
 */
public abstract class AbstractMessage {
	
	protected abstract String getQueue();

	protected abstract Message buildMessage(Session session) throws JMSException;

	public void sendMessage() throws NamingException, JMSException {
		InitialContext context = new InitialContext();
		ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = connectionFactory.createConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Message message = buildMessage(session);
		
		connection.start();
		Queue queue = (Queue) context.lookup(getQueue());
		MessageProducer sender = session.createProducer(queue);
		sender.send(message);		
		
		connection.close();
	}
	
	// TODO: Remove all the comments
	public Message sendMessageWithReply() throws NamingException, JMSException {
		InitialContext context = new InitialContext();
		ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = connectionFactory.createConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Message message = buildMessage(session);
		
		connection.start();
		
		Queue queue = (Queue) context.lookup(getQueue());
		MessageProducer sender = session.createProducer(queue);
		System.out.println("Nachricht senden.");
		sender.send(message);
		System.out.println("Nachricht gesendet.");
		
		if (message.getJMSReplyTo() == null){
			return null;
		}
		
		MessageConsumer consumer = session.createConsumer(message.getJMSReplyTo());
		MapMessage resultMsg = (MapMessage) consumer.receive(1000);
		System.out.println("Antwort erhalten.");
		
		// TODO: remove following if block (only for testing
		if (resultMsg != null){
			int totalNumberOfPrescriptions = resultMsg.getIntProperty("totalNumberOfPrescriptions");
			System.out.println("Ende. Ergebnis ist " + totalNumberOfPrescriptions);
		} else {
			System.out.println("Ende. Ergebnis ist null");
		}		
		
		connection.close();
		return resultMsg;
	}
}

