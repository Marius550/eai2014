package de.java.ejb;

import javax.ejb.EJBException;

public class KeyConstraintViolation extends EJBException {

	private static final long serialVersionUID = -4967598074366408367L;

	public KeyConstraintViolation(String message) {
		super(message);
	}
}
