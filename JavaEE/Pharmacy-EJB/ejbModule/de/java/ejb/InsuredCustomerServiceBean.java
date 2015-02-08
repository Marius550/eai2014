package de.java.ejb;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.java.domain.InsuredCustomer;

@Stateless
public class InsuredCustomerServiceBean implements InsuredCustomerService {

  @PersistenceContext
  private EntityManager em;
  
  @Override
  public InsuredCustomer getInsuredCustomer(long id) {
    return em.find(InsuredCustomer.class, id);
  }

  @Override
  public Collection<InsuredCustomer> getAllInsuredCustomers() {
    return em.createQuery("From InsuredCustomer", InsuredCustomer.class).getResultList();
  }

  @Override 
  public InsuredCustomer createInsuredCustomer(InsuredCustomer newInsuredCustomer) {
	  /*
    if (em
        .createQuery("SELECT COUNT(*) FROM Customer WHERE name = :name", Long.class)
        .setParameter("name", newCustomer.getName()).getSingleResult() > 0) {
      throw new KeyConstraintViolation(String.format(
          "Customer with name: %s already in database", newCustomer.getName()));
    }
    */
    em.persist(newInsuredCustomer);
    return newInsuredCustomer;
  }

  @Override
  public InsuredCustomer update(long id, String vorname, String nachname, String email) {
	InsuredCustomer insuredCustomer = getInsuredCustomer(id);
	insuredCustomer.setVorname(vorname);
	insuredCustomer.setNachname(nachname);
	insuredCustomer.setEmail(email);
    return insuredCustomer;
  }

}
