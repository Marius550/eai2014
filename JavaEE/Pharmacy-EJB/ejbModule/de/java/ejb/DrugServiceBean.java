package de.java.ejb;

import java.util.Collection;
import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.java.domain.Drug;

@Stateless
public class DrugServiceBean implements DrugService {

  @PersistenceContext
  private EntityManager em;

  @Override
  public Collection<Drug> getAllDrugs() {
    return em.createQuery("FROM Drug", Drug.class).getResultList();
  }

  @Override
  public Collection<Drug> getAllDrugsLike(String searchTerm) {
    if (empty(searchTerm)) {
      return getAllDrugs();
    }

    final String query = "SELECT d FROM Drug d WHERE d.pzn = :pzn OR lower(d.name) LIKE :searchTerm";
    return em.createQuery(query, Drug.class)
        .setParameter("searchTerm", prepareUniversalMatch(searchTerm))
        .setParameter("pzn", parseIntOrDefaultToZero(searchTerm))
        .getResultList();
  }

  private boolean empty(String searchTerm) {
    return searchTerm == null || searchTerm.trim().isEmpty();
  }

  private int parseIntOrDefaultToZero(String searchTerm) {
    try {
      return Integer.parseInt(searchTerm);
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  private String prepareUniversalMatch(String searchTerm) {
    return "%" + searchTerm.toLowerCase() + "%";
  }

  @Override
  public Drug getDrug(int pzn) {
    return em.find(Drug.class, pzn);
  }

  @Override
  public Drug getDrugWithInventoryEvents(int pzn) {
    Drug drug = getDrug(pzn);
    forceLoadOfInventoryEvents(drug);
    return drug;
  }

  private void forceLoadOfInventoryEvents(Drug drug) {
    drug.getEvents().size();
  }

  @Override
  public Drug createDrug(Drug newDrug) {
    if (em.createQuery("SELECT COUNT(*) FROM Drug WHERE pzn=:pzn",
        Long.class).setParameter("pzn", newDrug.getPzn())
        .getSingleResult() > 0)
      throw new KeyConstraintViolation(String.format(
          "Drug with PZN: %s already in database", newDrug.getPzn()));

    em.persist(newDrug);
    return newDrug;
  }

  @Override
  public Drug updateMasterData(int pzn, String name, String description) {
    Drug drug = getDrug(pzn);
    drug.setName(name);
    drug.setDescription(description);
    return drug;
  }

  @Override
  public Drug updateReplenishmentConfig(int pzn, long minimumInventoryLevel, long optimalInventoryLevel) {
    Drug drug = getDrug(pzn);
    drug.setMinimumInventoryLevel(minimumInventoryLevel);
    drug.setOptimalInventoryLevel(optimalInventoryLevel);
    return drug;
  }

  @Override
  public Drug withdraw(int pzn, long quantity, Date dateOfAction) {
    Drug drug = getDrug(pzn);
    drug.withdraw(quantity, dateOfAction);
    return drug;
  }

  @Override
  public Drug restock(int pzn, long quantity, Date dateOfAction) {
    Drug drug = getDrug(pzn);
    drug.restock(quantity, dateOfAction);
    return drug;
  }

  @Override
  public Drug replenish(int pzn, long quantity, Date dateOfAction) {
    Drug drug = getDrug(pzn);
    drug.replenish(quantity, dateOfAction);
    return drug;
  }
}
