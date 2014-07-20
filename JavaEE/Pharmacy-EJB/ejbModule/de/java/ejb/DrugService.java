package de.java.ejb;

import java.util.Collection;
import java.util.Date;

import javax.ejb.Remote;

import de.java.domain.Drug;

@Remote
public interface DrugService {

  Collection<Drug> getAllDrugs();

  Collection<Drug> getAllDrugsLike(String searchTerm);

  Drug getDrug(int pzn);

  Drug getDrugWithInventoryEvents(int pzn);

  Drug createDrug(Drug drug);

  Drug updateMasterData(int pzn, String name, String description);

  Drug updateReplenishmentConfig(int pzn, long minimumInventoryLevel,
      long optimalInventoryLevel);

  Drug withdraw(int pzn, long quantity, Date dateOfAction);

  Drug restock(int pzn, long quantity, Date dateOfAction);

  Drug replenish(int pzn, long quantity, Date dateOfAction);
}
